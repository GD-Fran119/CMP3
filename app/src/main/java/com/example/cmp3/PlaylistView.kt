package com.example.cmp3

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.databaseStuff.AppDatabase
import com.example.databaseStuff.SongPlaylistRelationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class PlaylistView : AppCompatActivity() {
    private lateinit var playlist: SongPlaylistRelationData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_view)

        try {
            playlist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("playlist", SongPlaylistRelationData::class.java)!!
            } else{
                intent.getParcelableExtra("playlist")!!
            }
            loadPlaylist()
        }catch (_: Exception){
            Toast.makeText(this, "Failure when retrieving song data", Toast.LENGTH_SHORT).show()
        }


    }

    private fun loadPlaylist() {

        if(playlist.songs.isNotEmpty()){
            Toast.makeText(this, playlist.songs[0].title, Toast.LENGTH_SHORT).show()
        }

    }
}