package com.example.cmp3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.config.GlobalPreferencesConstants
import com.example.config.MainActivityPreferencesConstants
import com.google.android.material.button.MaterialButton
import kotlin.properties.Delegates
import android.content.Intent
import android.widget.ImageView


class ChangeLayoutActivity : AppCompatActivity() {
    companion object{
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
    private var layoutSelected = -1
    private lateinit var imageView: ImageView
    private val layoutsResIds = arrayOf(0, 0, 0)

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

        imageView = findViewById(R.id.change_layout_image)

        setCurrentLayout()

        //If activityLayoutChange or fragmentLayoutChange not in range
        //Send dialog and go back
        findViewById<MaterialButton>(R.id.change_layout_button1).setOnClickListener {
            layoutSelected = 1
            setImageView()
        }

        findViewById<MaterialButton>(R.id.change_layout_button2).setOnClickListener {
            layoutSelected = 2
            setImageView()
        }

        findViewById<MaterialButton>(R.id.change_layout_button3).setOnClickListener {
            layoutSelected = 3
            setImageView()
        }

        findViewById<MaterialButton>(R.id.change_layout_save_button).setOnClickListener {
            if(currentLayout != layoutSelected){
                Toast.makeText(this@ChangeLayoutActivity, "Changes committed", Toast.LENGTH_SHORT).show()
                val prefs = getSharedPreferences(activityPreferencesName, MODE_PRIVATE)
                prefs.edit().apply {
                    putInt(layoutPreferencesKey, layoutSelected)
                }.apply()
            }

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
                        currentLayout = getSharedPreferences(GlobalPreferencesConstants.MAIN_ACT_PREFERENCES, MODE_PRIVATE).getInt(
                            MainActivityPreferencesConstants.SONGS_LAYOUT_KEY, 1)
                        layoutPreferencesKey = MainActivityPreferencesConstants.SONGS_LAYOUT_KEY

                        layoutsResIds[0] = R.drawable.songlist_layout1
                        layoutsResIds[1] = R.drawable.songlist_layout2
                        layoutsResIds[2] = R.drawable.songlist_layout3
                    }
                    PLAYLISTS_FRAGMENT_LAYOUT -> {
                        currentLayout = getSharedPreferences(GlobalPreferencesConstants.MAIN_ACT_PREFERENCES, MODE_PRIVATE).getInt(
                            MainActivityPreferencesConstants.PLAYLISTS_LAYOUT_KEY, 1)
                        layoutPreferencesKey = MainActivityPreferencesConstants.PLAYLISTS_LAYOUT_KEY

                        layoutsResIds[0] = R.drawable.playlists_list_layout1
                        layoutsResIds[1] = R.drawable.playlists_list_layout2
                        layoutsResIds[2] = R.drawable.playlists_list_layout3
                    }
                    else -> {
                        Toast.makeText(this, "Error: fragment needs to be set when Main Activity is selected", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                }

                activityPreferencesName = GlobalPreferencesConstants.MAIN_ACT_PREFERENCES
            }
            PLAYLIST_ACTIVITY_LAYOUT -> {
                currentLayout = getSharedPreferences(GlobalPreferencesConstants.PLAYLIST_ACT_PREFERENCES, MODE_PRIVATE).getInt(
                    GlobalPreferencesConstants.LAYOUT_KEY, 1)

                activityPreferencesName = GlobalPreferencesConstants.PLAYLIST_ACT_PREFERENCES
                layoutPreferencesKey = GlobalPreferencesConstants.LAYOUT_KEY

                layoutsResIds[0] = R.drawable.playlist_layout1
                layoutsResIds[1] = R.drawable.playlist_layout2
                layoutsResIds[2] = R.drawable.playlist_layout3
            }
            PLAY_CONTROL_ACTIVITY_LAYOUT -> {
                currentLayout = getSharedPreferences(GlobalPreferencesConstants.PLAY_CONTROL_ACT_PREFERENCES, MODE_PRIVATE).getInt(
                    GlobalPreferencesConstants.LAYOUT_KEY, 1)

                activityPreferencesName = GlobalPreferencesConstants.PLAY_CONTROL_ACT_PREFERENCES
                layoutPreferencesKey = GlobalPreferencesConstants.LAYOUT_KEY

                layoutsResIds[0] = R.drawable.play_control_layout1
                layoutsResIds[1] = R.drawable.play_control_layout2
                layoutsResIds[2] = R.drawable.play_control_layout3
            }
            SEARCH_ACTIVITY_LAYOUT -> {
                currentLayout = getSharedPreferences(GlobalPreferencesConstants.SEARCH_ACT_PREFERENCES, MODE_PRIVATE).getInt(
                    GlobalPreferencesConstants.LAYOUT_KEY, 1)

                activityPreferencesName = GlobalPreferencesConstants.SEARCH_ACT_PREFERENCES
                layoutPreferencesKey = GlobalPreferencesConstants.LAYOUT_KEY

                layoutsResIds[0] = R.drawable.search_layout1
                layoutsResIds[1] = R.drawable.search_layout2
                layoutsResIds[2] = R.drawable.search_layout3
            }
            ADD_SONGS_ACTIVITY_LAYOUT -> {
                currentLayout = getSharedPreferences(GlobalPreferencesConstants.ADD_SONGS_ACT_PREFERENCES, MODE_PRIVATE).getInt(
                    GlobalPreferencesConstants.LAYOUT_KEY, 1)

                activityPreferencesName = GlobalPreferencesConstants.ADD_SONGS_ACT_PREFERENCES
                layoutPreferencesKey = GlobalPreferencesConstants.LAYOUT_KEY

                layoutsResIds[0] = R.drawable.add_songs_layout1
                layoutsResIds[1] = R.drawable.add_songs_layout2
                layoutsResIds[2] = R.drawable.add_songs_layout3
            }
            else -> {
                Toast.makeText(this, "Error: not proper activity to change layout", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
        }

        if(currentLayout !in 1..3) currentLayout = 1
        layoutSelected = currentLayout

        setImageView()
    }

    private fun setImageView(){
        when(layoutSelected){
            1 -> {
                imageView.setImageResource(layoutsResIds[0])
            }
            2 -> {
                imageView.setImageResource(layoutsResIds[1])
            }
            3 -> {
                imageView.setImageResource(layoutsResIds[2])
            }
            else -> {
                imageView.setImageResource(layoutsResIds[0])
            }
        }
    }
}