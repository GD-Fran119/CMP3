package com.example.recyclerviewAdapters

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.example.bottomSheets.SongInfoDialogFragment
import com.example.cmp3.AddSongsToPlaylistActivity
import com.example.cmp3.R
import com.example.songsAndPlaylists.Song
import java.util.Locale
import com.example.cmp3.SearchActivity
import com.example.config.GlobalPreferencesConstants
import com.google.android.material.button.MaterialButton

/**
 * Adapter used in [SearchActivity]'s [RecyclerView]
 * @param context [Activity] which creates an [PlaylistSongAdapter] instance
 * @param songs list of the songs to be displayed
 * @param fragmentManager needed when showing song info with [SongInfoDialogFragment]
 * @param viewLayoutRes resource layout which will be used to display the items of the dataset
 */
class SearchSongAdapter(private val context: Context, private val songs: List<Song>, private val fragmentManager: FragmentManager, private val viewLayoutRes: Int): RecyclerView.Adapter<SongListViewHolder>(){

    /**
     * This dataset will be modified runtime when user enters a new query
     * @see [filterDataset]
     */
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

        decorateView(view)
        return SongListViewHolder(view, context)
    }

    /**
     * Establishes the colors the view elements must have
     * @param view view to customize
     */
    private fun decorateView(view: View){
        val prefs = context.getSharedPreferences(GlobalPreferencesConstants.SEARCH_ACT_PREFERENCES, Context.MODE_PRIVATE)

        prefs.apply {
            val constants = SearchActivity.PreferencesConstants

            view.findViewById<ConstraintLayout>(R.id.song_list_item_layout).backgroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.ITEM_BG_KEY,
                    context.getColor(R.color.default_layout_bg)
                )
            )

            view.findViewById<TextView>(R.id.song_list_item_title).setTextColor(
                getInt(
                    constants.ITEM_TEXT_KEY,
                    context.getColor(R.color.default_text_color)
                )
            )

            view.findViewById<TextView>(R.id.song_list_item_album_artist).setTextColor(
                getInt(
                    constants.ITEM_TEXT_KEY,
                    context.getColor(R.color.default_text_color)
                )
            )

            val image = view.findViewById<ImageView>(R.id.song_list_item_image)
            image.backgroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.ITEM_IMG_BG_KEY,
                    context.getColor(R.color.default_image_placeholder_bg)
                )
            )
            image.foregroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.ITEM_IMG_FG_KEY,
                    context.getColor(R.color.default_image_placeholder_fg)
                )
            )

            val button = view.findViewById<MaterialButton>(R.id.song_list_item_button)
            button.backgroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.ITEM_BTN_BG_KEY,
                    context.getColor(R.color.default_buttons_bg)
                )
            )
            button.foregroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.ITEM_BTN_FG_KEY,
                    context.getColor(R.color.default_buttons_fg)
                )
            )
        }
    }

    override fun onBindViewHolder(holder: SongListViewHolder, position: Int) {
        val song = songsDataset[position]
        holder.bind(song, songs.indexOf(song).toUInt(), fragmentManager)
    }

    override fun getItemCount(): Int {
        return songsDataset.size()
    }

    /**
     * Filters the dataset by comparing the title of the songs with the [query]
     * @param query text used to filter the dataset. If [query] is an empty [String] the dataset is not filtered and returned to its original state
     */
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