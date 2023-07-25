package com.example.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.cmp3.R
import com.example.songsAndPlaylists.Song
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SongInfoDialogFragment(private val song: Song):BottomSheetDialogFragment() {

    var action: SongInfoDialogAction? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.song_info_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.title_text).text = song.title
        view.findViewById<TextView>(R.id.artist_text).text = if (song.artist == "<unknown>") "Unknown" else song.artist
        view.findViewById<TextView>(R.id.album_text).text = if (song.album == "<unknown>") "Unknown" else song.album
        view.findViewById<TextView>(R.id.duration_text).text = song.getDuration()
        view.findViewById<TextView>(R.id.size_text).text = "${song.getSizeMB()} MB"
        view.findViewById<TextView>(R.id.path_text).text = song.path

        if(action != null) {
            view.findViewById<ImageView>(R.id.add_to_playlist_icon)
                .setImageResource(action!!.actionIcon)
            view.findViewById<TextView>(R.id.add_to_playlist_text).text = action!!.actionText
            view.findViewById<ConstraintLayout>(R.id.add_to_playlist_container).setOnClickListener {
                action?.onAction()
            }
        }
        else{
            view.findViewById<ImageView>(R.id.add_to_playlist_icon)
                .setImageResource(R.drawable.ic_horizontal_line)
            view.findViewById<TextView>(R.id.add_to_playlist_text).text = "No action added"
            view.findViewById<ConstraintLayout>(R.id.add_to_playlist_container).setOnClickListener(null)
        }
    }

    interface SongInfoDialogAction{
        var actionIcon: Int
        var actionText : String
        fun onAction()
    }
}