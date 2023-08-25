package com.example.cmp3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.animations.ImageFadeInAnimation
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
    private lateinit var image: ImageView
    private lateinit var title: TextView
    private lateinit var desc: TextView
    private lateinit var playButton : MaterialButton
    private lateinit var progressBar : ProgressBar
    //Job that searches and establishes image
    private var findImageJob : Job? = null
    //Job that tracks the progress of the current song
    private var progressJob : Job? = null

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

                if(data != null) {
                    val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)

                    withContext(Dispatchers.Main) {
                        image.foreground = null
                        image.setImageBitmap(bitmap)
                        image.startAnimation(ImageFadeInAnimation(0f, 1f))
                    }
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
        view.findViewById<ConstraintLayout>(R.id.current_song_container)?.setOnClickListener{
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

        view.findViewById<MaterialButton>(R.id.current_song_next_song)?.setOnClickListener{
            if(player.getCurrentSong() == null) return@setOnClickListener

            player.playNext()
            playButton.foreground = AppCompatResources.getDrawable(activity as Context, R.drawable.ic_pause)
        }

    }

    override fun onResume() {
        super.onResume()

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

        //Add listener to Player
        player.onSongChangedListener = listener
    }

    override fun onPause() {
        super.onPause()

        //Cancel jobs
        findImageJob?.cancel()
        progressJob?.cancel()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment CurrentSongFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            CurrentSongFragment()
    }

    /**
     * Class that stores keys for [CurrentSongFragment]'s [SharedPreferences]
     */
    class PreferencesConstants private constructor(){
        companion object{
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