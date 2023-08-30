package com.example.recyclerviewAdapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomSheets.SongInfoDialogFragment
import com.example.cmp3.PlayControlView
import com.example.cmp3.R
import com.example.cmp3.playlistView.PlaylistView
import com.example.config.GlobalPreferencesConstants
import com.example.databaseStuff.AppDatabase
import com.example.databaseStuff.SongPlaylistRelation
import com.example.playerStuff.Player
import com.example.songsAndPlaylists.Song
import com.example.songsAndPlaylists.SongList
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Adapter used in [PlaylistView]'s [RecyclerView]
 * @param context [Activity] which creates an [PlaylistSongAdapter] instance
 * @param playlist [SongList] whose songs will be displayed
 * @param fragmentManager needed when showing song info with [SongInfoDialogFragment]
 */
class PlaylistSongAdapter(private var context: Activity, var playlist: SongList, private val fragmentManager: FragmentManager): RecyclerView.Adapter<PlaylistSongAdapter.PlaylistSongViewHolder>() {

    /**
     * Property that defines what to do when removing a song from the playlist
     */
    var onItemRemoved : OnItemRemoved? = null

    /**
     * [ViewHolder][RecyclerView.ViewHolder] for the [PlaylistSongAdapter]
     */
    class PlaylistSongViewHolder(private val view: View, private val activity: Activity) : RecyclerView.ViewHolder(view) {

        var songItemPos : Int = -1

        /**
         * Links the view elements with the [song]
         * @param song song whose info will be displayed
         * @param pos position of the song in the [PlaylistSongAdapter]
         * @param fragmentManager needed when showing song info with [SongInfoDialogFragment]
         */
        fun bind(song: Song, pos: Int, fragmentManager: FragmentManager, onRemoveItem: OnItemRemoved, playlist: SongList){

            songItemPos = pos
            view.findViewById<TextView>(R.id.playlist_item_song_title).text = song.title

            val artist = if (song.artist == "<unknown>") "Unknown" else song.artist
            view.findViewById<TextView>(R.id.playlist_item_song_desc).text = "$artist - ${song.album}"

            view.findViewById<MaterialButton>(R.id.playlist_item_song_options).setOnClickListener{

                val dialog = SongInfoDialogFragment(song)
                val action = object : SongInfoDialogFragment.SongInfoDialogAction{
                    override var actionIcon: Int = R.drawable.ic_delete

                    override var actionText: String = "Delete song from playlist"

                    override fun onAction() {

                        CoroutineScope(Dispatchers.Default).launch {
                            val dao = AppDatabase.getInstance(activity).playlistDao()
                            val relation = SongPlaylistRelation(song.path, playlist.id)
                            dao.deleteSongFromPlaylist(relation)
                        }
                        //Notify data change
                        onRemoveItem.notifyItemRemoved(pos)
                        dialog.dismiss()
                        Toast.makeText(activity, "Song deleted", Toast.LENGTH_SHORT).show()
                    }

                }

                dialog.action = action
                dialog.show(fragmentManager, "Song info")
            }

            view.setOnClickListener {
                val player = Player.instance
                player.setList(playlist)
                player.setCurrentSongAndPLay(position.toUInt())
                activity.startActivity(Intent(activity, PlayControlView::class.java))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistSongViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_playlist_view, parent, false)

        decorateView(view)

        return PlaylistSongViewHolder(view, context)
    }

    /**
     * Establishes the colors the view elements must have
     * @param view view to customize
     */
    private fun decorateView(view: View){
        val prefs = context.getSharedPreferences(GlobalPreferencesConstants.PLAYLIST_ACT_PREFERENCES, Context.MODE_PRIVATE)

        prefs.apply {
            val constants = PlaylistView.PreferencesConstants

            view.findViewById<ConstraintLayout>(R.id.playlist_item_layout).backgroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.ITEM_BG_KEY,
                    context.getColor(R.color.default_layout_bg)
                )
            )

            val textColor = getInt(
                constants.ITEM_TEXT_KEY,
                context.getColor(R.color.default_text_color)
            )
            view.findViewById<TextView>(R.id.playlist_item_song_title).setTextColor(textColor)
            view.findViewById<TextView>(R.id.playlist_item_song_desc).setTextColor(textColor)

            val button = view.findViewById<MaterialButton>(R.id.playlist_item_song_options)
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

    override fun onBindViewHolder(holder: PlaylistSongViewHolder, position: Int) {
        val song = playlist.getSong(position.toUInt())
        holder.bind(song, position, fragmentManager, onItemRemoved!!, playlist)
    }

    override fun getItemCount(): Int {
        return playlist.getListSize().toInt()
    }

    /**
     * Interface to implement when a notification of item removed is required
     */
    interface OnItemRemoved{
        /**
         * Method invoked when an item is removed from the playlist
         * @param pos position in the playlist of the item removed before deletion
         */
        fun notifyItemRemoved(pos: Int)
    }

}