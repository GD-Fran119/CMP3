package com.example.cmp3

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.RecyclerView
import com.example.animations.ImageFadeInAnimation
import com.example.databaseStuff.AppDatabase
import com.example.databaseStuff.SongPlaylistRelationData
import com.example.dialogs.PlaylistRenameDialog
import com.example.recyclerviewAdapters.PlaylistSongAdapter
import com.example.songsAndPlaylists.SongList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PlaylistView : AppCompatActivity() {
    private lateinit var playlist: SongPlaylistRelationData
    private lateinit var nameTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var durationTextView: TextView
    private lateinit var playlistImageView: ImageView
    private lateinit var recyclerView: RecyclerView
    private var playAllTextSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO
        //Change for customization
        setContentView(R.layout.activity_playlist_view2)

        setUpViewVariables()
        retrievePlaylist()
        setUpButtons()
        setUpRecyclerView()
    }

    private fun retrievePlaylist() {
        try {
            playlist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("playlist", SongPlaylistRelationData::class.java)!!
            } else{
                intent.getParcelableExtra("playlist")!!
            }
            loadPlaylist()
        }catch (_: Exception){
            Toast.makeText(this, "Failure when retrieving song data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpViewVariables() {
        nameTextView = findViewById(R.id.playlist_name)
        //Start marquee animation
        nameTextView.isSelected = true
        dateTextView = findViewById(R.id.playlist_date)
        durationTextView = findViewById(R.id.playlist_duration)
        playlistImageView = findViewById(R.id.playlist_image)
    }

    private fun setUpButtons() {
        findViewById<Button>(R.id.topbar_back_button).setOnClickListener{
            onBackPressed()
        }

        findViewById<Button>(R.id.topbar_options_button).setOnClickListener {
            //TODO
            //Show menu
            val popup = PopupMenu(this, it)
            popup.menuInflater.inflate(R.menu.playlist_menu, popup.menu)
            popup.show()

            popup.setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.playlist_change_layout -> Toast.makeText(
                        this@PlaylistView,
                        "Change layout",
                        Toast.LENGTH_SHORT
                    ).show()

                    R.id.playlist_change_style -> Toast.makeText(
                        this@PlaylistView,
                        "Change style",
                        Toast.LENGTH_SHORT
                    ).show()

                    R.id.playlist_rename -> renamePlaylist()

                    R.id.playlist_delete -> deletePlaylist()
                }
                true
            }
        }
    }

    private fun setUpRecyclerView() {
        val adapter = PlaylistSongAdapter(this, SongList(playlist), supportFragmentManager)
        adapter.onItemRemoved = object: PlaylistSongAdapter.OnItemRemoved{
            override fun notifyItemRemoved(pos: Int) {
                val newList = playlist.songs.toMutableList()
                newList.removeAt(pos)
                playlist.songs = newList.toList()
                val songlist = SongList(playlist)
                findViewById<FragmentContainerView>(R.id.playlist_play_all_fragment).getFragment<PlayAllSongsFragment>()
                    .setList(songlist)
                adapter.playlist = songlist
                adapter.notifyItemRemoved(pos)
                adapter.notifyItemRangeChanged(pos, newList.size)
                loadPlaylist()
            }
        }
        recyclerView = findViewById(R.id.playlist_songs_recyclerview)
        recyclerView.adapter = adapter
    }

    private fun renamePlaylist() {
        val dialog = PlaylistRenameDialog(playlist.playlist.id)

        val listener = object: PlaylistRenameDialog.OnConfirmListener {
            override fun notifyConfirmation() {
                playlist.playlist.name = dialog.getInput()
                nameTextView.text = playlist.playlist.name
            }
        }
        dialog.onConfirmationListener = listener

        dialog.show(supportFragmentManager, "Rename playlist dialog")
    }

    private fun deletePlaylist() {

        val confirmationDialog = android.app.AlertDialog.Builder(this)
                                .setTitle("Delete playlist")
            .setMessage("Are you sure you want to delete ${playlist.playlist.name}?")
            .setIcon(R.drawable.ic_delete)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Delete"
            ) { dialog, _ ->
                CoroutineScope(Dispatchers.Default).launch {
                    val dao = AppDatabase.getInstance(this@PlaylistView).playlistDao()
                    dao.deletePlaylist(playlist.playlist.id)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@PlaylistView,
                            "${playlist.playlist.name} deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                        onBackPressed()
                    }
                }
                dialog.dismiss()
            }
            .create()

        confirmationDialog.show()

    }

    override fun onStart() {
        super.onStart()

        if(!playAllTextSet) {
            val songlist = SongList(playlist)
            findViewById<FragmentContainerView>(R.id.playlist_play_all_fragment).getFragment<PlayAllSongsFragment>()
                .setList(songlist)
            playAllTextSet = true
        }
    }

    private fun loadPlaylist() {

        nameTextView.text = playlist.playlist.name
        dateTextView.text = "Created: ${playlist.playlist.date}"
        durationTextView.text = SongList(playlist).getDuration()

        playlistImageView.setImageResource(R.drawable.ic_music_note)

        if(playlist.songs.isNotEmpty()) {
            CoroutineScope(Dispatchers.Main)
                .launch {
                    val mediaRetriever = MediaMetadataRetriever()
                    val firstSong = playlist.songs[0]
                    mediaRetriever.setDataSource(firstSong.path)

                    val data = mediaRetriever.embeddedPicture
                    mediaRetriever.release()

                    if (data != null) {
                        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)

                        withContext(Dispatchers.Main) {
                            playlistImageView.setImageBitmap(bitmap)
                            playlistImageView.startAnimation(ImageFadeInAnimation(0f, 1f))
                        }
                    }
                }
        }

    }

}