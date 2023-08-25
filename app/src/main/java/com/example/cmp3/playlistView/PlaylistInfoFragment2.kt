package com.example.cmp3.playlistView

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
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
 * - Image of the first song in the playlist is placed on the top as a full parent-width image
 * - Playlist name, creation date and duration are displayed beneath the playlist image
 */
class PlaylistInfoFragment2 : PlaylistInfoBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.playlist_info_fragment2, container, false)
    }

    override fun setlist(newList: SongList){
        super.setlist(newList)

        imageView.setImageDrawable(null)
        imageView.foreground = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_music_note)

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
                        withContext(Dispatchers.Main){
                            imageView.foreground = null
                            imageView.setImageBitmap(bitmap)
                            imageView.startAnimation(ImageFadeInAnimation(0f, 1f))
                        }
                    }
                }
        }

    }
}