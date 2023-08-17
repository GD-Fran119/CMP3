package com.example.cmp3.playlistView

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Guideline
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.RecyclerView
import com.example.cmp3.ChangeLayoutActivity
import com.example.cmp3.PlayAllSongsFragment
import com.example.cmp3.R
import com.example.config.GlobalPreferencesConstants
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

    companion object{
        private const val SQUARE_IMAGE_LAYOUT = 1
        private const val WIDE_IMAGE_LAYOUT = 2
        private const val ROUND_IMAGE_LAYOUT = 3
    }

    private var currentLayout = -1

    private lateinit var playlist: SongPlaylistRelationData
    private lateinit var recyclerView: RecyclerView
    private lateinit var fragmentContainerView: FragmentContainerView
    private var playAllTextSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_info)

        fragmentContainerView = findViewById(R.id.playlist_info_container)

        retrievePlaylist()
        setUpButtons()
        setUpRecyclerView()
    }

    override fun onStart() {
        super.onStart()

        checkAndSetUpInfoFragment()

        if(!playAllTextSet) {
            setUpPlayAllSongs()
            playAllTextSet = true
        }
    }

    private fun checkAndSetUpInfoFragment(){
        val prefs = getSharedPreferences(ChangeLayoutActivity.PLAYLIST_ACT_PREFERENCES, Context.MODE_PRIVATE)
        var layoutSaved = prefs.getInt(GlobalPreferencesConstants.LAYOUT_KEY, -1)

        //No config
        if(layoutSaved == -1) {
            prefs.edit().apply {
                putInt(GlobalPreferencesConstants.LAYOUT_KEY, 1)
            }.apply()

            layoutSaved = 1
        }

        //Layout changed
        if(currentLayout != layoutSaved){
            //Update layout so it matches config
            currentLayout = layoutSaved
            val transaction = supportFragmentManager.beginTransaction().setReorderingAllowed(true)

            when(currentLayout){
                ROUND_IMAGE_LAYOUT -> {
                    transaction.replace(R.id.playlist_info_container, PlaylistInfoFragment3())
                    findViewById<Guideline>(R.id.playlist_info_guideline).setGuidelinePercent(0.35f)
                }
                SQUARE_IMAGE_LAYOUT ->{
                    transaction.replace(R.id.playlist_info_container, PlaylistInfoFragment1())
                    findViewById<Guideline>(R.id.playlist_info_guideline).setGuidelinePercent(0.35f)
                }
                WIDE_IMAGE_LAYOUT -> {
                    transaction.replace(R.id.playlist_info_container, PlaylistInfoFragment2())
                    findViewById<Guideline>(R.id.playlist_info_guideline).setGuidelinePercent(0.45f)
                }
                else -> {//Integrity error E.g.: currentLayout == 8
                    transaction.replace(R.id.playlist_info_container, PlaylistInfoFragment1())
                    findViewById<Guideline>(R.id.playlist_info_guideline).setGuidelinePercent(0.35f)
                    currentLayout = 1
                    prefs.edit().apply {
                        putInt(GlobalPreferencesConstants.LAYOUT_KEY, 1)
                    }.apply()
                }
            }

            transaction.runOnCommit {
                loadPlaylist()
            }

            transaction.commit()

        }
    }


    private fun retrievePlaylist() {
        try {
            playlist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            intent.getParcelableExtra("playlist", SongPlaylistRelationData::class.java)!!
                        } else{
                            intent.getParcelableExtra("playlist")!!
                        }
        }catch (_: Exception){
            Toast.makeText(this, "Failure when retrieving song data", Toast.LENGTH_SHORT).show()
        }
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
                    R.id.playlist_change_layout -> {
                        val intent = Intent(this@PlaylistView, ChangeLayoutActivity::class.java)
                        intent.putExtra(ChangeLayoutActivity.ACTIVITY_LAYOUT_CHANGE, ChangeLayoutActivity.PLAYLIST_ACTIVITY_LAYOUT)
                        startActivity(intent)
                    }

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
                val songList = SongList(playlist)
                findViewById<FragmentContainerView>(R.id.playlist_play_all_fragment).getFragment<PlayAllSongsFragment>()
                    .setList(songList)
                adapter.playlist = songList
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
                loadPlaylist()
                setUpPlayAllSongs()
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


    private fun setUpPlayAllSongs(){
        val songList = SongList(playlist)
        findViewById<FragmentContainerView>(R.id.playlist_play_all_fragment).getFragment<PlayAllSongsFragment>()
            .setList(songList)
    }

    private fun loadPlaylist() {
        fragmentContainerView.getFragment<PlaylistInfoFragment>().setlist(SongList(playlist))
    }
}