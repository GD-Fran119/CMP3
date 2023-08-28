package com.example.recyclerviewAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cmp3.R
import com.example.cmp3.SongListView
import com.example.databaseStuff.PlaylistEntity
import com.example.songsAndPlaylists.Song

/**
 * Adapter used when adding a song to a custom playlist in [SongListView]'s [RecyclerView]
 */
class PlaylistBottomSheetAdapter(private val playlists: List<PlaylistEntity>, private val song: Song, private val onSongAdded: OnAddToPlaylistListener):
    RecyclerView.Adapter<PlaylistBottomSheetAdapter.ViewHolder>() {
    /**
     * [ViewHolder][RecyclerView.ViewHolder] for the [PlaylistBottomSheetAdapter]
     */
    class ViewHolder(private val view: View, private val onSongAdded: OnAddToPlaylistListener) : RecyclerView.ViewHolder(view){
        /**
         * Links the view elements with the [playlist] info
         * @param playlist playlist whose info will be displayed
         * @param song song which will be added to the playlist if the user chooses it
         */
        fun bind(playlist: PlaylistEntity, song: Song){
            view.findViewById<TextView>(R.id.playlist_name_bottom_box).text = playlist.name
            view.setOnClickListener {
                onSongAdded.onSongAdded(song.path, playlist.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.add_song_botomsheet_item_layout, parent, false)

        return ViewHolder(view, onSongAdded)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(playlists[position], song)
    }

    /**
     * Interface to implement to manage how the selected song is added to the chosen playlist
     */
    interface OnAddToPlaylistListener{
        /**
         * Method invoked when song is going to be added to a playlist
         * @param path file path of the song which is going to be added
         * @param id id of the playlist to which the song is going to be added
         */
        fun onSongAdded(path: String, id: Int)
    }
}