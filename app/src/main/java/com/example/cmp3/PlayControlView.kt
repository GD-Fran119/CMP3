package com.example.cmp3

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import com.example.animations.ImageFadeInAnimation
import com.example.playerStuff.Player
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.palette.graphics.Palette
import com.example.bottomSheets.SongListItemsDialogFragment
import com.example.config.GlobalPreferencesConstants
import com.example.config.PlayerStateSaver
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PlayControlView : AppCompatActivity(){

    companion object{
        private const val SQUARE_IMAGE_LAYOUT = 1
        private const val ROUND_IMAGE_LAYOUT = 2
        private const val WIDE_IMAGE_LAYOUT = 3
    }

    private lateinit var title: TextView
    private lateinit var desc: TextView
    private lateinit var img: ImageView
    private lateinit var playModeBtn: MaterialButton
    private lateinit var playButton: MaterialButton
    private lateinit var listButton: MaterialButton
    private val player = Player.instance
    private var defaultBGColor: Int = 0
    private var currentColor: Int = 0
    private var currentLayout = -1

    private val listItems = SongListItemsDialogFragment(Player.instance.getList())

    private lateinit var seekBar: SeekBar
    private var seekbarJob: Job? = null
    private var findImageJob : Job? = null

    private var listener : Player.OnSongChangedListener? = null

    override fun onStart() {
        super.onStart()

        checkAndSetUpLayout()

        setUpVariablesAndButtons()

        setUpListener()

        seekbarJob = CoroutineScope(Dispatchers.Main).launch {
            var playerTouched = false
            seekBar.setOnSeekBarChangeListener(object: OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    seekBar?.progress = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    playerTouched = true
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    playerTouched = false
                    if(seekBar != null) {
                        Player.instance.setTime(seekBar.progress.toUInt())
                        changePlayButton()
                    }
                }

            })

            while(true){
                if(!playerTouched && Player.instance.isAvailableProgress()){
                    seekBar.max = Player.instance.getCurrentSongDuration().toInt()
                    seekBar.progress = Player.instance.getCurrentSongProgress()
                }
                delay(500)
            }
        }

    }

    override fun onPause() {
        super.onPause()
        try{
            seekbarJob?.cancel()
        }catch (_: Exception){}

    }

    private fun setUpListener(){
        listener = object: Player.OnSongChangedListener{
            override fun listen() {
                updateUI()
            }
        }
        player.onSongChangedListener = listener
    }

    private var saverJob: Job? = null
    private fun updateUI(){
        val song = player.getCurrentSong() ?: return

        title.text = song.title
        desc.text = if(song.artist == "<unknown>") "Unknown"
        else song.artist

        changeSongImg()
        CoroutineScope(Dispatchers.Main).launch {
            delay(100)
            changePlayButton()
        }

        saverJob?.cancel()
        saverJob = CoroutineScope(Dispatchers.Default).launch {
            PlayerStateSaver.saveState(this@PlayControlView)
        }
    }

    private fun checkAndSetUpLayout(){
        val prefs = getSharedPreferences(ChangeLayoutActivity.PLAY_CONTROL_ACT_PREFERENCES, Context.MODE_PRIVATE)
        var layoutSaved = prefs.getInt(GlobalPreferencesConstants.LAYOUT_KEY, -1)

        //No config
        if(layoutSaved == -1) {
            prefs.edit().apply {
                putInt(GlobalPreferencesConstants.LAYOUT_KEY, 1)
            }.apply()

            layoutSaved = 1
        }

        //Layout changed
        if(currentLayout != layoutSaved){
            //Update layout so it matches config
            currentLayout = layoutSaved

            when(prefs.getInt(GlobalPreferencesConstants.LAYOUT_KEY, 1)){
                SQUARE_IMAGE_LAYOUT -> {
                    setContentView(R.layout.activity_play_control_view1)
                }
                ROUND_IMAGE_LAYOUT -> {
                    setContentView(R.layout.activity_play_control_view2)
                }
                WIDE_IMAGE_LAYOUT -> {
                    setContentView(R.layout.activity_play_control_view3)
                }
                else -> {
                    prefs.edit().apply {
                        putInt(GlobalPreferencesConstants.LAYOUT_KEY, 1)
                    }.apply()
                    setContentView(R.layout.activity_play_control_view1)
                }
            }
        }
    }

    private fun showSongs() {
        listItems.show(supportFragmentManager, "dialog")
    }

    private fun setUpVariablesAndButtons(){
        title = findViewById(R.id.play_control_title)
        desc = findViewById(R.id.play_control_desc)
        img = findViewById(R.id.play_control_img)

        title.isSelected = true

        seekBar = findViewById(R.id.play_control_seekbar)

        playButton = findViewById(R.id.play_control_play_button)
        playButton.setOnClickListener{
            playPauseBtn()
        }
        listButton = findViewById(R.id.play_control_songlist_button)
        listButton.setOnClickListener{
            showSongs()
        }

        playModeBtn = findViewById(R.id.play_control_playmode_button)
        playModeBtn.setOnClickListener{
            changePlayModeBtn()
        }
        changePlayModeBtnImg()

        findViewById<MaterialButton>(R.id.play_control_next_button).setOnClickListener{
            player.playNext()
            playButton.foreground = getDrawable(R.drawable.ic_pause)
        }

        findViewById<MaterialButton>(R.id.play_control_previous_button).setOnClickListener{
            player.playPrevious()
            playButton.foreground = getDrawable(R.drawable.ic_pause)
        }

        seekBar = findViewById(R.id.play_control_seekbar)
        if(Player.instance.isAvailableProgress()){
            seekBar.max = Player.instance.getCurrentSongDuration().toInt()
            seekBar.progress = Player.instance.getCurrentSongProgress()
        }

        findViewById<MaterialButton>(R.id.topbar_back_button).setOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.topbar_options_button).setOnClickListener {
            val popup = PopupMenu(this, it)
            popup.menuInflater.inflate(R.menu.customization_menu, popup.menu)
            popup.show()

            popup.setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.playlist_change_layout -> {
                        val intent = Intent(this@PlayControlView, ChangeLayoutActivity::class.java)
                        intent.putExtra(ChangeLayoutActivity.ACTIVITY_LAYOUT_CHANGE, ChangeLayoutActivity.PLAY_CONTROL_ACTIVITY_LAYOUT)
                        startActivity(intent)
                    }

                    R.id.playlist_change_style -> Toast.makeText(
                        this@PlayControlView,
                        "Change style",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                true
            }
        }
    }

    private fun changeSongImg() {
        img.setImageResource(R.drawable.ic_music_note)
        img.foreground = null

        findViewById<ConstraintLayout>(R.id.play_control_layout).setBackgroundColor(defaultBGColor)
        findImageJob?.cancel()
        findImageJob = CoroutineScope(Dispatchers.Default).launch {
            val currentSong = player.getCurrentSong()
            if(currentSong != null){
                val mediaRetriever = MediaMetadataRetriever()
                mediaRetriever.setDataSource(currentSong.path)

                val data = mediaRetriever.embeddedPicture
                mediaRetriever.release()


                if(data != null) {
                    val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)

                    when(currentLayout){
                        ROUND_IMAGE_LAYOUT -> setRoundedBitmapToImageView(bitmap)
                        else -> setBitmapToImageView(bitmap)
                    }

                    Palette.from(bitmap).generate{palette ->
                        if(palette != null) {
                            val defaultColor = getColor(R.color.light_blue_400)
                            currentColor = palette.getDarkMutedColor(defaultColor)
                            if(currentColor != defaultColor)
                                findViewById<ConstraintLayout>(R.id.play_control_layout).setBackgroundColor(currentColor)
                            else
                                findViewById<ConstraintLayout>(R.id.play_control_layout).setBackgroundColor(palette.getDarkVibrantColor(defaultColor))
                        }
                    }
                }
            }
        }
    }

    private suspend fun setRoundedBitmapToImageView(bitmap: Bitmap){
        val height = bitmap.height
        val width = bitmap.width
        val dim = Integer.max(height, width)
        val croppedBitmap = ThumbnailUtils.extractThumbnail(bitmap, dim, dim)
        val roundedBitmapDrawable=
            RoundedBitmapDrawableFactory.create(resources, croppedBitmap)

        roundedBitmapDrawable.isCircular = true

        withContext(Dispatchers.Main) {
            img.foreground = getDrawable(R.drawable.circle_album_foreground)
            img.setImageDrawable(roundedBitmapDrawable)
            img.startAnimation(ImageFadeInAnimation(0f, 1f))
        }
    }

    private suspend fun setBitmapToImageView(bitmap: Bitmap){
        withContext(Dispatchers.Main) {
            img.setImageBitmap(bitmap)
            img.startAnimation(ImageFadeInAnimation(0f, 1f))
        }
    }

    private fun changePlayModeBtnImg() {
        if(Player.instance.isListLoop()){
            playModeBtn.foreground = getDrawable(R.drawable.ic_repeat_list)
        } else if(Player.instance.isSongLoop()){
            playModeBtn.foreground = getDrawable(R.drawable.ic_repeat_current)
        }else{
            playModeBtn.foreground = getDrawable(R.drawable.ic_random_mode)
        }

    }

    private fun changePlayButton(){
        if(player.isPlayingSong()){
            playButton.foreground = getDrawable(R.drawable.ic_pause)
        }
        else{
            playButton.foreground = getDrawable(R.drawable.ic_play)
        }
    }

    private fun changePlayModeBtn(){
        player.changePlayMode()
        changePlayModeBtnImg()
        CoroutineScope(Dispatchers.Default).launch {
            PlayerStateSaver.saveState(this@PlayControlView)
        }
    }
    private fun playPauseBtn(){
        if(player.isPlayingSong()){
            player.pause()
        }
        else{
            player.play()
        }
        changePlayButton()
    }
}