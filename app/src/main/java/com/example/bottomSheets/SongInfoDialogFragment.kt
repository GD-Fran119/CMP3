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

/**
 * Class that shows a bottom sheet with a song info. It also allows to set an action
 * to perform when action item is clicked by the user
 * @param song song whose info will be displayed
 */
class SongInfoDialogFragment(private val song: Song):BottomSheetDialogFragment() {

    /**
     * Property that defines the code to execute when using the action item
     */
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
        //Setting info
        view.findViewById<TextView>(R.id.title_text).text = song.title
        view.findViewById<TextView>(R.id.artist_text).text = if (song.artist == "<unknown>") "Unknown" else song.artist
        view.findViewById<TextView>(R.id.album_text).text = if (song.album == "<unknown>") "Unknown" else song.album
        view.findViewById<TextView>(R.id.duration_text).text = song.getDuration()
        view.findViewById<TextView>(R.id.size_text).text = "${song.getSizeMB()} MB"
        view.findViewById<TextView>(R.id.path_text).text = song.path

        //Setting action to perform
        if(action == null) {
            view.findViewById<ImageView>(R.id.add_to_playlist_icon)
                .setImageResource(R.drawable.ic_horizontal_line)
            view.findViewById<TextView>(R.id.add_to_playlist_text).text = "No action added"
            view.findViewById<ConstraintLayout>(R.id.add_to_playlist_container).setOnClickListener(null)
            return
        }

        view.findViewById<ImageView>(R.id.add_to_playlist_icon)
            .setImageResource(action!!.actionIcon)
        view.findViewById<TextView>(R.id.add_to_playlist_text).text = action!!.actionText
        view.findViewById<ConstraintLayout>(R.id.add_to_playlist_container).setOnClickListener {
            action?.onAction()
        }
    }

    /**
     * Interface to implement when the user is allowed to perform that action if they click the action item.
     * The action item is the last item displayed by the [SongInfoDialogFragment]. It is used, as said, to execute
     * some code when the user taps on this action item
     */
    interface SongInfoDialogAction{
        /**
         * Resource drawable to display
         */
        var actionIcon: Int
        /**
         * Action explanation text (E.g. "Add to playlist")
         */
        var actionText : String

        /**
         * Method to invoke when action item is clicked
         */
        fun onAction()
    }
}