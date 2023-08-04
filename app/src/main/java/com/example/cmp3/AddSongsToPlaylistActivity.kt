package com.example.cmp3

import com.example.songsAndPlaylists.MainListHolder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewAdapters.AddSongAdapter
import com.google.android.material.button.MaterialButton

class AddSongsToPlaylistActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AddSongAdapter
    private lateinit var searchBox: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_songs_to_playlist)

        val songs = MainListHolder.getMainList()

        val id = intent.getIntExtra("id", -1)

        recyclerView = findViewById(R.id.add_playlist_recyclerview)

        adapter = AddSongAdapter.create(this, songs.getList(), id)

        recyclerView.adapter = adapter

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
            Toast.makeText(this@AddSongsToPlaylistActivity, "Options selected", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.search_clear_button).setOnClickListener {
            searchBox.text.clear()
        }
    }
}