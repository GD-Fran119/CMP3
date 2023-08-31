package com.example.cmp3

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.config.GlobalPreferencesConstants
import com.example.playerStuff.Player
import com.example.songsAndPlaylists.SongList
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Fragment that displays how many songs have a specific playlist. If this fragment is clicked by the user,
 * the playlist is set on the [Player], a random song is selected and played and a [PlayControlView] instance is started
 */
class PlayAllSongsFragment(private var songList: SongList) : Fragment() {

    private lateinit var list: SongList
    //Style version
    private var currentStyleVersion = -1
    private lateinit var playAllText: TextView
    private lateinit var desc: TextView
    private lateinit var icon: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play_all_songs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playAllText = view.findViewById(R.id.play_all_songs_text)
        desc = view.findViewById(R.id.play_all_songs_number)
        icon = view.findViewById(R.id.play_all_songs_icon)
        setList(songList)
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
            GlobalPreferencesConstants.PLAY_ALL_FRAGMENT_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
        var version = prefs.getInt(PlayControlView.PreferencesConstants.STYLE_VERSION_KEY, 0)

        if(version == 0) {
            createStylePreferences(prefs)
            version = prefs.getInt(PlayControlView.PreferencesConstants.STYLE_VERSION_KEY, 0)
        }

        if(version == currentStyleVersion) return

        currentStyleVersion = version

        prefs.apply {
            val constants = PreferencesConstants
            val iconColor = getInt(constants.TEXT_KEY, R.color.default_play_all_icon_color)
            icon.foregroundTintList = ColorStateList.valueOf(iconColor)
            playAllText.setTextColor(iconColor)

            desc.setTextColor(getInt(constants.SONGS_KEY, R.color.default_play_all_text_color))
        }

    }

    /**
     * Creates the default style for the view
     * @param preferences [SharedPreferences] where the style will be stored
     */
    private fun createStylePreferences(preferences: SharedPreferences) {
        preferences.edit().apply {
            val constants = PreferencesConstants
            putInt(constants.TEXT_KEY, requireContext().getColor(R.color.default_play_all_icon_color))
            putInt(constants.SONGS_KEY, requireContext().getColor(R.color.default_play_all_text_color))
            putInt(constants.STYLE_VERSION_KEY, 1)
        }.apply()
    }

    /**
     * Establishes the list to hold
     * @param newList list this fragment holds
     */
    fun setList(newList: SongList){
        list = newList

        desc.text = if (list.getListSize() != 1.toUInt()) "${list.getListSize()} songs"
                    else "1 song"

        view?.findViewById<LinearLayout>(R.id.play_all_songs_container)?.setOnClickListener{
            if(list.getListSize() == 0.toUInt()) {
                Toast.makeText(activity, "There are no songs to play", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val player = Player.instance
            player.setList(list)
            player.setCurrentSongAndPLay(Random.nextUInt(list.getListSize()))
            activity?.startActivity(Intent(activity, PlayControlView::class.java))
        }
    }


    /**
     * Class that stores keys for [PlayAllSongsFragment]'s [SharedPreferences]
     */
    class PreferencesConstants private constructor(){
        companion object{
            /**
             * Key that refers to fragment style version
             */
            const val STYLE_VERSION_KEY = "version"

            /**
             * Key that refers to Fragment text and icon color
             */
            const val TEXT_KEY = "text_color"
            /**
             * Key that refers to Fragment songs number color
             */
            const val SONGS_KEY = "songs_color"
        }
    }
}