package com.example.recyclerviewAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cmp3.R
import com.example.databaseStuff.AppDatabase
import com.example.databaseStuff.PlaylistEntity
import com.example.databaseStuff.SongPlaylistRelation
import com.example.songsAndPlaylists.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.zip.Inflater

class PlaylistBottomSheetAdapter(private val playlists: List<PlaylistEntity>, private val song: Song, private val onSongAdded: OnAddToPlaylistListener):
    RecyclerView.Adapter<PlaylistBottomSheetAdapter.ViewHolder>() {
    class ViewHolder(private val view: View, private val onSongAdded: OnAddToPlaylistListener) : RecyclerView.ViewHolder(view){
        fun bind(playlist: PlaylistEntity, song: Song){
            view.findViewById<TextView>(R.id.playlist_name_bottom_box).text = playlist.name
            view.setOnClickListener {
                onSongAdded.onSongAdded(song.path, playlist.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.add_song_botomsheet_item_layout, parent, false)

        return ViewHolder(view, onSongAdded)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(playlists[position], song)
    }

    interface OnAddToPlaylistListener{
        fun onSongAdded(path: String, id: Int)
    }
}