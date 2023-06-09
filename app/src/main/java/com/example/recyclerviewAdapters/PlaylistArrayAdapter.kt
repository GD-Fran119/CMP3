package com.example.recyclerviewAdapters

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.animations.ImageFadeInAnimation
import com.example.cmp3.R
import com.example.databaseStuff.SongPlaylistRelationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistArrayAdapter private constructor(private var context: Context, private var playlists: List<SongPlaylistRelationData>) :
    RecyclerView.Adapter<PlaylistArrayAdapter.PlaylistViewHolder>() {

    companion object{
        fun create(c : Context, playlists: List<SongPlaylistRelationData>): PlaylistArrayAdapter {
            return PlaylistArrayAdapter(c, playlists)
        }
    }

    class PlaylistViewHolder(private val view: View, private val activity: Context) : RecyclerView.ViewHolder(view) {

        private var job : Job? = null
        private val imageView = view.findViewById<ImageView>(R.id.playlist_icon)

        fun bind(playlist: SongPlaylistRelationData, pos: Int){
            view.findViewById<TextView>(R.id.playlist_name).text = playlist.playlist.name
            view.findViewById<TextView>(R.id.playlist_songs).text = playlist.songs.size.toString() + " songs"
            job?.cancel()
            imageView.setImageResource(R.drawable.ic_music_note)
            job = CoroutineScope(Dispatchers.Main)
                .launch {
                    val mediaRetriever = MediaMetadataRetriever()
                    val firstSong = playlist.songs[0]
                    mediaRetriever.setDataSource(firstSong.path)

                    val data = mediaRetriever.embeddedPicture
                    mediaRetriever.release()

                    if(data != null) {
                        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)

                        withContext(Dispatchers.Main) {
                            imageView.setImageBitmap(bitmap)
                            imageView.startAnimation(ImageFadeInAnimation(0f, 1f))
                        }
                    }
                }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PlaylistViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.playlists_view_item, parent, false)
        return PlaylistViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, pos: Int) {
        val playlist = playlists[pos]
        holder.bind(playlist, pos)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}