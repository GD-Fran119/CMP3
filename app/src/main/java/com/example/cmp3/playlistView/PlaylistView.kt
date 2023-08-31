package com.example.cmp3.playlistView

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Guideline
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.RecyclerView
import com.example.cmp3.ChangeLayoutActivity
import com.example.cmp3.ChangeStyleActivity
import com.example.cmp3.PlayAllSongsFragment
import com.example.cmp3.R
import com.example.config.GlobalPreferencesConstants
import com.example.databaseStuff.AppDatabase
import com.example.databaseStuff.SongPlaylistRelationData
import com.example.dialogs.PlaylistRenameDialog
import com.example.recyclerviewAdapters.PlaylistSongAdapter
import com.example.songsAndPlaylists.SongList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.cmp3.CurrentSongFragment
import com.example.playerStuff.Player
import android.os.Parcelable
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton

/**
 * Activity that displays all the info of a playlist:
 * - Image of the first song contained in the playlist
 * - Playlist name, creation date and duration in a [PlaylistInfoBaseFragment]
 * - Songs are shown in a [RecyclerView]
 * - Current song that is being played in a [CurrentSongFragment]
 *
 * Requires an [Intent] with:
 * - [SongPlaylistRelationData] as [Parcelable] (E.g. intent.putExtra([INTENT_PLAYLIST_KEY], [SongPlaylistRelationData]))
 */
class PlaylistView : AppCompatActivity() {

    companion object{
        /**
         * Key to use when referring to the layout 2 this Activity supports
         */
        private const val SQUARE_IMAGE_LAYOUT = 1
        /**
         * Key to use when referring to the layout 2 this Activity supports
         */
        private const val WIDE_IMAGE_LAYOUT = 2
        /**
         * Key to use when referring to the layout 3 this Activity supports
         */
        private const val ROUND_IMAGE_LAYOUT = 3
        /**
         * Key to use when starting this activity. This key is needed to store the id in the [Intent] this activity
         * uses to operate
         */
        const val INTENT_PLAYLIST_KEY = "playlist"
    }

    //Custom layout set
    private var currentLayout = -1
    private var currentStyleVersion = -1

    private lateinit var playlist: SongPlaylistRelationData
    private lateinit var bgLayout: ConstraintLayout
    private lateinit var backButton: MaterialButton
    private lateinit var optionsButton: MaterialButton
    private lateinit var recyclerView: RecyclerView
    //Playlist info fragment
    private lateinit var fragmentContainerView: FragmentContainerView
    //Checks whether the PlayAllFragment playlist has been set
    private var playAllTextSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_info)

        bgLayout = findViewById(R.id.playlist_info_layout)
        fragmentContainerView = findViewById(R.id.playlist_info_container)

        retrievePlaylist()

        val fragment = PlayAllSongsFragment(SongList(playlist))
        supportFragmentManager.beginTransaction().setReorderingAllowed(true).replace(R.id.playlist_play_all_fragment, fragment).commit()
        setUpButtons()
        setUpRecyclerView()
    }

    override fun onStart() {
        super.onStart()

        checkAndSetUpInfoFragment()
        checkAndSetUpStyle()

        if(!playAllTextSet) {
            setUpPlayAllSongs()
            playAllTextSet = true
        }
    }

    /**
     * Checks whether the current layout has been changed. If so, the new layout is set
     */
    private fun checkAndSetUpInfoFragment(){
        val prefs = getSharedPreferences(GlobalPreferencesConstants.PLAYLIST_ACT_PREFERENCES, Context.MODE_PRIVATE)
        var layoutSaved = prefs.getInt(GlobalPreferencesConstants.LAYOUT_KEY, -1)

        //No config
        if(layoutSaved == -1) {
            prefs.edit().apply {
                putInt(GlobalPreferencesConstants.LAYOUT_KEY, 1)
            }.apply()

            layoutSaved = 1
        }

        //Layout changed
        if(currentLayout != layoutSaved){
            //Update layout so it matches config
            currentLayout = layoutSaved
            val transaction = supportFragmentManager.beginTransaction().setReorderingAllowed(true)

            when(currentLayout){
                ROUND_IMAGE_LAYOUT -> {
                    transaction.replace(R.id.playlist_info_container, PlaylistInfoFragment3())
                    findViewById<Guideline>(R.id.playlist_info_guideline).setGuidelinePercent(0.35f)
                }
                SQUARE_IMAGE_LAYOUT ->{
                    transaction.replace(R.id.playlist_info_container, PlaylistInfoFragment1())
                    findViewById<Guideline>(R.id.playlist_info_guideline).setGuidelinePercent(0.35f)
                }
                WIDE_IMAGE_LAYOUT -> {
                    transaction.replace(R.id.playlist_info_container, PlaylistInfoFragment2())
                    findViewById<Guideline>(R.id.playlist_info_guideline).setGuidelinePercent(0.45f)
                }
                else -> {//Integrity error E.g.: currentLayout == 8 (somehow ¯\_(ツ)_/¯)
                    transaction.replace(R.id.playlist_info_container, PlaylistInfoFragment1())
                    findViewById<Guideline>(R.id.playlist_info_guideline).setGuidelinePercent(0.35f)
                    currentLayout = 1
                    prefs.edit().apply {
                        putInt(GlobalPreferencesConstants.LAYOUT_KEY, 1)
                    }.apply()
                }
            }

            transaction.runOnCommit {
                loadPlaylist()
            }

            transaction.commit()

        }
    }

    /**
     * Checks whether the current style has been changed. If so, the new style is set
     */
    private fun checkAndSetUpStyle() {
        val prefs = getSharedPreferences(GlobalPreferencesConstants.PLAYLIST_ACT_PREFERENCES, MODE_PRIVATE)
        var version = prefs.getInt(PreferencesConstants.STYLE_VERSION_KEY, 0)

        if(version == 0) {
            createStylePreferences(prefs)
            version = prefs.getInt(PreferencesConstants.STYLE_VERSION_KEY, 0)
        }

        if(version == currentStyleVersion) return

        currentStyleVersion = version

        prefs.apply {
            val constants = PreferencesConstants
            bgLayout.backgroundTintList = ColorStateList.valueOf(getInt(constants.GENERAL_LAYOUT_BG_KEY, getColor(R.color.default_layout_bg)))

            backButton.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.BACK_BTN_BG_KEY, getColor(R.color.default_buttons_bg))
            )
            backButton.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.BACK_BTN_FG_KEY, getColor(R.color.default_buttons_fg))
            )
            optionsButton.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.OPTIONS_BTN_BG_KEY, getColor(R.color.default_buttons_bg))
            )
            optionsButton.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.OPTIONS_BTN_FG_KEY, getColor(R.color.default_buttons_fg))
            )

            recyclerView.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.ITEMS_CONTAINER_BG_KEY, getColor(R.color.default_layout_bg))
            )

            //TODO
            //Get pos and scroll to it
            recyclerView.adapter = recyclerView.adapter
        }
    }

    /**
     * Creates the default style for the view
     * @param preferences [SharedPreferences] where the style will be stored
     */
    private fun createStylePreferences(preferences: SharedPreferences) {
        preferences.edit().apply {
            val constants = PreferencesConstants
            putInt(constants.GENERAL_LAYOUT_BG_KEY, getColor(R.color.default_layout_bg))
            putInt(constants.BACK_BTN_BG_KEY, getColor(R.color.default_buttons_bg))
            putInt(constants.BACK_BTN_FG_KEY, getColor(R.color.default_buttons_fg))
            putInt(constants.OPTIONS_BTN_BG_KEY, getColor(R.color.default_buttons_bg))
            putInt(constants.OPTIONS_BTN_FG_KEY, getColor(R.color.default_buttons_fg))
            putInt(constants.PLAYLIST_IMG_BG_KEY, getColor(R.color.default_image_placeholder_bg))
            putInt(constants.PLAYLIST_IMG_FG_KEY, getColor(R.color.default_image_placeholder_fg))
            putInt(constants.PLAYLIST_INFO_KEY, getColor(R.color.default_text_color))
            putInt(constants.ITEMS_CONTAINER_BG_KEY, getColor(R.color.default_layout_bg))
            putInt(constants.ITEM_BG_KEY, getColor(R.color.default_layout_bg))
            putInt(constants.ITEM_TEXT_KEY, getColor(R.color.default_text_color))
            putInt(constants.ITEM_BTN_BG_KEY, getColor(R.color.default_buttons_bg))
            putInt(constants.ITEM_BTN_FG_KEY, getColor(R.color.default_buttons_fg))
            putInt(constants.STYLE_VERSION_KEY, 1)
        }.apply()
    }


    /**
     * Collects the playlist passed as [Parcelable] via intent
     */
    private fun retrievePlaylist() {
        try {
            playlist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            intent.getParcelableExtra(INTENT_PLAYLIST_KEY, SongPlaylistRelationData::class.java)!!
                        } else{
                            intent.getParcelableExtra(INTENT_PLAYLIST_KEY)!!
                        }
        }catch (_: Exception){
            Toast.makeText(this, "Failure when retrieving song data", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Establishes functionality for top bar buttons
     */
    private fun setUpButtons() {
        backButton = findViewById(R.id.topbar_back_button)
        backButton.setOnClickListener{
            onBackPressed()
        }

        optionsButton = findViewById(R.id.topbar_options_button)
        optionsButton.setOnClickListener {
            val popup = PopupMenu(this, it)
            popup.menuInflater.inflate(R.menu.playlist_menu, popup.menu)
            popup.show()

            popup.setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.playlist_change_layout -> {
                        val intent = Intent(this@PlaylistView, ChangeLayoutActivity::class.java)
                        intent.putExtra(ChangeLayoutActivity.ACTIVITY_LAYOUT_CHANGE, ChangeLayoutActivity.PLAYLIST_ACTIVITY_LAYOUT)
                        startActivity(intent)
                    }

                    R.id.playlist_change_style -> {
                        val intent = Intent(this@PlaylistView, ChangeStyleActivity::class.java)
                        intent.putExtra(ChangeStyleActivity.ACTIVITY_STYLE_CHANGE, ChangeStyleActivity.PLAYLIST_ACTIVITY)
                        startActivity(intent)
                    }

                    R.id.playlist_rename -> renamePlaylist()

                    R.id.playlist_delete -> deletePlaylist()
                }
                true
            }
        }
    }

    /**
     * Establishes the [RecyclerView] so it shows an animation when items are removed
     */
    private fun setUpRecyclerView() {
        val adapter = PlaylistSongAdapter(this, SongList(playlist), supportFragmentManager)
        adapter.onItemRemoved = object: PlaylistSongAdapter.OnItemRemoved{
            override fun notifyItemRemoved(pos: Int) {
                //Remove item from playlist
                val newList = playlist.songs.toMutableList()
                newList.removeAt(pos)
                playlist.songs = newList.toList()
                //Update info fragment
                val songList = SongList(playlist)
                findViewById<FragmentContainerView>(R.id.playlist_play_all_fragment).getFragment<PlayAllSongsFragment>()
                    .setList(songList)
                //Update list in adapter
                adapter.playlist = songList
                //Notify changes
                adapter.notifyItemRemoved(pos)
                adapter.notifyItemRangeChanged(pos, newList.size)
                loadPlaylist()
            }
        }
        recyclerView = findViewById(R.id.playlist_songs_recyclerview)
        recyclerView.adapter = adapter
    }

    /**
     * Shows a dialog requesting the new name for the playlist. Checks if input is empty and, if input is not empty,
     * updates playlist info in database and UI.
     */
    private fun renamePlaylist() {
        val dialog = PlaylistRenameDialog(playlist.playlist.id)

        val listener = object: PlaylistRenameDialog.OnConfirmListener {
            override fun notifyConfirmation() {
                //Change name
                playlist.playlist.name = dialog.getInput()
                //Update UI
                loadPlaylist()
                setUpPlayAllSongs()
            }
        }
        dialog.onConfirmationListener = listener

        dialog.show(supportFragmentManager, "Rename playlist dialog")
    }

    /**
     * Shows a confirmation dialog requesting if the user really wants to delete this [playlist].
     * If user confirms this action, the playlist is deleted from the database and [onBackPressed] is invoked.
     * 
     * This method does not check if the current playlist being played is the one that the user is deleting.
     * The user will continue listening to this playlist if it was set in the [Player]. However, the next time
     * the app is launched, and the las playlists played was the deleted one, the main list will be set as [Player]'s
     * playlist and the current song will be the one located at position 0.
     */
    private fun deletePlaylist() {

        val confirmationDialog = android.app.AlertDialog.Builder(this)
                                .setTitle("Delete playlist")
            .setMessage("Are you sure you want to delete ${playlist.playlist.name}?")
            .setIcon(R.drawable.ic_delete)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Delete"
            ) { dialog, _ ->
                CoroutineScope(Dispatchers.Default).launch {
                    val dao = AppDatabase.getInstance(this@PlaylistView).playlistDao()
                    dao.deletePlaylist(playlist.playlist.id)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@PlaylistView,
                            "${playlist.playlist.name} deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                        onBackPressed()
                    }
                }
                dialog.dismiss()
            }
            .create()

        confirmationDialog.show()
    }


    /**
     * Establishes the [PlayAllSongsFragment]'s playlist
     */
    private fun setUpPlayAllSongs(){
        val songList = SongList(playlist)
        findViewById<FragmentContainerView>(R.id.playlist_play_all_fragment).getFragment<PlayAllSongsFragment>()
            .setList(songList)
    }

    /**
     * Establishes the playlist info in the [PlaylistInfoBaseFragment]
     */
    private fun loadPlaylist() {
        fragmentContainerView.getFragment<PlaylistInfoBaseFragment>().setlist(SongList(playlist))
    }

    /**
     * Class that stores keys for [PlaylistView]'s [SharedPreferences]
     */
    class PreferencesConstants private constructor(){
        companion object{
            /**
             * Key that refers to Activity style version
             */
            const val STYLE_VERSION_KEY = "version"

            /**
             * Key that refers to Activity background color
             */
            const val GENERAL_LAYOUT_BG_KEY = "general_layout_bg_color"

            /**
             * Key that refers to Activity back button foreground color
             */
            const val BACK_BTN_FG_KEY = "back_button_fg_color"

            /**
             * Key that refers to Activity back button background color
             */
            const val BACK_BTN_BG_KEY = "back_button_bg_color"

            /**
             * Key that refers to Activity options button foreground color
             */
            const val OPTIONS_BTN_FG_KEY = "options_button_fg_color"

            /**
             * Key that refers to Activity options button background color
             */
            const val OPTIONS_BTN_BG_KEY = "options_button_bg_color"

            /**
             * Key that refers to Activity playlist image foreground color
             */
            const val PLAYLIST_IMG_FG_KEY = "playlist_image_fg_color"

            /**
             * Key that refers to Activity playlist image background color
             */
            const val PLAYLIST_IMG_BG_KEY = "playlist_image_bg_color"

            /**
             * Key that refers to Activity playlist info text color
             */
            const val PLAYLIST_INFO_KEY = "playlist_info_text_color"

            /**
             * Key that refers to Activity items container background color
             */
            const val ITEMS_CONTAINER_BG_KEY = "items_container_color"

            /**
             * Key that refers to Activity item background color
             */
            const val ITEM_BG_KEY = "item_bg_color"

            /**
             * Key that refers to Activity item text color
             */
            const val ITEM_TEXT_KEY = "item_text_color"

            /**
             * Key that refers to Activity item options button foreground color
             */
            const val ITEM_BTN_FG_KEY = "item_button_fg_color"

            /**
             * Key that refers to Activity item options button background color
             */
            const val ITEM_BTN_BG_KEY = "item_button_bg_color"
        }
    }
}