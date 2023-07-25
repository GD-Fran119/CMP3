package com.example.cmp3

import android.content.Intent
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
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.animations.ImageFadeInAnimation
import com.example.playerStuff.Player

import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrentSongFragment : Fragment() {

    private val player = Player.instance
    private lateinit var image: ImageView
    private lateinit var title: TextView
    private lateinit var desc: TextView
    private lateinit var playButton : MaterialButton
    private lateinit var progressBar : ProgressBar
    private var findImageJob : Job? = null
    private var progressJob : Job? = null

    private val listener = object: Player.OnSongChangedListener{
        override fun listen() {

            val song = player.getCurrentSong() ?: return
            title.text = song.title
            desc.text = if(song.artist == "<unknown>") "Unknown"
            else song.artist

            image.setImageResource(R.drawable.ic_music_note)

            findImageJob?.cancel()
            findImageJob = CoroutineScope(Dispatchers.Default).launch {
                val mediaRetriever = MediaMetadataRetriever()
                mediaRetriever.setDataSource(song.path)

                val data = mediaRetriever.embeddedPicture
                mediaRetriever.release()

                if(data != null) {
                    val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)

                    withContext(Dispatchers.Main) {
                        image.setImageBitmap(bitmap)
                        image.startAnimation(ImageFadeInAnimation(0f, 1f))
                    }
                }
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

        image = view.findViewById(R.id.current_song_img)
        title = view.findViewById(R.id.current_song_title)
        desc = view.findViewById(R.id.current_song_desc)

        view.findViewById<ConstraintLayout>(R.id.current_song_container)?.setOnClickListener{
            startActivity(Intent(activity, PlayControlView::class.java))
        }

        progressBar = view.findViewById<ProgressBar>(R.id.current_song_progressbar)

        playButton = view.findViewById(R.id.current_song_pause_play)

        playButton.setOnClickListener{
            if(player.isPlayingSong()){
                player.pause()
                it.setBackgroundResource(R.drawable.ic_play)
            } else{
                player.play()
                it.setBackgroundResource(R.drawable.ic_pause)
            }
        }

        view.findViewById<MaterialButton>(R.id.current_song_next_song)?.setOnClickListener{
            player.playNext()
            playButton.setBackgroundResource(R.drawable.ic_pause)
        }

    }

    override fun onResume() {
        super.onResume()

        if(player.isPlayingSong()) playButton.setBackgroundResource(R.drawable.ic_pause)
        else playButton.setBackgroundResource(R.drawable.ic_play)

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

        player.onSongChangedListener = listener
    }

    override fun onPause() {
        super.onPause()

        player.onSongChangedListener = null
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
}