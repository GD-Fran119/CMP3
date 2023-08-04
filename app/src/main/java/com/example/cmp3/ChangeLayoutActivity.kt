package com.example.cmp3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlin.properties.Delegates


class ChangeLayoutActivity : AppCompatActivity() {
    companion object{
        /**
         * Name for SharedPreferences that store layout prefs:
         * - [MAIN_ACT_PREFERENCES]
         * - [PLAYLIST_ACT_PREFERENCES]
         * - [SEARCH_ACT_PREFERENCES]
         * - [PLAY_CONTROL_ACT_PREFERENCES]
         */
        private const val MAIN_ACT_PREFERENCES = "main_layout_prefs"
        /**
         * Name for SharedPreferences that store layout prefs:
         * - [MAIN_ACT_PREFERENCES]
         * - [PLAYLIST_ACT_PREFERENCES]
         * - [SEARCH_ACT_PREFERENCES]
         * - [PLAY_CONTROL_ACT_PREFERENCES]
         */
        private const val PLAYLIST_ACT_PREFERENCES = "playlist_layout_prefs"
        /**
         * Name for SharedPreferences that store layout prefs:
         * - [MAIN_ACT_PREFERENCES]
         * - [PLAYLIST_ACT_PREFERENCES]
         * - [SEARCH_ACT_PREFERENCES]
         * - [PLAY_CONTROL_ACT_PREFERENCES]
         */
        private const val SEARCH_ACT_PREFERENCES = "search_layout_prefs"
        /**
         * Name for SharedPreferences that store layout prefs:
         * - [MAIN_ACT_PREFERENCES]
         * - [PLAYLIST_ACT_PREFERENCES]
         * - [SEARCH_ACT_PREFERENCES]
         * - [PLAY_CONTROL_ACT_PREFERENCES]
         */
        private const val PLAY_CONTROL_ACT_PREFERENCES = "play_control_layout_prefs"

        /**
         * Constant to refer to Activity parameter in the [Intent] received by this Activity
         */
        const val ACTIVITY_LAYOUT_CHANGE = "activity"

        /**
         * Constant to refer to Activity parameter in the [Intent] received by this Activity
         */
        const val FRAGMENT_LAYOUT_CHANGE = "fragment"

        /**
         * Constant to refer to Main Activity
         */
        const val MAIN_ACTIVITY_LAYOUT = 1

        /**
         * Constant to use in conjunction with [MAIN_ACTIVITY_LAYOUT], refers to Songs Tab
         */
        const val SONGS_FRAGMENT_LAYOUT = 1.1f
        /**
         * Constant to use in conjunction with [MAIN_ACTIVITY_LAYOUT], refers to Playlists Tab
         */
        const val PLAYLISTS_FRAGMENT_LAYOUT = 1.2f
        /**
         * Constant to refer to Playlist Activity
         */
        const val PLAYLIST_ACTIVITY_LAYOUT = 2
        /**
         * Constant to refer to Search Activity
         */
        const val SEARCH_ACTIVITY_LAYOUT = 3
        /**
         * Constant to refer to Play Control Activity
         */
        const val PLAY_CONTROL_ACTIVITY_LAYOUT = 4
    }

    private var activityLayoutChange by Delegates.notNull<Int>()
    private var fragmentLayoutChange by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_layout)

        //From intent
        //- Get activity to change its layout
        //- If it is main activity, retrieve tab selected
        //For the activity, retrieve all layouts available
        //Display layouts and select current in use
        activityLayoutChange = intent.getIntExtra(ACTIVITY_LAYOUT_CHANGE, -1)
        fragmentLayoutChange = intent.getIntExtra(FRAGMENT_LAYOUT_CHANGE, -1)

        //If activityLayoutChange or fragmentLayoutChange not in range
        //Send dialog and go back
    }
}