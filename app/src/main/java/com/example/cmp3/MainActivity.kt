package com.example.cmp3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

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
            Toast.makeText(this, "Song view clicked", Toast.LENGTH_SHORT).show()
        })

        viewPager.adapter = adapter


        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                val fragment = (viewPager.adapter as MainViewFragmentAdapter).createFragment(position)
                tab.text = fragment.toString()
        }.attach()

        tabLayout.addOnTabSelectedListener(this)

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        viewPager.currentItem = tab!!.position
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {}

    override fun onTabUnselected(tab: TabLayout.Tab?) {}
}