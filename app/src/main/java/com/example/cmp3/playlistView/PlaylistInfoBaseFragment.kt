package com.example.cmp3.playlistView

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cmp3.R
import com.example.config.GlobalPreferencesConstants
import com.example.songsAndPlaylists.SongList

/**
 * Base fragment class to display a playlist's info
 */
abstract class PlaylistInfoBaseFragment: Fragment() {

    protected lateinit var title: TextView
    protected lateinit var date: TextView
    protected lateinit var duration: TextView
    protected lateinit var imageView: ImageView
    protected lateinit var iconView: ImageView
    protected var list: SongList? = null

    /**
     * Foreground color when displaying placeholder
     */
    protected var imgForegroundDefaultColor = -1
    /**
     * Foreground color when displaying custom image
     */
    protected var imgForegroundColorWhenImageSet = -1
    private var currentStyleVersion = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Setting up variables
        title = view.findViewById(R.id.playlists_item_name)
        title.isSelected = true
        date = view.findViewById(R.id.playlist_date)
        duration = view.findViewById(R.id.playlist_duration)
        imageView = view.findViewById(R.id.playlist_image)
        iconView = view.findViewById(R.id.playlist_duration_icon)
    }

    override fun onStart() {
        super.onStart()

        checkAndSetUpStyle()
    }

    /**
     * Checks whether the current style has been changed. If so, the new style is set
     */
    private fun checkAndSetUpStyle() {
        val prefs = requireContext().getSharedPreferences(
            GlobalPreferencesConstants.PLAYLIST_ACT_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
        val version = prefs.getInt(PlaylistView.PreferencesConstants.STYLE_VERSION_KEY, 0)

        if(version == currentStyleVersion) return

        currentStyleVersion = version

        prefs.apply {
            val constants = PlaylistView.PreferencesConstants

            val textColor = getInt(constants.PLAYLIST_INFO_KEY, requireContext().getColor(R.color.default_text_color))
            title.setTextColor(textColor)
            date.setTextColor(textColor)
            duration.setTextColor(textColor)
            iconView.foregroundTintList = ColorStateList.valueOf(textColor)

            imageView.backgroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.PLAYLIST_IMG_BG_KEY,
                    requireContext().getColor(R.color.default_image_placeholder_bg)
                )
            )
            imgForegroundDefaultColor = getInt(
                constants.PLAYLIST_IMG_FG_KEY,
                requireContext().getColor(R.color.default_image_placeholder_fg)
            )
            imgForegroundColorWhenImageSet = getInt(
                constants.GENERAL_LAYOUT_BG_KEY,
                requireContext().getColor(R.color.default_layout_bg)
            )
            imageView.foregroundTintList = ColorStateList.valueOf(imgForegroundDefaultColor)

            //If playlist has image
            //Change foregroundTint so it seems it is a disc
            if(imageView.drawable != null)
                imageView.foregroundTintList = ColorStateList.valueOf(imgForegroundColorWhenImageSet)

        }
    }

    /**
     * Sets the playlist whose info is gonna be displayed
     * @param newList list whose info will be displayed
     */
    open fun setlist(newList: SongList){
        list = newList
        title.text = list!!.title
        date.text = "Created: ${list!!.creationDate}"
        duration.text = list!!.getDuration()
    }
}