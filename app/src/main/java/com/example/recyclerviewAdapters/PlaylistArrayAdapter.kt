package com.example.recyclerviewAdapters

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.RecyclerView
import com.example.animations.ImageFadeInAnimation
import com.example.cmp3.playlistView.PlaylistView
import com.example.cmp3.R
import com.example.databaseStuff.SongPlaylistRelationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistArrayAdapter private constructor(private var context: Activity, private var playlists: List<SongPlaylistRelationData>, private val viewLayoutRes: Int) :
    RecyclerView.Adapter<PlaylistArrayAdapter.PlaylistViewHolder>() {

    companion object{
        fun create(c : Activity, playlists: List<SongPlaylistRelationData>, viewLayoutRes: Int): PlaylistArrayAdapter {
            return PlaylistArrayAdapter(c, playlists, viewLayoutRes)
        }
    }

    class PlaylistViewHolder(private val view: View, private val activity: Activity) : RecyclerView.ViewHolder(view) {

        private var job : Job? = null
        private val imageView = view.findViewById<ImageView>(R.id.playlist_icon)

        fun bind(playlist: SongPlaylistRelationData, roundedImage: Boolean){
            view.findViewById<TextView>(R.id.playlist_name).text = playlist.playlist.name
            view.findViewById<TextView>(R.id.playlist_songs).text = if(playlist.songs.size != 1) "${playlist.songs.size} songs"
                                                                    else "1 song"

            view.setOnClickListener{
                val intent = Intent(activity, PlaylistView::class.java)
                intent.putExtra(PlaylistView.INTENT_PLAYLIST_KEY, playlist)
                activity.startActivity(intent)
            }

            job?.cancel()
            imageView.setImageDrawable(null)
            imageView.foreground = getDrawable(activity, R.drawable.ic_music_note)
            //TODO
            //Change with default foreground color
            imageView.foregroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            if(playlist.songs.isNotEmpty()) {
                job = CoroutineScope(Dispatchers.Default)
                    .launch {
                        val mediaRetriever = MediaMetadataRetriever()
                        val firstSong = playlist.songs[0]
                        mediaRetriever.setDataSource(firstSong.path)

                        val data = mediaRetriever.embeddedPicture
                        mediaRetriever.release()

                        if (data != null) {
                            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                            if(roundedImage)
                                setRoundedBitmapToImageView(bitmap)
                            else
                                setBitmapToImageView(bitmap)
                        }
                    }
            }
        }
        private suspend fun setRoundedBitmapToImageView(bitmap: Bitmap){
            val height = bitmap.height
            val width = bitmap.width
            val dim = Integer.max(height, width)
            val croppedBitmap = ThumbnailUtils.extractThumbnail(bitmap, dim, dim)
            val roundedBitmapDrawable=
                RoundedBitmapDrawableFactory.create(activity.resources, croppedBitmap)

            roundedBitmapDrawable.isCircular = true

            withContext(Dispatchers.Main) {

                imageView.foreground = getDrawable(activity, R.drawable.circle_album_foreground)
                imageView.foregroundTintList = view.findViewById<ConstraintLayout>(R.id.playlists_item_layout).backgroundTintList
                imageView.setImageDrawable(roundedBitmapDrawable)
                imageView.startAnimation(ImageFadeInAnimation(0f, 1f))
            }
        }

        private suspend fun setBitmapToImageView(bitmap: Bitmap){
            withContext(Dispatchers.Main) {
                imageView.foreground = null
                imageView.setImageBitmap(bitmap)
                imageView.startAnimation(ImageFadeInAnimation(0f, 1f))
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PlaylistViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(viewLayoutRes, parent, false)
        return PlaylistViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, pos: Int) {
        val playlist = playlists[pos]
        holder.bind(playlist, viewLayoutRes == R.layout.playlists_item_view3)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}