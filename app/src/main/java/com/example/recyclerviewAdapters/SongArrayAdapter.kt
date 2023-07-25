package com.example.recyclerviewAdapters

import com.example.animations.ImageFadeInAnimation
import com.example.songsAndPlaylists.MainListHolder
import com.example.playerStuff.Player
import com.example.songsAndPlaylists.Song
import com.example.songsAndPlaylists.SongList
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomSheets.SongInfoDialogFragment
import com.example.cmp3.PlayControlView
import com.example.cmp3.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SongArrayAdapter private constructor(private var context: Context, private var songs: SongList, private val fragmentManager: FragmentManager) :
    RecyclerView.Adapter<SongArrayAdapter.SongListViewHolder>() {

    companion object{
        fun create(c : Context, s: SongList, fragmentManager: FragmentManager): SongArrayAdapter {
            return SongArrayAdapter(c, s, fragmentManager)
        }
    }

    class SongListViewHolder(private val view: View, private val activity: Context) : RecyclerView.ViewHolder(view) {

        private val titleText: TextView = view.findViewById(R.id.title)
        private val subtitleText: TextView = view.findViewById(R.id.albumNArtist)
        private val imageView: ImageView = view.findViewById(R.id.icon)
        private val button : Button = view.findViewById(R.id.mainViewSongButton)
        private var job : Job? = null
        private lateinit var song: Song

        fun bind(song: Song, pos: UInt, fragmentManager: FragmentManager) {

            this.song = song
            titleText.text = song.title
            val artist = if (song.artist == "<unknown>") "Unknown" else song.artist
            subtitleText.text = "$artist - ${song.album}"

            job?.cancel()
            imageView.setImageResource(R.drawable.ic_music_note)
            Log.i("Song adapter", "Bind calling detected")

            //TODO
            //Fix infinite binding
            job = CoroutineScope(Dispatchers.Default).launch {

                val mediaRetriever = MediaMetadataRetriever()
                mediaRetriever.setDataSource(song.path)

                val data = mediaRetriever.embeddedPicture
                mediaRetriever.release()

                if (data != null) {

                    val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)

                    withContext(Dispatchers.Main) {

                        //imageView.setImageBitmap(bitmap)
                        imageView.startAnimation(ImageFadeInAnimation(0f, 1f))

                    }
                }

            }

            view.setOnClickListener {
                //New intent to play control view with song playing
                try {
                    Player.instance.setList(MainListHolder.getMainList())
                    Player.instance.setCurrentSongAndPLay(pos)
                }catch (e: Exception){
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(activity, PlayControlView::class.java)
                activity.startActivity(intent)

            }

            button.setOnClickListener {

                val dialog = SongInfoDialogFragment(song)

                val action = object : SongInfoDialogFragment.SongInfoDialogAction{
                    override var actionIcon: Int = R.drawable.ic_playlist_add

                    override var actionText: String = "Add to playlist"

                    override fun onAction() {
                        //TODO
                        //Show playlists bottom sheet dialog
                        Toast.makeText(activity, "Song added", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }

                }

                dialog.action = action
                dialog.show(fragmentManager, "Song info")
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_song_list_view, parent, false)
        return SongListViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: SongListViewHolder, position: Int) {
        val song = songs.getSong(position.toUInt())
        holder.bind(song, position.toUInt(), fragmentManager)
    }

    override fun getItemCount(): Int {
        return songs.getListSize().toInt()
    }
}