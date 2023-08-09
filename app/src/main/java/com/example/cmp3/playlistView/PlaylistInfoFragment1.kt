package com.example.cmp3.playlistView

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.animations.ImageFadeInAnimation
import com.example.cmp3.R
import com.example.songsAndPlaylists.SongList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistInfoFragment1: PlaylistInfoFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.playlist_info_fragment1, container, false)
    }

    override fun setlist(newList: SongList){
        super.setlist(newList)

        imageView.setImageResource(R.drawable.ic_music_note)
        imageView.foreground = null

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
                            imageView.setImageBitmap(bitmap)
                            imageView.startAnimation(ImageFadeInAnimation(0f, 1f))
                        }
                    }
                }
        }

    }
}