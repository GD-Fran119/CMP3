package com.example.recyclerviewAdapters

import com.example.songsAndPlaylists.Song
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
import com.example.bottomSheets.SongInfoDialogFragment
import com.example.cmp3.MainActivity
import com.example.cmp3.R
import com.example.cmp3.SongListView
import com.example.config.GlobalPreferencesConstants
import com.google.android.material.button.MaterialButton

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

        decorateView(view)

        return SongListViewHolder(view, context)
    }

    /**
     * Establishes the colors the view elements must have
     * @param view view to customize
     */
    private fun decorateView(view: View){
        val prefs = context.getSharedPreferences(GlobalPreferencesConstants.MAIN_ACT_PREFERENCES, Context.MODE_PRIVATE)

        prefs.apply {
            val constants = MainActivity.PreferencesConstants

            view.findViewById<ConstraintLayout>(R.id.song_list_item_layout).backgroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.SONGS_ITEM_BG_KEY,
                    context.getColor(R.color.default_layout_bg)
                )
            )

            view.findViewById<TextView>(R.id.song_list_item_title).setTextColor(
                getInt(
                    constants.SONGS_ITEM_TEXT_KEY,
                    context.getColor(R.color.default_text_color)
                )
            )

            view.findViewById<TextView>(R.id.song_list_item_album_artist).setTextColor(
                getInt(
                    constants.SONGS_ITEM_TEXT_KEY,
                    context.getColor(R.color.default_text_color)
                )
            )

            val image = view.findViewById<ImageView>(R.id.song_list_item_image)
            image.backgroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.SONGS_ITEM_IMG_BG_KEY,
                    context.getColor(R.color.default_image_placeholder_bg)
                )
            )
            image.foregroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.SONGS_ITEM_IMG_FG_KEY,
                    context.getColor(R.color.default_image_placeholder_fg)
                )
            )

            val button = view.findViewById<MaterialButton>(R.id.song_list_item_button)
            button.backgroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.SONGS_ITEM_BTN_BG_KEY,
                    context.getColor(R.color.default_buttons_bg)
                )
            )
            button.foregroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.SONGS_ITEM_BTN_FG_KEY,
                    context.getColor(R.color.default_buttons_fg)
                )
            )
        }
    }

    override fun onBindViewHolder(holder: SongListViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song, position.toUInt(), fragmentManager)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

}