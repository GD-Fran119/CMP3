package com.example.recyclerviewAdapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomSheets.SongInfoDialogFragment
import com.example.cmp3.PlayControlView
import com.example.cmp3.R
import com.example.databaseStuff.AppDatabase
import com.example.databaseStuff.SongPlaylistRelation
import com.example.playerStuff.Player
import com.example.songsAndPlaylists.Song
import com.example.songsAndPlaylists.SongList
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistSongAdapter(private var context: Activity, var playlist: SongList, private val fragmentManager: FragmentManager): RecyclerView.Adapter<PlaylistSongAdapter.PlaylistSongViewHolder>() {

    var onItemRemoved : OnItemRemoved? = null

    class PlaylistSongViewHolder(private val view: View, private val activity: Activity) : RecyclerView.ViewHolder(view) {

        var songItemPos : Int = -1
        fun bind(song: Song, pos: Int, fragmentManager: FragmentManager, onRemoveItem: OnItemRemoved, playlist: SongList){

            songItemPos = pos
            view.findViewById<TextView>(R.id.item_song_title).text = song.title

            val artist = if (song.artist == "<unknown>") "Unknown" else song.artist
            view.findViewById<TextView>(R.id.item_song_desc).text = "$artist - ${song.album}"

            view.findViewById<MaterialButton>(R.id.item_song_options).setOnClickListener{

                val dialog = SongInfoDialogFragment(song)
                val action = object : SongInfoDialogFragment.SongInfoDialogAction{
                    override var actionIcon: Int = R.drawable.ic_delete

                    override var actionText: String = "Delete song from playlist"

                    override fun onAction() {
                        //TODO
                        //Delete song from playlist

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
        return PlaylistSongViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: PlaylistSongViewHolder, position: Int) {
        val song = playlist.getSong(position.toUInt())
        val onClickAction = View.OnClickListener {
            val player = Player.instance
            player.setList(playlist)
            player.setCurrentSongAndPLay(position.toUInt())
            context.startActivity(Intent(context, PlayControlView::class.java))
        }
        holder.bind(song, position, fragmentManager, onItemRemoved!!, playlist)
    }

    override fun getItemCount(): Int {
        return playlist.getListSize().toInt()
    }

    interface OnItemRemoved{
        fun notifyItemRemoved(pos: Int)
    }

}