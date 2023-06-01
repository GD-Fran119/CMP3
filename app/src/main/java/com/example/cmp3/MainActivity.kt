package com.example.cmp3

import ImageFinderAndSetter
import Player
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity(){

    private val adapter =  MainViewFragmentAdapter(supportFragmentManager, lifecycle)
    private lateinit var viewPager : ViewPager2
    private lateinit var playButton : MaterialButton
    private lateinit var nextButton : MaterialButton
    private lateinit var imageView: ImageView
    private val player = Player.instance

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

        supportFragmentManager.setFragmentResultListener(SongListView.resultKey, this) { _, _ ->
            setUpCurrentSongContainer()
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

    }

    override fun onStart() {
        super.onStart()

        setUpCurrentSongContainer()
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
    }

    private fun setUpCurrentSongContainer(){
        imageView.setImageResource(R.mipmap.ic_launcher)
        val currentSong = Player.instance.getCurrentSong()
        if(currentSong != null){
            findViewById<TextView>(R.id.main_current_song_title).text = currentSong.title
            findViewById<TextView>(R.id.main_current_song_desc).text = currentSong.artist

            ImageFinderAndSetter.setImage(imageView, currentSong.path)

        }
        changePlayButton()
    }
}
