package com.example.cmp3

import com.example.config.CurrentSongAndPlaylistConfigSaver
import com.example.animations.ImageFadeInAnimation
import com.example.playerStuff.Player
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.palette.graphics.Palette
import com.example.bottomSheets.ListItemListDialogFragment
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PlayControlView : AppCompatActivity(){
    private lateinit var title: TextView
    private lateinit var desc: TextView
    private lateinit var img: ImageView
    private lateinit var playModeBtn: MaterialButton
    private lateinit var playButton: MaterialButton
    private lateinit var listButton: MaterialButton
    private val player = Player.instance
    private var defaultBGColor: Int = 0
    private var currentColor: Int = 0

    private val listItems = ListItemListDialogFragment()

    private lateinit var seekBar: SeekBar
    private var seekbarJob: Job? = null
    private var findImageJob : Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_control_view)

        title = findViewById(R.id.play_control_title)
        desc = findViewById(R.id.play_control_desc)
        img = findViewById(R.id.play_control_img)
        defaultBGColor = findViewById<ConstraintLayout>(R.id.play_control_main_container).solidColor

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
            CurrentSongAndPlaylistConfigSaver.savePlayList(this)
        }

        findViewById<MaterialButton>(R.id.play_control_next_button).setOnClickListener{
            player.next()
            playButton.foreground = getDrawable(R.drawable.ic_pause)
        }

        findViewById<MaterialButton>(R.id.play_control_previous_button).setOnClickListener{
            player.previous()
            playButton.foreground = getDrawable(R.drawable.ic_pause)
        }

        seekBar = findViewById(R.id.play_control_seekbar)
        if(Player.instance.isAvailableProgress()){
            seekBar.max = Player.instance.getCurrentSongDuration().toInt()
            seekBar.progress = Player.instance.getCurrentSongProgress()
        }

    }

    private fun showSongs() {
        listItems.show(supportFragmentManager, "dialog")
    }

    private fun changeSongImg() {
        img.setImageResource(R.drawable.ic_music_note)
        findViewById<ConstraintLayout>(R.id.play_control_main_container).setBackgroundColor(defaultBGColor)
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
                    Palette.from(bitmap).generate{palette ->
                        if(palette != null) {
                            val defaultColor = getColor(R.color.light_blue_400)
                            currentColor = palette.getDarkMutedColor(defaultColor)
                            if(currentColor != defaultColor)
                                findViewById<ConstraintLayout>(R.id.play_control_main_container).setBackgroundColor(currentColor)
                            else
                                findViewById<ConstraintLayout>(R.id.play_control_main_container).setBackgroundColor(palette.getDarkVibrantColor(defaultColor))
                        }
                    }

                    withContext(Dispatchers.Main) {
                        img.setImageBitmap(bitmap)
                        img.startAnimation(ImageFadeInAnimation(0f, 1f))
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        try{
            seekbarJob?.cancel()
            player.onSongChangedListener = null
        }catch (_: Exception){}

    }
    override fun onStart() {
        super.onStart()
        player.onSongChangedListener = object: Player.OnSongChangedListener{
            override fun listen() {
                val song = player.getCurrentSong() ?: return
                title.text = song.title
                desc.text = if(song.artist == "<unknown>") "Unknown"
                else song.artist

                img.setImageResource(R.drawable.ic_music_note)
                changeSongImg()
            }
        }

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

    private fun changePlayModeBtnImg() {
        if(Player.instance.isListLoop()){
            playModeBtn.background = getDrawable(R.drawable.ic_repeat_list)
        } else if(Player.instance.isSongLoop()){
            playModeBtn.background = getDrawable(R.drawable.ic_repeat_current)
        }else{
            playModeBtn.background = getDrawable(R.drawable.ic_random_mode)
        }

        CurrentSongAndPlaylistConfigSaver.savePlayList(this)
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