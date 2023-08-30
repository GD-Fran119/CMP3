package com.example.cmp3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import com.example.songsAndPlaylists.MainListHolder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.config.GlobalPreferencesConstants
import com.example.recyclerviewAdapters.AddSongAdapter
import com.google.android.material.button.MaterialButton
import kotlin.properties.Delegates

/**
 * Activity to add songs to a specific playlist. Requires an [Intent] with:
 * - Playlist [ID][Int]
 */
class AddSongsToPlaylistActivity : AppCompatActivity() {
    private lateinit var bgLayout: ConstraintLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AddSongAdapter
    private lateinit var searchBox: EditText
    private lateinit var backButton: MaterialButton
    private lateinit var optionsButton: MaterialButton
    private lateinit var clearButton: Button
    //ID of the playlist to which add songs
    private var id by Delegates.notNull<Int>()

    //Custom layout set
    private var currentLayout = -1
    private var currentStyleVersion = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_songs_to_playlist)

        //Retrieve playlist id
        id = intent.getIntExtra(PLAYLIST_ID_KEY, -1)

        bgLayout = findViewById(R.id.add_song_playlist_layout)

        recyclerView = findViewById(R.id.add_playlist_recyclerview)

        searchBox = findViewById(R.id.search_text)
        searchBox.addTextChangedListener(object: TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //When new text is typed, the adapter filters its dataset so it only displays
                //the text that matches the text writen by the user
                adapter.filterDataSet(s.toString())
                recyclerView.scrollToPosition(0)
            }

            //Not used
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            //Not used
            override fun afterTextChanged(s: Editable?) {}
        })

        backButton = findViewById(R.id.topbar_back_button)
        backButton.setOnClickListener {
            onBackPressed()
        }

        optionsButton = findViewById(R.id.topbar_options_button)
        optionsButton.setOnClickListener {
            val popup = PopupMenu(this, it)
            popup.menuInflater.inflate(R.menu.customization_menu, popup.menu)
            popup.show()

            popup.setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.playlist_change_layout -> {
                        val intent = Intent(this@AddSongsToPlaylistActivity, ChangeLayoutActivity::class.java)
                        intent.putExtra(ChangeLayoutActivity.ACTIVITY_LAYOUT_CHANGE, ChangeLayoutActivity.ADD_SONGS_ACTIVITY_LAYOUT)
                        startActivity(intent)
                    }

                    R.id.playlist_change_style -> {
                        val intent = Intent(this@AddSongsToPlaylistActivity, ChangeStyleActivity::class.java)
                        intent.putExtra(ChangeStyleActivity.ACTIVITY_STYLE_CHANGE, ChangeStyleActivity.ADD_SONGS_ACTIVITY)
                        startActivity(intent)
                    }

                }
                true
            }
        }

        clearButton = findViewById(R.id.search_clear_button)
        clearButton.setOnClickListener {
            searchBox.text.clear()
        }
    }

    override fun onStart() {
        super.onStart()

        checkAndSetUpLayout()
        checkAndSetUpStyle()
    }

    /**
     * Checks whether the current layout has been changed. If so, the new layout is set
     */
    private fun checkAndSetUpLayout(){
        val prefs = getSharedPreferences(GlobalPreferencesConstants.ADD_SONGS_ACT_PREFERENCES, Context.MODE_PRIVATE)
        var savedLayout = prefs.getInt(GlobalPreferencesConstants.LAYOUT_KEY, -1)

        //No config
        if(savedLayout == -1) {
            prefs.edit().apply {
                putInt(GlobalPreferencesConstants.LAYOUT_KEY, 1)
            }.apply()

            savedLayout = 1
        }

        //Layout changed
        if(currentLayout == savedLayout) return

        currentLayout = if(savedLayout !in 1..3) 1
                        else savedLayout

        prefs?.edit().apply {
            this!!.putInt(GlobalPreferencesConstants.LAYOUT_KEY, currentLayout)
        }?.apply()

        recyclerView.setHasFixedSize(true)

        when(currentLayout){
            RIGHT_ICON_LAYOUT -> {
                adapter = AddSongAdapter.create(this, MainListHolder.getMainList().getList(), id, AddSongAdapter.AddSongViewHolder.addedSongs, R.layout.add_songs_playlist_item1)
            }
            LEFT_ICON_LAYOUT -> {
                adapter = AddSongAdapter.create(this, MainListHolder.getMainList().getList(), id, AddSongAdapter.AddSongViewHolder.addedSongs, R.layout.add_songs_playlist_item2)
            }
            CORNER_ICON_LAYOUT -> {
                adapter = AddSongAdapter.create(this,MainListHolder.getMainList().getList(), id, AddSongAdapter.AddSongViewHolder.addedSongs, R.layout.add_songs_playlist_item3)
            }
            else -> {
                adapter = AddSongAdapter.create(this, MainListHolder.getMainList().getList(), id, AddSongAdapter.AddSongViewHolder.addedSongs, R.layout.add_songs_playlist_item1)
                currentLayout = 1
                prefs?.edit().apply {
                    this!!.putInt(GlobalPreferencesConstants.LAYOUT_KEY, currentLayout)
                }?.apply()
            }
        }

        recyclerView.adapter = adapter
        adapter.filterDataSet(searchBox.text.toString())

    }

    /**
     * Checks whether the current style has been changed. If so, the new style is set
     */
    private fun checkAndSetUpStyle() {
        val prefs = getSharedPreferences(GlobalPreferencesConstants.ADD_SONGS_ACT_PREFERENCES, MODE_PRIVATE)
        var version = prefs.getInt(PreferencesConstants.STYLE_VERSION_KEY, 0)

        if(version == 0) {
            createStylePreferences(prefs)
            version = prefs.getInt(PreferencesConstants.STYLE_VERSION_KEY, 0)
        }

        if(version == currentStyleVersion) return

        currentStyleVersion = version

        prefs.apply {
            val constants = PreferencesConstants
            bgLayout.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.GENERAL_LAYOUT_BG_KEY, getColor(R.color.default_layout_bg))
            )

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

            val searchTextColor = getInt(constants.SEARCH_BOX_KEY, getColor(R.color.default_search_box_text_color))
            searchBox.setTextColor(searchTextColor)
            searchBox.setHintTextColor(ColorStateList.valueOf(searchTextColor).withAlpha(0x80).defaultColor)

            clearButton.setTextColor(getInt(constants.CLEAR_BTN_KEY, getColor(R.color.default_search_clear_button_color)))

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
            putInt(constants.SEARCH_BOX_KEY, getColor(R.color.default_search_box_text_color))
            putInt(constants.CLEAR_BTN_KEY, getColor(R.color.default_search_clear_button_color))
            putInt(constants.ITEMS_CONTAINER_BG_KEY, getColor(R.color.default_layout_bg))
            putInt(constants.ITEM_BG_KEY, getColor(R.color.default_layout_bg))
            putInt(constants.ITEM_ICON_KEY, getColor(R.color.default_icon_color))
            putInt(constants.ITEM_TEXT_KEY, getColor(R.color.default_text_color))
            putInt(constants.STYLE_VERSION_KEY, 1)
        }.apply()
    }

    companion object{
        /**
         * Key to use when referring to the layout 1 this Activity supports
         */
        private const val RIGHT_ICON_LAYOUT = 1
        /**
         * Key to use when referring to the layout 2 this Activity supports
         */
        private const val LEFT_ICON_LAYOUT = 2
        /**
         * Key to use when referring to the layout 3 this Activity supports
         */
        private const val CORNER_ICON_LAYOUT = 3
        /**
         * Key to use when starting this activity. This key is needed to store the id in the [Intent] this activity
         * uses to operate
         */
        const val PLAYLIST_ID_KEY = "id"
    }

    /**
     * Class that stores keys for [AddSongsToPlaylistActivity]'s [SharedPreferences]
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
             * Key that refers to Activity search box text color
             */
            const val SEARCH_BOX_KEY = "search_box_text_color"

            /**
             * Key that refers to Activity clear button color
             */
            const val CLEAR_BTN_KEY = "clear_button_color"

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
             * Key that refers to Activity item icon color
             */
            const val ITEM_ICON_KEY = "item_icon_color"
        }
    }
}