package com.example.recyclerviewAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.example.cmp3.R
import com.example.databaseStuff.AppDatabase
import com.example.databaseStuff.SongPlaylistRelation
import com.example.songsAndPlaylists.Song
import com.example.songsAndPlaylists.SongList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class AddSongAdapter private constructor(private var context: Context, private var songs: List<Song>, private val playlistID: Int) :
    RecyclerView.Adapter<AddSongAdapter.AddSongViewHolder>(){

    private var songsDataset : SortedList<Song> = SortedList(Song::class.java, object: SortedList.Callback<Song>(){
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

    companion object{
        fun create(c : Context, s: List<Song>, id: Int): AddSongAdapter {
            //Reset songs added
            AddSongViewHolder.addedSongs.clear()
            return AddSongAdapter(c, s, id)
        }
    }

    class AddSongViewHolder(private val view: View, private val activity: Context, private val id: Int) : RecyclerView.ViewHolder(view) {

        companion object{
            var addedSongs = mutableSetOf<String>()
        }
        private val titleText: TextView = view.findViewById(R.id.add_song_playlist_item_title)
        private val subtitleText: TextView = view.findViewById(R.id.add_song_playlist_item_desc)
        private val img : ImageView = view.findViewById(R.id.add_song_playlist_item_image)
        private lateinit var song: Song

        fun bind(song: Song) {

            this.song = song
            titleText.text = song.title
            val artist = if (song.artist == "<unknown>") "Unknown" else song.artist
            subtitleText.text = "${artist} - ${song.album}"

            val image = if(song.path in addedSongs) R.drawable.ic_item_added_to_list
                        else R.drawable.ic_playlist_add
            img.setImageResource(image)

            view.setOnClickListener {
                //Add song to playlist
                //If it is already added, notify user
                img.setImageResource(R.drawable.ic_item_added_to_list)
                CoroutineScope(Dispatchers.Default).launch {
                    addedSongs += song.path
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
        val song = songsDataset[position]
        holder.bind(song)
    }

    override fun getItemCount(): Int {
        return songsDataset.size()
    }

    fun filterDataSet(query: String){
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
