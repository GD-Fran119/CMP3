package com.example.cmp3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cmp3.changeStyleFragments.AddSongsStyleFragment
import com.example.cmp3.changeStyleFragments.PlaylistViewStyleFragment
import com.example.cmp3.changeStyleFragments.SearchStyleFragment
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
        fragment = SearchStyleFragment(R.layout.style_fragment_search1)
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.change_style_fragment_container, fragment)
            .commit()

        findViewById<MaterialButton>(R.id.change_style_save_button).setOnClickListener{
            when(layout){
                1 -> {
                    layout = 2
                    fragment = SearchStyleFragment(R.layout.style_fragment_search2)
                    supportFragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.change_style_fragment_container, fragment)
                        .commit()
                }
                2 -> {
                    layout = 3
                    fragment = SearchStyleFragment(R.layout.style_fragment_search3)
                    supportFragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.change_style_fragment_container, fragment)
                        .commit()
                }
                3 -> {
                    layout = 1
                    fragment = SearchStyleFragment(R.layout.style_fragment_search1)
                    supportFragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.change_style_fragment_container, fragment)
                        .commit()
                }
                else -> {
                    layout = 1
                    fragment = SearchStyleFragment(R.layout.style_fragment_search1)
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