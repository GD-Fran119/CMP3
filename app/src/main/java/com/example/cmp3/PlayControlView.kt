package com.example.cmp3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Bitmap
import com.example.animations.ImageFadeInAnimation
import com.example.playerStuff.Player
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
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
import kotlin.properties.Delegates

/**
 * Activity where the user can control haw a playlist is played
 */
class PlayControlView : AppCompatActivity(){

    companion object{
        private const val SQUARE_IMAGE_LAYOUT = 1
        private const val ROUND_IMAGE_LAYOUT = 2
        private const val WIDE_IMAGE_LAYOUT = 3
    }

    private lateinit var bgLayout: ConstraintLayout
    private lateinit var title: TextView
    private lateinit var desc: TextView
    private lateinit var img: ImageView
    private var imgDefaultColor by Delegates.notNull<Int>()
    private lateinit var playModeBtn: MaterialButton
    private lateinit var playButton: MaterialButton
    private lateinit var listButton: MaterialButton
    private lateinit var previousButton: MaterialButton
    private lateinit var nextButton: MaterialButton
    private lateinit var backButton: MaterialButton
    private lateinit var optionsButton: MaterialButton
    private val player = Player.instance
    //BG color changes with the song image
    private var defaultBGColor: Int = 0
    //If current song has an embedded image
    //The BG is changed according to that image
    private var currentBGColor: Int = 0
    //Custom layout established
    private var currentLayout = -1
    //Custom style version
    private var currentStyleVersion = -1
    //Check whether style must be set no matter the version
    private var mustChangeStyle = false

    //Dialog to show if user wants to know the songs in the playlist or wants to set the current song to a specific one
    private val listItems = SongListItemsDialogFragment(Player.instance.getList())

    private lateinit var seekBar: SeekBar
    private var seekbarJob: Job? = null
    private var findImageJob : Job? = null

    private var listener : Player.OnSongChangedListener? = null

    override fun onStart() {

        super.onStart()

        checkAndSetUpLayout()

        setUpVariablesAndButtons()

        checkAndSetUpStyle()

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

    /**
     * Checks whether the current style has been changed. If so, the new style is set
     */
    private fun checkAndSetUpStyle() {
        val prefs = getSharedPreferences(GlobalPreferencesConstants.PLAY_CONTROL_ACT_PREFERENCES, MODE_PRIVATE)
        var version = prefs.getInt(PreferencesConstants.STYLE_VERSION_KEY, 0)

        if(version == 0) {
            createStylePreferences(prefs)
            version = prefs.getInt(PreferencesConstants.STYLE_VERSION_KEY, 0)
        }

        if(!mustChangeStyle && version == currentStyleVersion) return

        currentStyleVersion = version

        prefs.apply {
            val constants = PreferencesConstants
            defaultBGColor = getInt(constants.GENERAL_LAYOUT_BG_KEY, getColor(R.color.default_layout_bg))
            bgLayout.backgroundTintList = ColorStateList.valueOf(defaultBGColor)

            backButton.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.BACK_BTN_BG_KEY, getColor(R.color.default_buttons_bg))
            )
            backButton.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.BACK_BTN_FG_KEY, getColor(R.color.default_buttons_fg))
            )
            optionsButton.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.OPTIONS_BTN_BG_KEY, getColor(R.color.default_buttons_bg))
            )
            optionsButton.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.OPTIONS_BTN_FG_KEY, getColor(R.color.default_buttons_fg))
            )

            val songInfoColor = getInt(constants.SONG_TEXT_KEY, getColor(R.color.default_play_control_text_color))
            title.setTextColor(songInfoColor)
            desc.setTextColor(songInfoColor)

            img.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.SONG_IMAGE_BG_KEY, getColor(R.color.default_image_placeholder_bg))
            )
            imgDefaultColor = getInt(constants.SONG_IMAGE_FG_KEY, getColor(R.color.default_image_placeholder_fg))
            img.foregroundTintList = ColorStateList.valueOf(imgDefaultColor)

            seekBar.progressTintList = ColorStateList.valueOf(
                getInt(constants.SEEK_BAR_KEY, getColor(R.color.default_seek_bar_color))
            )
            seekBar.progressBackgroundTintList = seekBar.progressTintList
            seekBar.thumbTintList = seekBar.progressTintList

            playButton.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.PLAY_BTN_BG_KEY, getColor(R.color.default_buttons_bg))
            )
            playButton.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.PLAY_BTN_FG_KEY, getColor(R.color.default_buttons_fg))
            )
            playModeBtn.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.PLAY_MODE_BTN_BG_KEY, getColor(R.color.default_buttons_bg))
            )
            playModeBtn.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.PLAY_MODE_BTN_FG_KEY, getColor(R.color.default_buttons_fg))
            )
            listButton.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.LIST_BTN_BG_KEY, getColor(R.color.default_buttons_bg))
            )
            listButton.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.LIST_BTN_FG_KEY, getColor(R.color.default_buttons_fg))
            )
            previousButton.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.PREVIOUS_BTN_BG_KEY, getColor(R.color.default_buttons_bg))
            )
            previousButton.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.PREVIOUS_BTN_FG_KEY, getColor(R.color.default_buttons_fg))
            )
            nextButton.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.NEXT_BTN_BG_KEY, getColor(R.color.default_buttons_bg))
            )
            nextButton.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.NEXT_BTN_FG_KEY, getColor(R.color.default_buttons_fg))
            )
        }
        mustChangeStyle = false
    }

    /**
     * Creates the default style for the view
     * @param preferences [SharedPreferences] where the style will be stored
     */
    private fun createStylePreferences(preferences: SharedPreferences) {
        preferences.edit().apply {
            val constants = PreferencesConstants
            putInt(constants.GENERAL_LAYOUT_BG_KEY, getColor(R.color.default_layout_bg))
            putInt(constants.BACK_BTN_BG_KEY, getColor(R.color.default_buttons_bg))
            putInt(constants.BACK_BTN_FG_KEY, getColor(R.color.default_buttons_fg))
            putInt(constants.OPTIONS_BTN_BG_KEY, getColor(R.color.default_buttons_bg))
            putInt(constants.OPTIONS_BTN_FG_KEY, getColor(R.color.default_buttons_fg))
            putInt(constants.SONG_TEXT_KEY, getColor(R.color.default_play_control_text_color))
            putInt(constants.SONG_IMAGE_BG_KEY, getColor(R.color.default_image_placeholder_bg))
            putInt(constants.SONG_IMAGE_FG_KEY, getColor(R.color.default_image_placeholder_fg))
            putInt(constants.SEEK_BAR_KEY, getColor(R.color.default_seek_bar_color))
            putInt(constants.PLAY_BTN_BG_KEY, getColor(R.color.default_buttons_bg))
            putInt(constants.PLAY_BTN_FG_KEY, getColor(R.color.default_buttons_fg))
            putInt(constants.PLAY_MODE_BTN_BG_KEY, getColor(R.color.default_buttons_bg))
            putInt(constants.PLAY_MODE_BTN_FG_KEY, getColor(R.color.default_buttons_fg))
            putInt(constants.LIST_BTN_BG_KEY, getColor(R.color.default_buttons_bg))
            putInt(constants.LIST_BTN_FG_KEY, getColor(R.color.default_buttons_fg))
            putInt(constants.PREVIOUS_BTN_BG_KEY, getColor(R.color.default_buttons_bg))
            putInt(constants.PREVIOUS_BTN_FG_KEY, getColor(R.color.default_buttons_fg))
            putInt(constants.NEXT_BTN_BG_KEY, getColor(R.color.default_buttons_bg))
            putInt(constants.NEXT_BTN_FG_KEY, getColor(R.color.default_buttons_fg))
            putInt(constants.STYLE_VERSION_KEY, 1)
        }.apply()
    }

    override fun onPause() {
        super.onPause()
        try{
            seekbarJob?.cancel()
        }catch (_: Exception){}

    }

    /**
     * Establishes the [Player]'s listener to be notified when player state changes
     */
    private fun setUpListener(){
        listener = object: Player.OnSongChangedListener{
            override fun listen() {
                updateUI()
            }
        }
        player.onSongChangedListener = listener
    }

    private var saverJob: Job? = null

    /**
     * Updates the UI with the [Player] state info. It also saves the [Player]'s current state
     */
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

    /**
     * Checks whether the current layout has been changed. If so, the new layout is set
     */
    private fun checkAndSetUpLayout(){
        val prefs = getSharedPreferences(GlobalPreferencesConstants.PLAY_CONTROL_ACT_PREFERENCES, Context.MODE_PRIVATE)
        var layoutSaved = prefs.getInt(GlobalPreferencesConstants.LAYOUT_KEY, -1)

        //No config
        if(layoutSaved == -1) {
            prefs.edit().apply {
                putInt(GlobalPreferencesConstants.LAYOUT_KEY, 1)
            }.apply()

            layoutSaved = 1
        }

        if(currentLayout == layoutSaved) return
        //Layout changed

        //Update layout so it matches config
        currentLayout = layoutSaved
        mustChangeStyle = true

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

    /**
     * Shows the songs of the current playlist in a bottom sheet
     */
    private fun showSongs() {
        listItems.show(supportFragmentManager, "dialog")
    }

    /**
     * Sets the variables used by the activity
     */
    private fun setUpVariablesAndButtons(){

        bgLayout = findViewById(R.id.play_control_layout)

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

        nextButton = findViewById(R.id.play_control_next_button)
        nextButton.setOnClickListener{
            player.playNext()
            playButton.foreground = AppCompatResources.getDrawable(this, R.drawable.ic_pause)
        }

        previousButton = findViewById(R.id.play_control_previous_button)
        previousButton.setOnClickListener{
            player.playPrevious()
            playButton.foreground = AppCompatResources.getDrawable(this, R.drawable.ic_pause)
        }

        seekBar = findViewById(R.id.play_control_seekbar)
        if(Player.instance.isAvailableProgress()){
            seekBar.max = Player.instance.getCurrentSongDuration().toInt()
            seekBar.progress = Player.instance.getCurrentSongProgress()
        }

        backButton = findViewById(R.id.topbar_back_button)
        backButton.setOnClickListener {
            onBackPressed()
        }

        optionsButton = findViewById(R.id.topbar_options_button)
        optionsButton.setOnClickListener {
            val popup = PopupMenu(this, it)
            popup.menuInflater.inflate(R.menu.customization_menu, popup.menu)
            popup.show()

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.playlist_change_layout -> {
                        val intent = Intent(this@PlayControlView, ChangeLayoutActivity::class.java)
                        intent.putExtra(ChangeLayoutActivity.ACTIVITY_LAYOUT_CHANGE, ChangeLayoutActivity.PLAY_CONTROL_ACTIVITY_LAYOUT)
                        startActivity(intent)
                    }

                    R.id.playlist_change_style -> {
                        val intent = Intent(this@PlayControlView, ChangeStyleActivity::class.java)
                        intent.putExtra(ChangeStyleActivity.ACTIVITY_STYLE_CHANGE, ChangeStyleActivity.PLAY_CONTROL_ACTIVITY)
                        startActivity(intent)
                    }

                }
                true
            }
        }
    }

    /**
     * When [Player]'s current song is changed, this method is invoked via listener to change the song image. Depending on the current layout setting,
     * the image will be displayed as a rounded corners square, a circle or a full-width image
     */
    private fun changeSongImg() {
        img.setImageDrawable(null)
        img.foreground = AppCompatResources.getDrawable(this, R.drawable.ic_music_note)
        img.foregroundTintList = ColorStateList.valueOf(imgDefaultColor)

        bgLayout.backgroundTintList = ColorStateList.valueOf(defaultBGColor)
        findImageJob?.cancel()
        findImageJob = CoroutineScope(Dispatchers.Default).launch {
            val currentSong = player.getCurrentSong() ?: return@launch

            val mediaRetriever = MediaMetadataRetriever()
            mediaRetriever.setDataSource(currentSong.path)

            val data = mediaRetriever.embeddedPicture
            mediaRetriever.release()

            if(data == null) return@launch

            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)

            when(currentLayout){
                ROUND_IMAGE_LAYOUT -> setRoundedBitmapToImageView(bitmap)
                else -> setBitmapToImageView(bitmap)
            }

            Palette.from(bitmap).generate{palette ->
                if(palette == null) return@generate

                currentBGColor = palette.getDarkMutedColor(defaultBGColor)

                if(currentBGColor != defaultBGColor)
                    bgLayout.backgroundTintList = ColorStateList.valueOf(currentBGColor)
                else {
                    currentBGColor = palette.getDarkVibrantColor(defaultBGColor)
                    bgLayout.backgroundTintList =
                        ColorStateList.valueOf(
                            currentBGColor
                        )
                }

                if(currentLayout == ROUND_IMAGE_LAYOUT)
                    img.foregroundTintList = ColorStateList.valueOf(currentBGColor)

            }

        }
    }

    /**
     * Establishes the image for the current song being played as circle-shaped
     * @param bitmap image to set
     */
    private suspend fun setRoundedBitmapToImageView(bitmap: Bitmap){
        val height = bitmap.height
        val width = bitmap.width
        val dim = Integer.max(height, width)
        val croppedBitmap = ThumbnailUtils.extractThumbnail(bitmap, dim, dim)
        val roundedBitmapDrawable=
            RoundedBitmapDrawableFactory.create(resources, croppedBitmap)

        roundedBitmapDrawable.isCircular = true

        withContext(Dispatchers.Main) {
            img.foreground = AppCompatResources.getDrawable(this@PlayControlView, R.drawable.circle_album_foreground)
            img.setImageDrawable(roundedBitmapDrawable)
            img.startAnimation(ImageFadeInAnimation(0f, 1f))
        }
    }

    /**
     * Establishes the image for the current song being played. The way it is displayed depends on the layout settings
     * @param bitmap image to set
     */
    private suspend fun setBitmapToImageView(bitmap: Bitmap){
        withContext(Dispatchers.Main) {
            img.foreground = null
            img.setImageBitmap(bitmap)
            img.startAnimation(ImageFadeInAnimation(0f, 1f))
        }
    }

    /**
     * Changes the foreground icon of the play mode button according to the [Player]'s state
     */
    private fun changePlayModeBtnImg() {
        if(Player.instance.isListLoop()){
            playModeBtn.foreground = AppCompatResources.getDrawable(this, R.drawable.ic_repeat_list)
        } else if(Player.instance.isSongLoop()){
            playModeBtn.foreground = AppCompatResources.getDrawable(this, R.drawable.ic_repeat_current)
        }else{
            playModeBtn.foreground = AppCompatResources.getDrawable(this, R.drawable.ic_random_mode)
        }

    }

    /**
     * Swaps the play button icon between paused and playing according to the [Player]'s state
     */
    private fun changePlayButton(){
        if(player.isPlayingSong()){
            playButton.foreground = AppCompatResources.getDrawable(this, R.drawable.ic_pause)
        }
        else{
            playButton.foreground = AppCompatResources.getDrawable(this, R.drawable.ic_play)
        }
    }

    /**
     * Changes the [Player] play mode and calls [changePlayModeBtnImg] to establish the icon of the button
     */
    private fun changePlayModeBtn(){
        player.changePlayMode()
        changePlayModeBtnImg()
        CoroutineScope(Dispatchers.Default).launch {
            PlayerStateSaver.saveState(this@PlayControlView)
        }
    }

    /**
     * Changes [Player]'s play state from playing to paused and vice versa. Then, it calls [changePlayButton] to establish the button icon
     */
    private fun playPauseBtn(){
        if(player.isPlayingSong()){
            player.pause()
        }
        else{
            player.play()
        }
        changePlayButton()
    }

    /**
     * Class that stores keys for [PlayControlView]'s [SharedPreferences]
     */
    class PreferencesConstants private constructor(){
        companion object{
            /**
             * Key that refers to Activity background color
             */
            const val GENERAL_LAYOUT_BG_KEY = "general_layout_bg_color"

            /**
             * Key that refers to Activity style version
             */
            const val STYLE_VERSION_KEY = "version"

            /**
             * Key that refers to Activity back button foreground color
             */
            const val BACK_BTN_FG_KEY = "back_button_fg_color"

            /**
             * Key that refers to Activity back button background color
             */
            const val BACK_BTN_BG_KEY = "back_button_bg_color"

            /**
             * Key that refers to Activity options button foreground color
             */
            const val OPTIONS_BTN_FG_KEY = "options_button_fg_color"

            /**
             * Key that refers to Activity options button background color
             */
            const val OPTIONS_BTN_BG_KEY = "options_button_bg_color"

            /**
             * Key that refers to Activity song info color
             */
            const val SONG_TEXT_KEY = "song_text_color"

            /**
             * Key that refers to Activity song image placeholder foreground color
             */
            const val SONG_IMAGE_FG_KEY = "song_image_fg_color"

            /**
             * Key that refers to Activity song image placeholder background color
             */
            const val SONG_IMAGE_BG_KEY = "song_image_bg_color"

            /**
             * Key that refers to Activity seek bar color
             */
            const val SEEK_BAR_KEY = "seek_bar_color"

            /**
             * Key that refers to Activity play button foreground color
             */
            const val PLAY_BTN_FG_KEY = "play_button_fg_color"

            /**
             * Key that refers to Activity play button background color
             */
            const val PLAY_BTN_BG_KEY = "play_button_bg_color"

            /**
             * Key that refers to Activity list button foreground color
             */
            const val LIST_BTN_FG_KEY = "list_button_fg_color"

            /**
             * Key that refers to Activity list button background color
             */
            const val LIST_BTN_BG_KEY = "list_button_bg_color"

            /**
             * Key that refers to Activity play mode button foreground color
             */
            const val PLAY_MODE_BTN_FG_KEY = "play_mode_button_fg_color"

            /**
             * Key that refers to Activity play mode button background color
             */
            const val PLAY_MODE_BTN_BG_KEY = "play_mode_button_bg_color"

            /**
             * Key that refers to Activity previous button foreground color
             */
            const val PREVIOUS_BTN_FG_KEY = "previous_button_fg_color"

            /**
             * Key that refers to Activity previous button background color
             */
            const val PREVIOUS_BTN_BG_KEY = "previous_button_bg_color"

            /**
             * Key that refers to Activity next button foreground color
             */
            const val NEXT_BTN_FG_KEY = "next_button_fg_color"

            /**
             * Key that refers to Activity next button background color
             */
            const val NEXT_BTN_BG_KEY = "next_button_bg_color"
        }
    }
}