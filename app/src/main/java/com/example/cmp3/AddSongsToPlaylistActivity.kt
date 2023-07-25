package com.example.cmp3

import com.example.songsAndPlaylists.MainListHolder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewAdapters.AddSongAdapter

class AddSongsToPlaylistActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_songs_to_playlist)

        val songs = MainListHolder.getMainList()

        val id = intent.getIntExtra("id", -1)

        recyclerView = findViewById(R.id.add_playlist_recyclerview)

        recyclerView.adapter = AddSongAdapter.create(this, songs, id)
    }
}