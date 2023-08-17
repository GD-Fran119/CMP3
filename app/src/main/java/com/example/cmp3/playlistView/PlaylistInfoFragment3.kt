package com.example.cmp3.playlistView

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.example.animations.ImageFadeInAnimation
import com.example.cmp3.R
import com.example.songsAndPlaylists.SongList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistInfoFragment3 : PlaylistInfoFragment() {

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

        if(list!!.isNotEmpty()) {
            CoroutineScope(Dispatchers.Main)
                .launch {
                    val mediaRetriever = MediaMetadataRetriever()
                    val firstSong = list!!.getSong(0u)
                    mediaRetriever.setDataSource(firstSong.path)

                    val data = mediaRetriever.embeddedPicture
                    mediaRetriever.release()

                    if (data != null) {
                        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                        setRoundedBitmapToImageView(bitmap)
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
            RoundedBitmapDrawableFactory.create(resources, croppedBitmap)

        roundedBitmapDrawable.isCircular = true

        withContext(Dispatchers.Main) {

            imageView.foreground = getDrawable(activity as Context, R.drawable.circle_album_foreground)
            imageView.setImageDrawable(roundedBitmapDrawable)
            imageView.startAnimation(ImageFadeInAnimation(0f, 1f))
        }
    }
}