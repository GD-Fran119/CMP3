package com.example.cmp3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import MainViewFragmentAdapter
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(){

    private val adapter = MainViewFragmentAdapter(supportFragmentManager, lifecycle)
    private lateinit var viewPager : ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = title

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        viewPager = findViewById(R.id.view_pager)
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

        findViewById<MaterialButton>(R.id.main_search_button).setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        findViewById<Button>(R.id.main_options_button).setOnClickListener {

            val popup = PopupMenu(this, it)
            popup.menuInflater.inflate(R.menu.customization_menu, popup.menu)
            popup.show()

            popup.setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.playlist_change_layout -> {
                        val intent = Intent(this@MainActivity, ChangeLayoutActivity::class.java)
                        intent.putExtra(ChangeLayoutActivity.ACTIVITY_LAYOUT_CHANGE, ChangeLayoutActivity.MAIN_ACTIVITY_LAYOUT)
                        when(viewPager.currentItem){
                            0 -> {
                                intent.putExtra(ChangeLayoutActivity.FRAGMENT_LAYOUT_CHANGE, ChangeLayoutActivity.SONGS_FRAGMENT_LAYOUT)
                            }
                            1 -> {
                                intent.putExtra(ChangeLayoutActivity.FRAGMENT_LAYOUT_CHANGE, ChangeLayoutActivity.PLAYLISTS_FRAGMENT_LAYOUT)
                            }
                            else -> {
                                intent.putExtra(ChangeLayoutActivity.FRAGMENT_LAYOUT_CHANGE, ChangeLayoutActivity.SONGS_FRAGMENT_LAYOUT)
                            }
                        }
                        startActivity(intent)
                    }

                    R.id.playlist_change_style -> Toast.makeText(
                        this@MainActivity,
                        "Change style",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                true
            }
        }

    }
}

