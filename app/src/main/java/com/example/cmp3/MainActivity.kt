package com.example.cmp3

import com.example.config.CurrentSongAndPlaylistConfigSaver
import ImageFinderAndSetter
import com.example.playerStuff.Player
import SongFinishedNotifier
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import MainViewFragmentAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), UpdateUI{

    private val adapter = MainViewFragmentAdapter(supportFragmentManager, lifecycle)
    private lateinit var viewPager : ViewPager2
    private lateinit var playButton : MaterialButton
    private lateinit var nextButton : MaterialButton
    private lateinit var imageView: ImageView
    private val player = Player.instance
    private lateinit var progressBar: ProgressBar
    private lateinit var progressbarJob: Job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = title

        imageView = findViewById(R.id.main_current_song_img)

        playButton = findViewById(R.id.main_current_song_pause_play)
        nextButton = findViewById(R.id.main_current_song_next_song)

        playButton.setOnClickListener{
            playPauseButtonFunction()
        }

        nextButton.setOnClickListener{
            player.next()
            setUpCurrentSongContainer()
            playButton.background = getDrawable(R.drawable.ic_pause)
        }

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        viewPager = findViewById(R.id.view_pager)
        playButton = findViewById(R.id.main_current_song_pause_play)
        nextButton = findViewById(R.id.main_current_song_next_song)

        findViewById<ConstraintLayout>(R.id.main_current_song_container).setOnClickListener {
            startActivity(Intent(this, PlayControlView::class.java))
        }

        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                val fragment = (viewPager.adapter as MainViewFragmentAdapter).createFragment(position)
                tab.text = fragment.toString()
        }.attach()

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })

        progressBar = findViewById(R.id.main_current_song_progressbar)
        if(player.isAvailableProgress()){
            progressBar.max = Player.instance.getCurrentSongDuration().toInt()
            progressBar.progress = Player.instance.getCurrentSongProgress()
        }

    }

    override fun onPause() {
        super.onPause()
        try{
            progressbarJob.cancel()
        }catch (_: Exception){}
    }
    override fun onStart() {
        super.onStart()
        SongFinishedNotifier.setCurrentActivity(this)
        setUpCurrentSongContainer()

        progressbarJob = CoroutineScope(Dispatchers.Main).launch {
            while(true){
                if(Player.instance.isAvailableProgress()){
                    progressBar.max = Player.instance.getCurrentSongDuration().toInt()
                    progressBar.progress = Player.instance.getCurrentSongProgress()
                }
                delay(500)
            }
        }

    }

    private fun changePlayButton(){
        if(player.isPlayingSong()){
            playButton.background = getDrawable(R.drawable.ic_pause)
        }
        else{
            playButton.background = getDrawable(R.drawable.ic_play)
        }
    }

    private fun playPauseButtonFunction(){
        if(player.isPlayingSong()){
            player.pause()
            changePlayButton()
        }
        else{
            player.play()
            changePlayButton()
        }

        CurrentSongAndPlaylistConfigSaver.savePlayList(this)
    }

    private fun setUpCurrentSongContainer(){
        imageView.setImageResource(R.drawable.ic_music_note)
        val currentSong = Player.instance.getCurrentSong()
        if(currentSong != null){
            findViewById<TextView>(R.id.main_current_song_title).text = currentSong.title
            findViewById<TextView>(R.id.main_current_song_desc).text = if (currentSong.artist == "<unknown>") "Unknown" else currentSong.artist

            ImageFinderAndSetter.setImage(imageView, currentSong.path)

        }
        changePlayButton()
    }

    override fun updateUISongFinished() {
        setUpCurrentSongContainer()
        playButton.background = if (Player.instance.isPlayingSong()) getDrawable(R.drawable.ic_pause) else getDrawable(R.drawable.ic_play)
        CurrentSongAndPlaylistConfigSaver.savePlayList(this)
    }

}

