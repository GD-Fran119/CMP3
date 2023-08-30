package com.example.recyclerviewAdapters

import android.content.Context
import android.content.res.ColorStateList
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.example.cmp3.R
import com.example.cmp3.AddSongsToPlaylistActivity
import com.example.cmp3.MainActivity
import com.example.config.GlobalPreferencesConstants
import com.example.databaseStuff.AppDatabase
import com.example.databaseStuff.SongPlaylistRelation
import com.example.songsAndPlaylists.Song
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

/**
 * Adapter used in [AddSongsToPlaylistActivity]'s [RecyclerView]
 */
class AddSongAdapter private constructor(private var context: Context, private var songs: List<Song>, private val playlistID: Int, private val viewLayoutRes: Int) :
    RecyclerView.Adapter<AddSongAdapter.AddSongViewHolder>(){

    //List of songs that can be filtered
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
        /**
         * Factory method to create an [AddSongViewHolder]
         * @param c context from where the adapter will be created
         * @param id id of the playlist th which add songs
         * @param songsAddedSet initial dataset of the songs that are already added to the playlists
         * @param viewLayoutRes resource layout which will be used to display the items of the dataset
         * @return adapter
         */
        fun create(c : Context, s: List<Song>, id: Int, songsAddedSet: MutableSet<String> = mutableSetOf(), viewLayoutRes: Int): AddSongAdapter {
            //Reset songs added
            AddSongViewHolder.addedSongs = songsAddedSet
            return AddSongAdapter(c, s, id, viewLayoutRes)
        }
    }

    /**
     * [ViewHolder][RecyclerView.ViewHolder] for the [AddSongAdapter]
     */
    class AddSongViewHolder(private val view: View, private val activity: Context, private val id: Int) : RecyclerView.ViewHolder(view) {

        companion object{
            var addedSongs = mutableSetOf<String>()
        }
        private val titleText: TextView = view.findViewById(R.id.add_song_playlist_item_title)
        private val subtitleText: TextView = view.findViewById(R.id.add_song_playlist_item_desc)
        private val img : ImageView = view.findViewById(R.id.add_song_playlist_item_image)
        private lateinit var song: Song

        /**
         * Links the view elements with the [song]
         * @param song song whose info will be displayed
         */
        fun bind(song: Song) {

            this.song = song
            titleText.text = song.title
            val artist = if (song.artist == "<unknown>") "Unknown" else song.artist
            subtitleText.text = "${artist} - ${song.album}"

            val image = AppCompatResources.getDrawable(activity,
                                    if(song.path in addedSongs) R.drawable.ic_item_added_to_list
                                    else R.drawable.ic_playlist_add)
            img.foreground = image

            view.setOnClickListener {
                //Add song to playlist
                //If it is already added, notify user
                img.foreground = AppCompatResources.getDrawable(activity, R.drawable.ic_item_added_to_list)
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
            .inflate(viewLayoutRes, parent, false)

        decorateView(view)

        return AddSongViewHolder(view, context, playlistID)
    }

    /**
     * Establishes the colors the view elements must have
     * @param view view to customize
     */
    private fun decorateView(view: View){
        val prefs = context.getSharedPreferences(GlobalPreferencesConstants.ADD_SONGS_ACT_PREFERENCES, Context.MODE_PRIVATE)

        prefs.apply {
            val constants = AddSongsToPlaylistActivity.PreferencesConstants

            view.findViewById<ConstraintLayout>(R.id.add_song_playlist_item_layout).backgroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.ITEM_BG_KEY,
                    context.getColor(R.color.default_layout_bg)
                )
            )

            val textColor= getInt(
                constants.ITEM_TEXT_KEY,
                context.getColor(R.color.default_text_color)
            )
            view.findViewById<TextView>(R.id.add_song_playlist_item_title).setTextColor(textColor)
            view.findViewById<TextView>(R.id.add_song_playlist_item_desc).setTextColor(textColor)

            view.findViewById<ImageView>(R.id.add_song_playlist_item_image).foregroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.ITEM_ICON_KEY,
                    context.getColor(R.color.default_icon_color)
                )
            )
        }
    }

    override fun onBindViewHolder(holder: AddSongViewHolder, position: Int) {
        val song = songsDataset[position]
        holder.bind(song)
    }

    override fun getItemCount(): Int {
        return songsDataset.size()
    }

    /**
     * Filters the dataset by comparing the title of the songs with the [query]
     * @param query text used to filter the dataset. If [query] is an empty [String] the dataset is not filtered and returned to its original state
     */
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
