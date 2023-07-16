package com.example.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.cmp3.R
import com.example.songsAndPlaylists.Song
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SongInfoDialogFragment(var song: Song):BottomSheetDialogFragment() {
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
        view.findViewById<TextView>(R.id.size_text).text = "${song.getSizeMB().toString()} MB"
        view.findViewById<TextView>(R.id.path_text).text = song.path
    }
}