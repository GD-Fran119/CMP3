package com.example.cmp3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import MainViewFragmentAdapter
import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Button
import android.widget.PopupMenu
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Initial Activity the user sees when launching the app. It shows tabs for Songs and playlists,
 * currents song that is being played and option buttons at the top bar.
 */
class MainActivity : AppCompatActivity(){

    private val adapter = MainViewFragmentAdapter(supportFragmentManager, lifecycle)
    private lateinit var viewPager : ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        val permission = if(Build.VERSION.SDK_INT < 33)
            Manifest.permission.READ_EXTERNAL_STORAGE
        else
            Manifest.permission.READ_MEDIA_AUDIO

        val res = checkCallingOrSelfPermission(permission)
        if(res == PackageManager.PERMISSION_GRANTED) {
            findViewById<MaterialButton>(R.id.main_search_button).setOnClickListener {
                startActivity(Intent(this, SearchActivity::class.java))
            }
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

                    R.id.playlist_change_style -> {
                        val intent = Intent(this@MainActivity, ChangeStyleActivity::class.java)
                        intent.putExtra(ChangeStyleActivity.ACTIVITY_STYLE_CHANGE, ChangeStyleActivity.MAIN_ACTIVITY)
                        when(viewPager.currentItem){
                            0 -> {
                                intent.putExtra(ChangeStyleActivity.FRAGMENT_STYLE_CHANGE, ChangeStyleActivity.SONGS_FRAGMENT)
                            }
                            1 -> {
                                intent.putExtra(ChangeStyleActivity.FRAGMENT_STYLE_CHANGE, ChangeStyleActivity.PLAYLISTS_FRAGMENT)
                            }
                            else -> {
                                intent.putExtra(ChangeStyleActivity.FRAGMENT_STYLE_CHANGE, ChangeStyleActivity.SONGS_FRAGMENT)
                            }
                        }
                        startActivity(intent)
                    }

                }
                true
            }
        }

    }

    /**
     * Class that stores keys for [MainActivity]'s [SharedPreferences]
     */
    class PreferencesConstants private constructor(){
        companion object{
            /**
             * Key that refers to main activity style version
             */
            const val MAIN_STYLE_VERSION = "version"

            /**
             * Key that refers to playlists list layout
             */
            const val PLAYLISTS_LAYOUT_KEY = "playlists_layout"

            /**
             * Key that refers to playlists list style version
             */
            const val PLAYLIST_STYLE_VERSION = "version"

            /**
             * Key that refers to songs list layout
             */
            const val SONGS_LAYOUT_KEY = "songs_layout"

            /**
             * Key that refers to songs list style version
             */
            const val SONGS_STYLE_VERSION = "version"

            /**
             * Key that refers to Activity background color
             */
            const val GENERAL_LAYOUT_BG_KEY = "general_layout_bg_color"

            /**
             * Key that refers to Activity search button foreground color
             */
            const val SEARCH_BTN_FG_KEY = "search_button_fg_color"

            /**
             * Key that refers to Activity search button background color
             */
            const val SEARCH_BTN_BG_KEY = "search_button_bg_color"

            /**
             * Key that refers to Activity options button foreground color
             */
            const val OPTIONS_BTN_FG_KEY = "options_button_fg_color"

            /**
             * Key that refers to Activity options button background color
             */
            const val OPTIONS_BTN_BG_KEY = "options_button_bg_color"

            /**
             * Key that refers to Activity tabs color
             */
            const val TAB_COLOR_KEY = "tab_color"

            /**
             * Key that refers to Activity playlist items container background color
             */
            const val PLAYLIST_ITEMS_CONTAINER_BG_KEY = "playlist_items_container_color"

            /**
             * Key that refers to Activity playlist item background color
             */
            const val PLAYLIST_ITEM_BG_KEY = "playlist_item_bg_color"

            /**
             * Key that refers to Activity playlist item image placeholder foreground color
             */
            const val PLAYLIST_ITEM_IMG_FG_KEY = "playlist_item_image_fg_color"

            /**
             * Key that refers to Activity playlist item image placeholder background color
             */
            const val PLAYLIST_ITEM_IMG_BG_KEY = "playlist_item_image_bg_color"

            /**
             * Key that refers to Activity playlist item text color
             */
            const val PLAYLIST_ITEM_TEXT_KEY = "playlist_item_text_color"

            /**
             * Key that refers to Activity playlist item icon color
             */
            const val PLAYLIST_ITEM_ICON_KEY = "item_icon_color"

            /**
             * Key that refers to Activity crete playlist button foreground color
             */
            const val CREATE_PLAYLIST_BTN_FG_KEY = "create_playlist_button_fg_color"

            /**
             * Key that refers to Activity crete playlist button background color
             */
            const val CREATE_PLAYLIST_BTN_BG_KEY = "create_playlist_button_bg_color"

            /**
             * Key that refers to Activity songs items container background color
             */
            const val SONGS_ITEMS_CONTAINER_BG_KEY = "songs_items_container_color"

            /**
             * Key that refers to Activity songs item background color
             */
            const val SONGS_ITEM_BG_KEY = "songs_item_bg_color"

            /**
             * Key that refers to Activity songs item image placeholder foreground color
             */
            const val SONGS_ITEM_IMG_FG_KEY = "songs_item_image_fg_color"

            /**
             * Key that refers to Activity songs item image placeholder background color
             */
            const val SONGS_ITEM_IMG_BG_KEY = "songs_item_image_bg_color"

            /**
             * Key that refers to Activity songs item text color
             */
            const val SONGS_ITEM_TEXT_KEY = "songs_item_text_color"

            /**
             * Key that refers to Activity songs item options button foreground color
             */
            const val SONGS_ITEM_BTN_FG_KEY = "songs_item_button_fg_color"

            /**
             * Key that refers to Activity songs item options button background color
             */
            const val SONGS_ITEM_BTN_BG_KEY = "songs_item_button_bg_color"
        }
    }
}

