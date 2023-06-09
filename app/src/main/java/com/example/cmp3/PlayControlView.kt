package com.example.cmp3

import ImageFadeInAnimation
import Player
import SongFinishedNotifier
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.palette.graphics.Palette
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayControlView : AppCompatActivity(), UpdateUI {
    private lateinit var title: TextView
    private lateinit var desc: TextView
    private lateinit var img: ImageView
    private lateinit var playModeBtn: MaterialButton
    private lateinit var playButton: MaterialButton
    private lateinit var listButton: MaterialButton
    private val player = Player.instance
    private var defaultBGColor: Int = 0

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
            updateUI()
            playButton.background = getDrawable(R.drawable.ic_pause)
        }

        findViewById<MaterialButton>(R.id.play_control_previous_button).setOnClickListener{
            player.previous()
            updateUI()
            playButton.background = getDrawable(R.drawable.ic_pause)
        }

    }

    private fun showSongs() {
        ListItemListDialogFragment().show(supportFragmentManager, "dialog")
    }

    private fun changeSongImg() {
        img.setImageResource(R.drawable.ic_music_note)
        findViewById<ConstraintLayout>(R.id.play_control_main_container).setBackgroundColor(defaultBGColor)
        CoroutineScope(Dispatchers.Default).launch {
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
                            val color = palette.getDarkMutedColor(defaultColor)
                            if(color != defaultColor)
                                findViewById<ConstraintLayout>(R.id.play_control_main_container).setBackgroundColor(color)
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

    override fun updateUISongFinished(){
        updateUI()
        playButton.background = getDrawable(R.drawable.ic_pause)
        CurrentSongAndPlaylistConfigSaver.savePlayList(this)
    }

    private fun updateUI(){
        val currentSong = player.getCurrentSong()
        title.text = currentSong?.title
        desc.text = if (currentSong?.artist == "<unknown>") "Unknown" else currentSong?.artist
        changeSongImg()
        changePlayButton()
        changePlayModeBtnImg()

    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Default).launch {
            delay(100)
            withContext(Dispatchers.Main) {
                changePlayButton()
            }
        }
        updateUI()
        SongFinishedNotifier.setCurrentActivity(this)
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
            playButton.background = getDrawable(R.drawable.ic_pause)
        }
        else{
            playButton.background = getDrawable(R.drawable.ic_play)
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