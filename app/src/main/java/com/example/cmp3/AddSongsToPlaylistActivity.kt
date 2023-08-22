package com.example.cmp3

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.songsAndPlaylists.MainListHolder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.config.GlobalPreferencesConstants
import com.example.config.MainActivityPreferencesConstants
import com.example.recyclerviewAdapters.AddSongAdapter
import com.example.recyclerviewAdapters.SongArrayAdapter
import com.google.android.material.button.MaterialButton
import kotlin.properties.Delegates

class AddSongsToPlaylistActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AddSongAdapter
    private lateinit var searchBox: EditText
    private var id by Delegates.notNull<Int>()

    private var currentLayout = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_songs_to_playlist)

        val songs = MainListHolder.getMainList()

        id = intent.getIntExtra("id", -1)

        recyclerView = findViewById(R.id.add_playlist_recyclerview)

        searchBox = findViewById(R.id.search_text)
        searchBox.addTextChangedListener(object: TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filterDataSet(s.toString())
                recyclerView.scrollToPosition(0)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        findViewById<MaterialButton>(R.id.topbar_back_button).setOnClickListener {
            onBackPressed()
        }

        findViewById<MaterialButton>(R.id.topbar_options_button).setOnClickListener {
            val popup = PopupMenu(this, it)
            popup.menuInflater.inflate(R.menu.customization_menu, popup.menu)
            popup.show()

            popup.setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.playlist_change_layout -> {
                        val intent = Intent(this@AddSongsToPlaylistActivity, ChangeLayoutActivity::class.java)
                        intent.putExtra(ChangeLayoutActivity.ACTIVITY_LAYOUT_CHANGE, ChangeLayoutActivity.ADD_SONGS_ACTIVITY_LAYOUT)
                        startActivity(intent)
                    }

                    R.id.playlist_change_style -> {
                        val intent = Intent(this@AddSongsToPlaylistActivity, ChangeStyleActivity::class.java)
                        intent.putExtra(ChangeStyleActivity.ACTIVITY_STYLE_CHANGE, ChangeStyleActivity.ADD_SONGS_ACTIVITY)
                        startActivity(intent)
                    }

                }
                true
            }
        }

        findViewById<Button>(R.id.search_clear_button).setOnClickListener {
            searchBox.text.clear()
        }
    }

    override fun onStart() {
        super.onStart()
        checkAndSetUpLayout()
    }

    private fun checkAndSetUpLayout(){
        val prefs = getSharedPreferences(GlobalPreferencesConstants.ADD_SONGS_ACT_PREFERENCES, Context.MODE_PRIVATE)
        var savedLayout = prefs.getInt(GlobalPreferencesConstants.LAYOUT_KEY, -1)

        //No config
        if(savedLayout == -1) {
            prefs.edit().apply {
                putInt(GlobalPreferencesConstants.LAYOUT_KEY, 1)
            }.apply()

            savedLayout = 1
        }

        //Layout changed
        if(currentLayout != savedLayout){
            currentLayout = if(savedLayout !in 1..3) 1
                            else savedLayout

            prefs?.edit().apply {
                this!!.putInt(GlobalPreferencesConstants.LAYOUT_KEY, currentLayout)
            }?.apply()

            recyclerView.setHasFixedSize(true)

            when(currentLayout){
                RIGHT_ICON_LAYOUT -> {
                    adapter = AddSongAdapter.create(this, MainListHolder.getMainList().getList(), id, AddSongAdapter.AddSongViewHolder.addedSongs, R.layout.add_songs_playlist_item1)
                }
                LEFT_ICON_LAYOUT -> {
                    adapter = AddSongAdapter.create(this, MainListHolder.getMainList().getList(), id, AddSongAdapter.AddSongViewHolder.addedSongs, R.layout.add_songs_playlist_item2)
                }
                CORNER_ICON_LAYOUT -> {
                    adapter = AddSongAdapter.create(this,MainListHolder.getMainList().getList(), id, AddSongAdapter.AddSongViewHolder.addedSongs, R.layout.add_songs_playlist_item3)
                }
                else -> {
                    adapter = AddSongAdapter.create(this, MainListHolder.getMainList().getList(), id, AddSongAdapter.AddSongViewHolder.addedSongs, R.layout.add_songs_playlist_item1)
                    currentLayout = 1
                    prefs?.edit().apply {
                        this!!.putInt(GlobalPreferencesConstants.LAYOUT_KEY, currentLayout)
                    }?.apply()
                }
            }

            recyclerView.adapter = adapter
            adapter.filterDataSet(searchBox.text.toString())
        }
    }

    companion object{
        private const val RIGHT_ICON_LAYOUT = 1
        private const val LEFT_ICON_LAYOUT = 2
        private const val CORNER_ICON_LAYOUT = 3
    }
}