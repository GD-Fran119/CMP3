package com.example.cmp3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cmp3.changeStyleFragments.SongListStyleFragment
import com.example.cmp3.changeStyleFragments.StyleFragmentBase
import com.google.android.material.button.MaterialButton

class ChangeStyleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_style)


        setUp()

    }

    private lateinit var fragment: StyleFragmentBase

    private fun setUp(){
        fragment = SongListStyleFragment(R.layout.style_fragment_main_song_list1)
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.change_style_fragment_container, fragment)
            .commit()

        findViewById<MaterialButton>(R.id.change_style_save_button).setOnClickListener{
            when(layout){
                1 -> {
                    layout = 2
                    fragment = SongListStyleFragment(R.layout.style_fragment_main_song_list2)
                    supportFragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.change_style_fragment_container, fragment)
                        .commit()
                }
                2 -> {
                    layout = 3
                    fragment = SongListStyleFragment(R.layout.style_fragment_main_song_list3)
                    supportFragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.change_style_fragment_container, fragment)
                        .commit()
                }
                3 -> {
                    layout = 1
                    fragment = SongListStyleFragment(R.layout.style_fragment_main_song_list1)
                    supportFragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.change_style_fragment_container, fragment)
                        .commit()
                }
                else -> {
                    layout = 1
                    fragment = SongListStyleFragment(R.layout.style_fragment_main_song_list1)
                    supportFragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.change_style_fragment_container, fragment)
                        .commit()
                }
            }
        }

        findViewById<MaterialButton>(R.id.change_style_cancel_button).setOnClickListener {
            onBackPressed()
        }
    }

    private var layout = 1
}