package com.example.cmp3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cmp3.changeStyleFragments.AddSongsStyleFragment
import com.example.cmp3.changeStyleFragments.PlayControlStyleFragment
import com.example.cmp3.changeStyleFragments.PlaylistViewStyleFragment
import com.example.cmp3.changeStyleFragments.PlaylistsListStyleFragment
import com.example.cmp3.changeStyleFragments.SearchStyleFragment
import com.example.cmp3.changeStyleFragments.SongListStyleFragment
import com.example.cmp3.changeStyleFragments.StyleFragmentBase
import com.example.config.GlobalPreferencesConstants
import com.google.android.material.button.MaterialButton

/**
 * Activity to change an activity style settings. Requires an [Intent] with:
 * - [ACTIVITY_STYLE_CHANGE]
 * - [FRAGMENT_STYLE_CHANGE], if the activity to change is [MainActivity]
 */
class ChangeStyleActivity : AppCompatActivity() {

    /**
     * Companion object that stores:
     * - [ACTIVITY_STYLE_CHANGE]
     * - [FRAGMENT_STYLE_CHANGE]
     * - [MAIN_ACTIVITY]
     * - [SONGS_FRAGMENT]
     * - [PLAYLISTS_FRAGMENT]
     * - [PLAYLIST_ACTIVITY]
     * - [SEARCH_ACTIVITY]
     * - [PLAY_CONTROL_ACTIVITY]
     * - [ADD_SONGS_ACTIVITY]
     */
    companion object{
        /**
         * Constant to refer to Activity parameter in the [Intent] received by this Activity
         */
        const val ACTIVITY_STYLE_CHANGE = "activity"

        /**
         * Constant to refer to Activity parameter in the [Intent] received by this Activity
         */
        const val FRAGMENT_STYLE_CHANGE = "fragment"

        /**
         * Constant to refer to Main Activity
         */
        const val MAIN_ACTIVITY = 1

        /**
         * Constant to use in conjunction with [MAIN_ACTIVITY], refers to Songs Tab
         */
        const val SONGS_FRAGMENT = 1.1f
        /**
         * Constant to use in conjunction with [MAIN_ACTIVITY], refers to Playlists Tab
         */
        const val PLAYLISTS_FRAGMENT = 1.2f
        /**
         * Constant to refer to Playlist Activity
         */
        const val PLAYLIST_ACTIVITY = 2
        /**
         * Constant to refer to Search Activity
         */
        const val SEARCH_ACTIVITY = 3
        /**
         * Constant to refer to Play Control Activity
         */
        const val PLAY_CONTROL_ACTIVITY = 4
        /**
         * Constant to refer to Play Control Activity
         */
        const val ADD_SONGS_ACTIVITY = 5
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_style)

        val act = intent.getIntExtra(ACTIVITY_STYLE_CHANGE, -1)
        val tab = intent.getFloatExtra(FRAGMENT_STYLE_CHANGE, -1f)

        setUp(act, tab)

    }

    //Fragment that will be displayed and the one with which the user will interact
    private lateinit var fragment: StyleFragmentBase

    /**
     * Establishes [fragment] so the user can interact with it and change the style
     * @param act activity whose style the user wants to change
     * @param tab fragment whose style the user wants to change if [MainActivity] is selected
     */
    private fun setUp(act: Int, tab: Float){
        when(act) {

            MAIN_ACTIVITY -> {
                when(tab){
                    SONGS_FRAGMENT -> {
                        val layout = getSharedPreferences(GlobalPreferencesConstants.MAIN_ACT_PREFERENCES, MODE_PRIVATE).getInt(
                            MainActivity.PreferencesConstants.SONGS_LAYOUT_KEY, 1)
                        val res = when(layout){
                            1 -> R.layout.style_fragment_main_song_list1
                            2 -> R.layout.style_fragment_main_song_list2
                            3 -> R.layout.style_fragment_main_song_list3
                            else -> R.layout.style_fragment_main_song_list1
                        }

                        fragment = SongListStyleFragment(res)
                    }
                    PLAYLISTS_FRAGMENT -> {
                        val layout = getSharedPreferences(GlobalPreferencesConstants.MAIN_ACT_PREFERENCES, MODE_PRIVATE).getInt(
                            MainActivity.PreferencesConstants.PLAYLISTS_LAYOUT_KEY, 1)

                        val res = when(layout){
                            1 -> R.layout.style_fragment_playlists_list1
                            2 -> R.layout.style_fragment_playlists_list2
                            3 -> R.layout.style_fragment_playlists_list3
                            else -> R.layout.style_fragment_playlists_list1
                        }

                        fragment = PlaylistsListStyleFragment(res)
                    }
                    else -> {
                        Toast.makeText(this, "Error: fragment needs to be set when Main Activity is selected", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                }
            }

            PLAY_CONTROL_ACTIVITY -> {
                val layout = getSharedPreferences(GlobalPreferencesConstants.PLAY_CONTROL_ACT_PREFERENCES, MODE_PRIVATE).getInt(
                    GlobalPreferencesConstants.LAYOUT_KEY, 1)

                val res = when(layout){
                    1 -> R.layout.style_fragment_play_control1
                    2 -> R.layout.style_fragment_play_control2
                    3 -> R.layout.style_fragment_play_control3
                    else -> R.layout.style_fragment_play_control1
                }

                fragment = PlayControlStyleFragment(res)
            }

            PLAYLIST_ACTIVITY -> {
                val layout = getSharedPreferences(GlobalPreferencesConstants.PLAYLIST_ACT_PREFERENCES, MODE_PRIVATE).getInt(
                    GlobalPreferencesConstants.LAYOUT_KEY, 1)

                val res = when(layout){
                    1 -> R.layout.style_fragment_playlist_view1
                    2 -> R.layout.style_fragment_playlist_view2
                    3 -> R.layout.style_fragment_playlist_view3
                    else -> R.layout.style_fragment_playlist_view1
                }

                fragment = PlaylistViewStyleFragment(res)
            }

            ADD_SONGS_ACTIVITY -> {
                val layout = getSharedPreferences(GlobalPreferencesConstants.ADD_SONGS_ACT_PREFERENCES, MODE_PRIVATE).getInt(
                    GlobalPreferencesConstants.LAYOUT_KEY, 1)

                val res = when(layout){
                    1 -> R.layout.style_fragment_add_songs1
                    2 -> R.layout.style_fragment_add_songs2
                    3 -> R.layout.style_fragment_add_songs3
                    else -> R.layout.style_fragment_add_songs1
                }

                fragment = AddSongsStyleFragment(res)
            }

            SEARCH_ACTIVITY -> {
                val layout = getSharedPreferences(GlobalPreferencesConstants.SEARCH_ACT_PREFERENCES, MODE_PRIVATE).getInt(
                    GlobalPreferencesConstants.LAYOUT_KEY, 1)

                val res = when(layout){
                    1 -> R.layout.style_fragment_search1
                    2 -> R.layout.style_fragment_search2
                    3 -> R.layout.style_fragment_search3
                    else -> R.layout.style_fragment_search1
                }

                fragment = SearchStyleFragment(res)
            }
            else -> {
                Toast.makeText(this@ChangeStyleActivity, "Error: not proper activity to change layout", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
        }

        //Set fragment
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.change_style_fragment_container, fragment)
            .commit()

        //Set button click for confirmation
        findViewById<MaterialButton>(R.id.change_style_save_button).setOnClickListener {
            fragment.saveChanges()
            onBackPressed()
        }

        //Set button click for cancellation
        findViewById<MaterialButton>(R.id.change_style_cancel_button).setOnClickListener {
            onBackPressed()
        }
    }

}