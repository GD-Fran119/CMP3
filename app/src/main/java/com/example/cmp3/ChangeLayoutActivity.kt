package com.example.cmp3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.config.GlobalPreferencesConstants
import com.example.config.MainActivityPreferencesConstants
import com.google.android.material.button.MaterialButton
import kotlin.properties.Delegates


class ChangeLayoutActivity : AppCompatActivity() {
    companion object{
        /**
         * Name for SharedPreferences that store layout prefs:
         * - [MAIN_ACT_PREFERENCES]
         * - [PLAYLIST_ACT_PREFERENCES]
         * - [SEARCH_ACT_PREFERENCES]
         * - [PLAY_CONTROL_ACT_PREFERENCES]
         * - [ADD_SONGS_ACT_PREFERENCES]
         */
        const val MAIN_ACT_PREFERENCES = "main_layout_prefs"
        /**
         * Name for SharedPreferences that store layout prefs:
         * - [MAIN_ACT_PREFERENCES]
         * - [PLAYLIST_ACT_PREFERENCES]
         * - [SEARCH_ACT_PREFERENCES]
         * - [PLAY_CONTROL_ACT_PREFERENCES]
         * - [ADD_SONGS_ACT_PREFERENCES]
         */
        const val PLAYLIST_ACT_PREFERENCES = "playlist_layout_prefs"
        /**
         * Name for SharedPreferences that store layout prefs:
         * - [MAIN_ACT_PREFERENCES]
         * - [PLAYLIST_ACT_PREFERENCES]
         * - [SEARCH_ACT_PREFERENCES]
         * - [PLAY_CONTROL_ACT_PREFERENCES]
         * - [ADD_SONGS_ACT_PREFERENCES]
         */
        const val SEARCH_ACT_PREFERENCES = "search_layout_prefs"
        /**
         * Name for SharedPreferences that store layout prefs:
         * - [MAIN_ACT_PREFERENCES]
         * - [PLAYLIST_ACT_PREFERENCES]
         * - [SEARCH_ACT_PREFERENCES]
         * - [PLAY_CONTROL_ACT_PREFERENCES]
         * - [ADD_SONGS_ACT_PREFERENCES]
         */
        const val PLAY_CONTROL_ACT_PREFERENCES = "play_control_layout_prefs"
        /**
         * Name for SharedPreferences that store layout prefs:
         * - [MAIN_ACT_PREFERENCES]
         * - [PLAYLIST_ACT_PREFERENCES]
         * - [SEARCH_ACT_PREFERENCES]
         * - [PLAY_CONTROL_ACT_PREFERENCES]
         * - [ADD_SONGS_ACT_PREFERENCES]
         */
        const val ADD_SONGS_ACT_PREFERENCES = "add_songs_layout_prefs"

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
        /**
         * Constant to refer to Play Control Activity
         */
        const val ADD_SONGS_ACTIVITY_LAYOUT = 5
    }

    private var activityLayoutChange by Delegates.notNull<Int>()
    private var fragmentLayoutChange by Delegates.notNull<Float>()
    private lateinit var activityPreferencesName: String
    private lateinit var layoutPreferencesKey: String

    private var currentLayout by Delegates.notNull<Int>()
    private var layoutSelected by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_layout)

        //From intent
        //- Get activity to change its layout
        //- If it is main activity, retrieve tab selected
        //For the activity, retrieve all layouts available
        //Display layouts and select current in use
        activityLayoutChange = intent.getIntExtra(ACTIVITY_LAYOUT_CHANGE, -1)
        fragmentLayoutChange = intent.getFloatExtra(FRAGMENT_LAYOUT_CHANGE, -1.0f)

        setCurrentLayout()

        //If activityLayoutChange or fragmentLayoutChange not in range
        //Send dialog and go back
        findViewById<MaterialButton>(R.id.change_layout_button1).setOnClickListener {
            layoutSelected = 1
        }

        findViewById<MaterialButton>(R.id.change_layout_button2).setOnClickListener {
            layoutSelected = 2
        }

        findViewById<MaterialButton>(R.id.change_layout_button3).setOnClickListener {
            layoutSelected = 3
        }

        findViewById<MaterialButton>(R.id.change_layout_save_button).setOnClickListener {
            Toast.makeText(this@ChangeLayoutActivity, "Changes committed", Toast.LENGTH_SHORT).show()
            val prefs = getSharedPreferences(activityPreferencesName, MODE_PRIVATE)
            prefs.edit().apply {
                putInt(layoutPreferencesKey, layoutSelected)
            }.apply()
            onBackPressed()
        }

        findViewById<MaterialButton>(R.id.change_layout_cancel_button).setOnClickListener {
            onBackPressed()
        }
    }

    private fun setCurrentLayout() {
        when(activityLayoutChange){
            MAIN_ACTIVITY_LAYOUT -> {
                when(fragmentLayoutChange){
                    SONGS_FRAGMENT_LAYOUT->{
                        currentLayout = getSharedPreferences(MAIN_ACT_PREFERENCES, MODE_PRIVATE).getInt(
                            MainActivityPreferencesConstants.SONGS_LAYOUT_KEY, 1)
                        layoutPreferencesKey = MainActivityPreferencesConstants.SONGS_LAYOUT_KEY
                    }
                    PLAYLISTS_FRAGMENT_LAYOUT -> {
                        currentLayout = getSharedPreferences(MAIN_ACT_PREFERENCES, MODE_PRIVATE).getInt(
                            MainActivityPreferencesConstants.PLAYLISTS_LAYOUT_KEY, 1)
                        layoutPreferencesKey = MainActivityPreferencesConstants.PLAYLISTS_LAYOUT_KEY
                    }
                    else -> {
                        Toast.makeText(this, "Error: not proper activity to change layout", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                }

                activityPreferencesName = MAIN_ACT_PREFERENCES
            }
            PLAYLIST_ACTIVITY_LAYOUT -> {
                currentLayout = getSharedPreferences(PLAYLIST_ACT_PREFERENCES, MODE_PRIVATE).getInt(
                    GlobalPreferencesConstants.LAYOUT_KEY, 1)

                activityPreferencesName = PLAYLIST_ACT_PREFERENCES
                layoutPreferencesKey = GlobalPreferencesConstants.LAYOUT_KEY
            }
            PLAY_CONTROL_ACTIVITY_LAYOUT -> {
                currentLayout = getSharedPreferences(PLAY_CONTROL_ACT_PREFERENCES, MODE_PRIVATE).getInt(
                    GlobalPreferencesConstants.LAYOUT_KEY, 1)

                activityPreferencesName = PLAY_CONTROL_ACT_PREFERENCES
                layoutPreferencesKey = GlobalPreferencesConstants.LAYOUT_KEY
            }
            SEARCH_ACTIVITY_LAYOUT -> {
                currentLayout = getSharedPreferences(SEARCH_ACT_PREFERENCES, MODE_PRIVATE).getInt(
                    GlobalPreferencesConstants.LAYOUT_KEY, 1)

                activityPreferencesName = SEARCH_ACT_PREFERENCES
                layoutPreferencesKey = GlobalPreferencesConstants.LAYOUT_KEY
            }
            ADD_SONGS_ACTIVITY_LAYOUT -> {
                currentLayout = getSharedPreferences(ADD_SONGS_ACT_PREFERENCES, MODE_PRIVATE).getInt(
                    GlobalPreferencesConstants.LAYOUT_KEY, 1)

                activityPreferencesName = ADD_SONGS_ACT_PREFERENCES
                layoutPreferencesKey = GlobalPreferencesConstants.LAYOUT_KEY
            }
            else -> {
                Toast.makeText(this, "Error: not proper activity to change layout", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
        }

        if(currentLayout !in 1..3) currentLayout = 1
    }
}