package com.example.cmp3

import ImageFadeInAnimation
import ImageFinderAndSetter
import Player
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener{

    private val adapter =  MainViewFragmentAdapter(supportFragmentManager, lifecycle)
    private lateinit var viewPager : ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = title

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        viewPager = findViewById(R.id.view_pager)

        findViewById<ConstraintLayout>(R.id.main_current_song_container).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, PlayControlView::class.java))
        })

        viewPager.adapter = adapter


        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                val fragment = (viewPager.adapter as MainViewFragmentAdapter).createFragment(position)
                tab.text = fragment.toString()
        }.attach()

        tabLayout.addOnTabSelectedListener(this)

    }

    override fun onStart() {
        super.onStart()

        val currentSong = Player.instance.getCurrentSong()
        val imageView = findViewById<ImageView>(R.id.main_current_song_img)
        imageView.setImageResource(R.mipmap.ic_launcher)

        if(currentSong != null){
            findViewById<TextView>(R.id.main_current_song_title).text = currentSong.title
            findViewById<TextView>(R.id.main_current_song_desc).text = currentSong.artist

            ImageFinderAndSetter.setImage(imageView, currentSong.path)
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        viewPager.currentItem = tab!!.position
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {}

    override fun onTabUnselected(tab: TabLayout.Tab?) {}
}