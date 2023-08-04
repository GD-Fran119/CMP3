package com.example.recyclerviewAdapters

import com.example.songsAndPlaylists.Song
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmp3.R

open class SongArrayAdapter constructor(private var context: Context, private var songs: List<Song>, private val fragmentManager: FragmentManager) :
    RecyclerView.Adapter<SongListViewHolder>() {

    companion object{
        fun create(c : Context, s: List<Song>, fragmentManager: FragmentManager): SongArrayAdapter {
            return SongArrayAdapter(c, s, fragmentManager)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListViewHolder {
        val view = LayoutInflater
            .from(parent.context)
                //TODO
                //Change song list item layout
            .inflate(R.layout.item_song_list_view2, parent, false)
        return SongListViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: SongListViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song, position.toUInt(), fragmentManager)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

}