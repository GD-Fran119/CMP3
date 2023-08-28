package com.example.recyclerviewAdapters

import com.example.songsAndPlaylists.Song
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomSheets.SongInfoDialogFragment
import com.example.cmp3.SongListView

/**
 * Adapter used in [SongListView]'s [RecyclerView]
 */
open class SongArrayAdapter constructor(private var context: Context, private var songs: List<Song>, private val fragmentManager: FragmentManager, private val viewLayoutRes: Int) :
    RecyclerView.Adapter<SongListViewHolder>() {

    companion object{
        /**
         * Factory method to create a new [SongArrayAdapter]
         * @param c context from where the adapter is created
         * @param s list with the songs to display
         * @param fragmentManager needed when showing the songs info with [SongInfoDialogFragment]
         * @param viewLayoutRes resource layout which will be used to display the items of the dataset
         * @return adapter
         */
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