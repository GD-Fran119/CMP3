package com.example.config

import android.content.SharedPreferences

/**
 * Class that stores activities' [SharedPreferences] names. It also stores a key for layout prefs.
 */
class GlobalPreferencesConstants private constructor(){
    companion object{

        /**
         * Key that refers to activity layout
         */
        const val LAYOUT_KEY = "layout"

        /**
         * Name for SharedPreferences key that refers to layout prefs:
         * - [MAIN_ACT_PREFERENCES]
         * - [PLAYLIST_ACT_PREFERENCES]
         * - [SEARCH_ACT_PREFERENCES]
         * - [PLAY_CONTROL_ACT_PREFERENCES]
         * - [ADD_SONGS_ACT_PREFERENCES]
         * - [PLAY_ALL_FRAGMENT_PREFERENCES]
         * - [CURRENT_SONG_FRAGMENT_PREFERENCES]
         */
        const val MAIN_ACT_PREFERENCES = "main_layout_prefs"

        /**
         * Name for SharedPreferences that store layout prefs:
         * - [MAIN_ACT_PREFERENCES]
         * - [PLAYLIST_ACT_PREFERENCES]
         * - [SEARCH_ACT_PREFERENCES]
         * - [PLAY_CONTROL_ACT_PREFERENCES]
         * - [ADD_SONGS_ACT_PREFERENCES]
         * - [PLAY_ALL_FRAGMENT_PREFERENCES]
         * - [CURRENT_SONG_FRAGMENT_PREFERENCES]
         */
        const val PLAYLIST_ACT_PREFERENCES = "playlist_layout_prefs"

        /**
         * Name for SharedPreferences that store layout prefs:
         * - [MAIN_ACT_PREFERENCES]
         * - [PLAYLIST_ACT_PREFERENCES]
         * - [SEARCH_ACT_PREFERENCES]
         * - [PLAY_CONTROL_ACT_PREFERENCES]
         * - [ADD_SONGS_ACT_PREFERENCES]
         * - [PLAY_ALL_FRAGMENT_PREFERENCES]
         * - [CURRENT_SONG_FRAGMENT_PREFERENCES]
         */
        const val SEARCH_ACT_PREFERENCES = "search_layout_prefs"

        /**
         * Name for SharedPreferences that store layout prefs:
         * - [MAIN_ACT_PREFERENCES]
         * - [PLAYLIST_ACT_PREFERENCES]
         * - [SEARCH_ACT_PREFERENCES]
         * - [PLAY_CONTROL_ACT_PREFERENCES]
         * - [ADD_SONGS_ACT_PREFERENCES]
         * - [PLAY_ALL_FRAGMENT_PREFERENCES]
         * - [CURRENT_SONG_FRAGMENT_PREFERENCES]
         */
        const val PLAY_CONTROL_ACT_PREFERENCES = "play_control_layout_prefs"

        /**
         * Name for SharedPreferences that store layout prefs:
         * - [MAIN_ACT_PREFERENCES]
         * - [PLAYLIST_ACT_PREFERENCES]
         * - [SEARCH_ACT_PREFERENCES]
         * - [PLAY_CONTROL_ACT_PREFERENCES]
         * - [ADD_SONGS_ACT_PREFERENCES]
         * - [PLAY_ALL_FRAGMENT_PREFERENCES]
         * - [CURRENT_SONG_FRAGMENT_PREFERENCES]
         */
        const val ADD_SONGS_ACT_PREFERENCES = "add_songs_layout_prefs"

        /**
         * Name for SharedPreferences that store layout prefs:
         * - [MAIN_ACT_PREFERENCES]
         * - [PLAYLIST_ACT_PREFERENCES]
         * - [SEARCH_ACT_PREFERENCES]
         * - [PLAY_CONTROL_ACT_PREFERENCES]
         * - [ADD_SONGS_ACT_PREFERENCES]
         * - [PLAY_ALL_FRAGMENT_PREFERENCES]
         * - [CURRENT_SONG_FRAGMENT_PREFERENCES]
         */
        const val PLAY_ALL_FRAGMENT_PREFERENCES = "play_all_songs_fragment_prefs"

        /**
         * Name for SharedPreferences that store layout prefs:
         * - [MAIN_ACT_PREFERENCES]
         * - [PLAYLIST_ACT_PREFERENCES]
         * - [SEARCH_ACT_PREFERENCES]
         * - [PLAY_CONTROL_ACT_PREFERENCES]
         * - [ADD_SONGS_ACT_PREFERENCES]
         * - [PLAY_ALL_FRAGMENT_PREFERENCES]
         * - [CURRENT_SONG_FRAGMENT_PREFERENCES]
         */
        const val CURRENT_SONG_FRAGMENT_PREFERENCES = "current_song_fragment_prefs"
    }
}