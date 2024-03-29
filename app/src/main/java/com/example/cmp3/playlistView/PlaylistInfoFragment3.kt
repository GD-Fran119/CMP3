package com.example.cmp3.playlistView

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.example.animations.ImageFadeInAnimation
import com.example.cmp3.R
import com.example.songsAndPlaylists.SongList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Fragment class to display a playlist's info.
 * The info is displayed as follows:
 * - Image of the first song in the playlist is placed on the right as a circle
 * - Playlist name, creation date and duration are displayed on the left, one on top of each other
 */
class PlaylistInfoFragment3 : PlaylistInfoBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.playlist_info_fragment3, container, false)
    }

    override fun setlist(newList: SongList){
        super.setlist(newList)

        imageView.setImageDrawable(null)
        imageView.foreground = getDrawable(requireContext(), R.drawable.ic_music_note)
        imageView.foregroundTintList = ColorStateList.valueOf(imgForegroundDefaultColor)

        if(list!!.isEmpty()) return

        CoroutineScope(Dispatchers.Main).launch {
            val mediaRetriever = MediaMetadataRetriever()
            val firstSong = list!!.getSong(0u)
            mediaRetriever.setDataSource(firstSong.path)

            val data = mediaRetriever.embeddedPicture
            mediaRetriever.release()

            if (data == null) return@launch

            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            setRoundedBitmapToImageView(bitmap)

        }
    }

    /**
     * Establishes the playlist image as circle-shaped
     * @param bitmap image to display
     */
    private suspend fun setRoundedBitmapToImageView(bitmap: Bitmap){
        val height = bitmap.height
        val width = bitmap.width
        val dim = Integer.min(height, width)

        //Square-ize bitmap
        val croppedBitmap = ThumbnailUtils.extractThumbnail(bitmap, dim, dim)
        val roundedBitmapDrawable=
            RoundedBitmapDrawableFactory.create(resources, croppedBitmap)
        roundedBitmapDrawable.isCircular = true

        withContext(Dispatchers.Main) {
            imageView.foreground = getDrawable(activity as Context, R.drawable.circle_album_foreground)
            imageView.foregroundTintList = ColorStateList.valueOf(imgForegroundColorWhenImageSet)
            imageView.setImageDrawable(roundedBitmapDrawable)
            imageView.startAnimation(ImageFadeInAnimation(0f, 1f))
        }
    }
}