package com.example.recyclerviewAdapters

import android.app.Activity
import android.content.Context
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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.RecyclerView
import com.example.animations.ImageFadeInAnimation
import com.example.cmp3.MainActivity
import com.example.cmp3.playlistView.PlaylistView
import com.example.cmp3.PlaylistListView
import com.example.cmp3.R
import com.example.config.GlobalPreferencesConstants
import com.example.databaseStuff.SongPlaylistRelationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Adapter used in [PlaylistListView]'s [RecyclerView]
 */
class PlaylistArrayAdapter private constructor(private var context: Activity, private var playlists: List<SongPlaylistRelationData>, private val viewLayoutRes: Int) :
    RecyclerView.Adapter<PlaylistArrayAdapter.PlaylistViewHolder>() {

    private var defaultImageFGColor = -1
    companion object{
        /**
         * Factory method to create an [PlaylistArrayAdapter]
         * @param c context from where the adapter will be created
         * @param playlists list of all the playlist that will be displayed
         * @param viewLayoutRes resource layout which will be used to display the items of the dataset
         * @return adapter
         */
        fun create(c : Activity, playlists: List<SongPlaylistRelationData>, viewLayoutRes: Int): PlaylistArrayAdapter {
            return PlaylistArrayAdapter(c, playlists, viewLayoutRes)
        }
    }
    init {
        context.getSharedPreferences(GlobalPreferencesConstants.MAIN_ACT_PREFERENCES, Context.MODE_PRIVATE).apply {
            defaultImageFGColor = getInt(
                MainActivity.PreferencesConstants.PLAYLIST_ITEM_IMG_FG_KEY,
                context.getColor(R.color.default_image_placeholder_fg)
            )
        }
    }

    /**
     * [ViewHolder][RecyclerView.ViewHolder] for the [PlaylistArrayAdapter]
     */
    class PlaylistViewHolder(private val view: View, private val activity: Activity, private val defaultImageFGColor: Int) : RecyclerView.ViewHolder(view) {

        private var job : Job? = null
        private val imageView = view.findViewById<ImageView>(R.id.playlists_item_image)

        /**
         * Links the view elements with the [playlist]
         * @param playlist playlist whose info will be displayed
         * @param roundedImage true if the image associated to the playlist will be displayed as circle-shaped, false otherwise
         */
        fun bind(playlist: SongPlaylistRelationData, roundedImage: Boolean){
            view.findViewById<TextView>(R.id.playlists_item_name).text = playlist.playlist.name
            view.findViewById<TextView>(R.id.playlists_item_songs).text = if(playlist.songs.size != 1) "${playlist.songs.size} songs"
                                                                    else "1 song"

            view.setOnClickListener{
                val intent = Intent(activity, PlaylistView::class.java)
                intent.putExtra(PlaylistView.INTENT_PLAYLIST_KEY, playlist)
                activity.startActivity(intent)
            }

            job?.cancel()
            imageView.setImageDrawable(null)
            imageView.foreground = AppCompatResources.getDrawable(activity, R.drawable.ic_music_note)
            imageView.foregroundTintList = ColorStateList.valueOf(defaultImageFGColor)

            if(playlist.songs.isEmpty()) return

            job = CoroutineScope(Dispatchers.Default).launch {
                val mediaRetriever = MediaMetadataRetriever()
                val firstSong = playlist.songs[0]
                mediaRetriever.setDataSource(firstSong.path)

                val data = mediaRetriever.embeddedPicture
                mediaRetriever.release()

                if (data == null) return@launch

                val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                if(roundedImage)
                    setRoundedBitmapToImageView(bitmap)
                else
                    setBitmapToImageView(bitmap)
            }
        }

        /**
         * Establishes the image for the current playlist being displayed as circle-shaped
         * @param bitmap image to set
         */
        private suspend fun setRoundedBitmapToImageView(bitmap: Bitmap){
            val height = bitmap.height
            val width = bitmap.width
            val dim = Integer.min(height, width)

            val croppedBitmap = ThumbnailUtils.extractThumbnail(bitmap, dim, dim)
            val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(activity.resources, croppedBitmap)
            roundedBitmapDrawable.isCircular = true

            withContext(Dispatchers.Main) {
                imageView.foreground = AppCompatResources.getDrawable(activity, R.drawable.circle_album_foreground)
                imageView.foregroundTintList = view.findViewById<ConstraintLayout>(R.id.playlists_item_layout).backgroundTintList
                imageView.setImageDrawable(roundedBitmapDrawable)
                imageView.startAnimation(ImageFadeInAnimation(0f, 1f))
            }
        }

        /**
         * Establishes the image for the current playlist being displayed. The way it is displayed depends on the layout settings
         * @param bitmap image to set
         */
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

        decorateView(view)

        return PlaylistViewHolder(view, context, defaultImageFGColor)
    }

    /**
     * Establishes the colors the view elements must have
     * @param view view to customize
     */
    private fun decorateView(view: View){
        val prefs = context.getSharedPreferences(GlobalPreferencesConstants.MAIN_ACT_PREFERENCES, Context.MODE_PRIVATE)

        prefs.apply {
            val constants = MainActivity.PreferencesConstants

            view.findViewById<ConstraintLayout>(R.id.playlists_item_layout).backgroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.PLAYLIST_ITEM_BG_KEY,
                    context.getColor(R.color.default_layout_bg)
                )
            )

            val image = view.findViewById<ImageView>(R.id.playlists_item_image)
            image.backgroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.PLAYLIST_ITEM_IMG_BG_KEY,
                    context.getColor(R.color.default_image_placeholder_bg)
                )
            )
            image.foregroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.PLAYLIST_ITEM_IMG_FG_KEY,
                    context.getColor(R.color.default_image_placeholder_fg)
                )
            )

            val textColor= getInt(
                constants.PLAYLIST_ITEM_TEXT_KEY,
                context.getColor(R.color.default_text_color)
            )
            view.findViewById<TextView>(R.id.playlists_item_name).setTextColor(textColor)
            view.findViewById<TextView>(R.id.playlists_item_songs).setTextColor(textColor)

            view.findViewById<ImageView>(R.id.playlists_item_button)?.foregroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.PLAYLIST_ITEM_ICON_KEY,
                    context.getColor(R.color.default_icon_color)
                )
            )
        }
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, pos: Int) {
        val playlist = playlists[pos]
        holder.bind(playlist, viewLayoutRes == R.layout.playlists_item_view3)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}