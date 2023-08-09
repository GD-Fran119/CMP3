package com.example.cmp3.playlistView

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.cmp3.R
import com.example.songsAndPlaylists.SongList

abstract class PlaylistInfoFragment: Fragment() {

    protected lateinit var title: TextView
    protected lateinit var duration: TextView
    protected lateinit var imageView: ImageView
    protected var list: SongList? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = view.findViewById(R.id.playlist_name)
        title.isSelected = true
        duration = view.findViewById(R.id.playlist_duration)
        imageView = view.findViewById(R.id.playlist_image)
    }

    open fun setlist(newList: SongList){
        list = newList
        title.text = list!!.title
        duration.text = list!!.getDuration()
    }
}