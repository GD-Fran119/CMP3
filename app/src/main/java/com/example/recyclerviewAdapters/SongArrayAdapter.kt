package com.example.recyclerviewAdapters

import com.example.songsAndPlaylists.Song
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmp3.R

open class SongArrayAdapter constructor(private var context: Context, private var songs: List<Song>, private val fragmentManager: FragmentManager, private val viewLayoutRes: Int) :
    RecyclerView.Adapter<SongListViewHolder>() {

    companion object{
        fun create(c : Context, s: List<Song>, fragmentManager: FragmentManager, viewLayoutRes: Int): SongArrayAdapter {
            return SongArrayAdapter(c, s, fragmentManager, viewLayoutRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(viewLayoutRes, parent, false)
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