package com.example.cmp3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.animations.ImageFadeInAnimation
import com.example.config.GlobalPreferencesConstants
import com.example.config.PlayerStateSaver
import com.example.playerStuff.Player

import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Fragment that displays [Player]'s current song information
 */
class CurrentSongFragment : Fragment() {

    private val player = Player.instance
    private lateinit var bgLayout: ConstraintLayout
    private lateinit var image: ImageView
    private lateinit var title: TextView
    private lateinit var desc: TextView
    private lateinit var playButton : MaterialButton
    private lateinit var nextButton : MaterialButton
    private lateinit var progressBar : ProgressBar
    //Job that searches and establishes image
    private var findImageJob : Job? = null
    //Job that tracks the progress of the current song
    private var progressJob : Job? = null
    private var currentStyleVersion = -1

    //Listener for player
    private val listener = object: Player.OnSongChangedListener{
        private var saverJob: Job? = null
        override fun listen() {

            //Update UI
            val song = player.getCurrentSong() ?: return
            title.text = song.title
            desc.text = if(song.artist == "<unknown>") "Unknown"
                        else song.artist

            image.setImageDrawable(null)
            image.foreground = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_music_note)

            findImageJob?.cancel()
            findImageJob = CoroutineScope(Dispatchers.Default).launch {
                val mediaRetriever = MediaMetadataRetriever()
                mediaRetriever.setDataSource(song.path)

                val data = mediaRetriever.embeddedPicture
                mediaRetriever.release()

                if(data == null) return@launch

                val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)

                withContext(Dispatchers.Main) {
                    image.foreground = null
                    image.setImageBitmap(bitmap)
                    image.startAnimation(ImageFadeInAnimation(0f, 1f))

                }
            }

            //End of UI updating

            //Save Player state
            saverJob?.cancel()
            saverJob = CoroutineScope(Dispatchers.Default).launch {
                PlayerStateSaver.saveState(requireContext())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_song, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Set variables
        image = view.findViewById(R.id.current_song_img)
        title = view.findViewById(R.id.current_song_title)
        desc = view.findViewById(R.id.current_song_desc)
        title.isSelected = true

        //If layout is clicked go to Play Control Activity
        bgLayout = view.findViewById(R.id.current_song_container)
        bgLayout.setOnClickListener{
            if(player.getCurrentSong() == null) return@setOnClickListener

            startActivity(Intent(activity, PlayControlView::class.java))
        }

        //Mode variables setting
        progressBar = view.findViewById(R.id.current_song_progressbar)

        playButton = view.findViewById(R.id.current_song_pause_play)
        playButton.setOnClickListener{
            if(player.getCurrentSong() == null) return@setOnClickListener

            if(player.isPlayingSong()){
                player.pause()
                it.foreground = AppCompatResources.getDrawable(activity as Context, R.drawable.ic_play)
            } else{
                player.play()
                it.foreground = AppCompatResources.getDrawable(activity as Context, R.drawable.ic_pause)
            }
        }

        nextButton = view.findViewById(R.id.current_song_next_song)
        nextButton.setOnClickListener{
            if(player.getCurrentSong() == null) return@setOnClickListener

            player.playNext()
            playButton.foreground = AppCompatResources.getDrawable(activity as Context, R.drawable.ic_pause)
        }

    }

    /**
     * Checks whether the current style has been changed. If so, the new style is set
     */
    private fun checkAndSetUpStyle() {
        val prefs = requireContext().getSharedPreferences(
            GlobalPreferencesConstants.CURRENT_SONG_FRAGMENT_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
        var version = prefs.getInt(PlayControlView.PreferencesConstants.STYLE_VERSION_KEY, 0)

        if(version == 0) {
            createStylePreferences(prefs)
            version = prefs.getInt(PlayControlView.PreferencesConstants.STYLE_VERSION_KEY, 0)
        }

        if(currentStyleVersion == version) return

        currentStyleVersion = version

        prefs.apply {

            val constants = PreferencesConstants
            bgLayout.backgroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.FRAGMENT_LAYOUT_KEY,
                    requireContext().getColor(R.color.default_layout_bg)
                )
            )

            progressBar.progressTintList = ColorStateList.valueOf(
                getInt(
                    constants.PROGRESS_BAR_KEY,
                    requireContext().getColor(R.color.default_progress_bar_color)
                )
            )
            progressBar.progressBackgroundTintList = progressBar.progressTintList

            image.backgroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.IMAGE_BG_KEY,
                    requireContext().getColor(R.color.default_image_placeholder_bg)
                )
            )
            image.foregroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.IMAGE_FG_KEY,
                    requireContext().getColor(R.color.default_image_placeholder_fg)
                )
            )

            val songInfoColor = getInt(
                constants.TEXT_KEY,
                requireContext().getColor(R.color.default_text_color)
            )
            title.setTextColor(songInfoColor)
            desc.setTextColor(songInfoColor)

            playButton.backgroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.PLAY_BTN_BG_KEY,
                    requireContext().getColor(R.color.default_buttons_bg)
                )
            )
            playButton.foregroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.PLAY_BTN_FG_KEY,
                    requireContext().getColor(R.color.default_buttons_fg)
                )
            )
            nextButton.backgroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.NEXT_BTN_BG_KEY,
                    requireContext().getColor(R.color.default_buttons_bg)
                )
            )
            nextButton.foregroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.NEXT_BTN_FG_KEY,
                    requireContext().getColor(R.color.default_buttons_fg)
                )
            )
        }
    }

    /**
     * Creates the default style for the view
     * @param preferences [SharedPreferences] where the style will be stored
     */
    private fun createStylePreferences(preferences: SharedPreferences) {
        val constants = PreferencesConstants
        preferences.edit().apply {
            putInt(constants.FRAGMENT_LAYOUT_KEY, requireContext().getColor(R.color.default_layout_bg))
            putInt(constants.PROGRESS_BAR_KEY, requireContext().getColor(R.color.default_progress_bar_color))
            putInt(constants.IMAGE_BG_KEY, requireContext().getColor(R.color.default_image_placeholder_bg))
            putInt(constants.IMAGE_FG_KEY, requireContext().getColor(R.color.default_image_placeholder_fg))
            putInt(constants.TEXT_KEY, requireContext().getColor(R.color.default_text_color))
            putInt(constants.PLAY_BTN_BG_KEY, requireContext().getColor(R.color.default_buttons_bg))
            putInt(constants.PLAY_BTN_FG_KEY, requireContext().getColor(R.color.default_buttons_fg))
            putInt(constants.NEXT_BTN_BG_KEY, requireContext().getColor(R.color.default_buttons_bg))
            putInt(constants.NEXT_BTN_FG_KEY, requireContext().getColor(R.color.default_buttons_fg))
            putInt(constants.STYLE_VERSION_KEY, 1)
        }.apply()
    }

    override fun onStart() {
        super.onStart()

        //Update play button
        if(player.isPlayingSong()) playButton.foreground = AppCompatResources.getDrawable(activity as Context, R.drawable.ic_pause)
        else playButton.foreground = AppCompatResources.getDrawable(activity as Context, R.drawable.ic_play)

        //Set up progress bar job
        progressJob?.cancel()
        progressJob = CoroutineScope(Dispatchers.Default).launch {
            while(true){
                if(player.isAvailableProgress()){
                    progressBar.max = player.getCurrentSongDuration().toInt()
                    progressBar.progress = player.getCurrentSongProgress()
                }
                delay(500)
            }

        }

        checkAndSetUpStyle()

        //Add listener to Player
        player.onSongChangedListener = listener
    }

    override fun onPause() {
        super.onPause()

        //Cancel jobs
        findImageJob?.cancel()
        progressJob?.cancel()
    }

    /**
     * Class that stores keys for [CurrentSongFragment]'s [SharedPreferences]
     */
    class PreferencesConstants private constructor(){
        companion object{
            /**
             * Key that refers to fragment style version
             */
            const val STYLE_VERSION_KEY = "version"

            /**
             * Key that refers to Fragment background color
             */
            const val FRAGMENT_LAYOUT_KEY = "layout_bg_color"
            /**
             * Key that refers to Fragment progress bar color
             */
            const val PROGRESS_BAR_KEY = "progress_bar_color"
            /**
             * Key that refers to Fragment image foreground color
             */
            const val IMAGE_FG_KEY = "image_fg_color"
            /**
             * Key that refers to Fragment image background color
             */
            const val IMAGE_BG_KEY = "image_bg_color"
            /**
             * Key that refers to Fragment text color
             */
            const val TEXT_KEY = "text_color"
            /**
             * Key that refers to Fragment play button foreground color
             */
            const val PLAY_BTN_FG_KEY = "play_button_fg_color"
            /**
             * Key that refers to Fragment play button background color
             */
            const val PLAY_BTN_BG_KEY = "play_button_bg_color"
            /**
             * Key that refers to Fragment next button foreground color
             */
            const val NEXT_BTN_FG_KEY = "next_button_fg_color"
            /**
             * Key that refers to Fragment next button background color
             */
            const val NEXT_BTN_BG_KEY = "next_button_bg_color"
        }
    }
}