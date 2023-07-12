package com.example.recyclerviewAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cmp3.R
import com.example.databaseStuff.AppDatabase
import com.example.databaseStuff.SongPlaylistRelation
import com.example.songsAndPlaylists.Song
import com.example.songsAndPlaylists.SongList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddSongAdapter private constructor(private var context: Context, private var songs: SongList, private val playlistID: Int) :
    RecyclerView.Adapter<AddSongAdapter.AddSongViewHolder>() {

    companion object{
        fun create(c : Context, s: SongList, id: Int): AddSongAdapter {
            return AddSongAdapter(c, s, id)
        }
    }

    class AddSongViewHolder(private val view: View, private val activity: Context, private val id: Int) : RecyclerView.ViewHolder(view) {

        private val titleText: TextView = view.findViewById(R.id.add_song_playlist_item_title)
        private val subtitleText: TextView = view.findViewById(R.id.add_song_playlist_item_desc)
        private val img : ImageView = view.findViewById(R.id.add_song_playlist_item_image)
        private lateinit var song: Song

        fun bind(song: Song, pos: UInt) {

            this.song = song
            titleText.text = song.title
            val artist = if (song.artist == "<unknown>") "Unknown" else song.artist
            subtitleText.text = "${artist} - ${song.album}"

            view.setOnClickListener {
                //Add song to playlist
                //If it is already added, notify user
                img.setImageResource(R.drawable.ic_item_added_to_list)
                CoroutineScope(Dispatchers.Default).launch {
                    val dao = AppDatabase.getInstance(activity).playlistDao()
                    val insertion = dao.insertSongInPlaylist(SongPlaylistRelation(song.path, id))
                    withContext(Dispatchers.Main){
                        if(insertion == -1L)
                            Toast.makeText(activity, "Song already added to playlist", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddSongViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.add_songs_playlist_item, parent, false)
        return AddSongViewHolder(view, context, playlistID)
    }

    override fun onBindViewHolder(holder: AddSongViewHolder, position: Int) {
        val song = songs.getSong(position.toUInt())
        holder.bind(song, position.toUInt())
    }

    override fun getItemCount(): Int {
        return songs.getListSize().toInt()
    }
}
