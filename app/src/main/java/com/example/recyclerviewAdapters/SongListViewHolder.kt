package com.example.recyclerviewAdapters

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animations.ImageFadeInAnimation
import com.example.bottomSheets.PlaylistsDialogFragment
import com.example.bottomSheets.SongInfoDialogFragment
import com.example.cmp3.PlayControlView
import com.example.cmp3.R
import com.example.databaseStuff.AppDatabase
import com.example.playerStuff.Player
import com.example.songsAndPlaylists.MainListHolder
import com.example.songsAndPlaylists.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

/**
 * [ViewHolder][RecyclerView.ViewHolder] used in [SongArrayAdapter] and [SearchSongAdapter]
 * @param view view held by the ViewHolder
 * @param context context from where the adapter is created
 */
class SongListViewHolder(private val view: View, private val context: Context) : RecyclerView.ViewHolder(view) {

    private val titleText: TextView = view.findViewById(R.id.song_list_item_title)
    private val subtitleText: TextView = view.findViewById(R.id.song_list_item_album_artist)
    private val imageView: ImageView = view.findViewById(R.id.song_list_item_image)
    private val button : Button = view.findViewById(R.id.song_list_item_button)
    private var job : Job? = null
    private lateinit var song: Song

    /**
     * Links the view elements with the [song]
     * @param song song whose info will be displayed
     * @param pos position of the song in the adapter dataset
     * @param fragmentManager needed when showing song info with [SongInfoDialogFragment]
     */
    fun bind(song: Song, pos: UInt, fragmentManager: FragmentManager) {

        this.song = song
        titleText.text = song.title
        val artist = if (song.artist == "<unknown>") "Unknown" else song.artist
        subtitleText.text = "$artist - ${song.album}"

        job?.cancel()
        imageView.setImageDrawable(null)
        imageView.foreground = AppCompatResources.getDrawable(context, R.drawable.ic_music_note)

        job = CoroutineScope(Dispatchers.Default).launch {
            val mediaRetriever = MediaMetadataRetriever()
            mediaRetriever.setDataSource(song.path)

            val data = mediaRetriever.embeddedPicture
            mediaRetriever.release()

            if (data == null) return@launch

            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)

            withContext(Dispatchers.Main) {
                imageView.foreground = null
                imageView.setImageBitmap(bitmap)
                imageView.startAnimation(ImageFadeInAnimation(0f, 1f))
            }
        }

        view.setOnClickListener {
            //New intent to play control view with song playing
            try {
                Player.instance.setList(MainListHolder.getMainList())
                Player.instance.setCurrentSongAndPLay(pos)
            } catch (e: Exception){
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(context, PlayControlView::class.java)
            context.startActivity(intent)

        }

        button.setOnClickListener {

            val dialog = SongInfoDialogFragment(song)

            val action = object : SongInfoDialogFragment.SongInfoDialogAction {
                override var actionIcon: Int = R.drawable.ic_playlist_add

                override var actionText: String = "Add to playlist"

                override fun onAction() {
                    //TODO
                    CoroutineScope(Dispatchers.Default).launch{
                        val dao = AppDatabase.getInstance(context).playlistDao()
                        val playlists = dao.getPlaylistsInfo()

                        if(playlists.isEmpty()){
                            withContext(Dispatchers.Main){
                                Toast.makeText(context, "No playlists created", Toast.LENGTH_SHORT).show()
                            }
                            return@launch
                        }

                        PlaylistsDialogFragment(playlists, song).show(fragmentManager,"Add to playlist")
                    }
                    dialog.dismiss()
                }
            }
            dialog.action = action
            dialog.show(fragmentManager, "Song info")
        }
    }
}