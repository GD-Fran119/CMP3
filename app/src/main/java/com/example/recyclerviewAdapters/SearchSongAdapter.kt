package com.example.recyclerviewAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.example.cmp3.R
import com.example.songsAndPlaylists.Song
import java.util.Locale

class SearchSongAdapter(private val context: Context, private val songs: List<Song>, private val fragmentManager: FragmentManager, private val viewLayoutRes: Int): RecyclerView.Adapter<SongListViewHolder>(){

    private var songsDataset : SortedList<Song> = SortedList(Song::class.java, object:SortedList.Callback<Song>(){
        override fun compare(o1: Song?, o2: Song?): Int {
            if(o1 == null && o2 == null)
                return 0
            else if(o1 == null) return -1
            else if(o2 == null) return 1

            return o1.title.lowercase(Locale.ROOT).compareTo(o2.title.lowercase(Locale.ROOT))
        }

        override fun onInserted(position: Int, count: Int) {
            notifyItemRangeInserted(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            notifyItemRangeRemoved(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int) {
            notifyItemRangeChanged(position, count)
        }

        override fun areItemsTheSame(item1: Song?, item2: Song?): Boolean {
            return item1 == item2
        }

        override fun areContentsTheSame(oldItem: Song?, newItem: Song?): Boolean {
            return oldItem == newItem
        }
    })

    init {
        songsDataset.addAll(songs)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(viewLayoutRes, parent, false)
        return SongListViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: SongListViewHolder, position: Int) {
        val song = songsDataset[position]
        holder.bind(song, songs.indexOf(song).toUInt(), fragmentManager)
    }

    override fun getItemCount(): Int {
        return songsDataset.size()
    }

    fun filterDataset(query: String){
        val filteredList = songs.filter { it.title.contains(query, true) }
        songsDataset.beginBatchedUpdates()
        try{
            songsDataset.replaceAll(filteredList)
        }
        finally{
            songsDataset.endBatchedUpdates()
        }
    }
}