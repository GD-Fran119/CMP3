package com.example.cmp3

import com.example.songsAndPlaylists.MainListHolder
import com.example.songsAndPlaylists.Song
import com.example.recyclerviewAdapters.SongArrayAdapter
import com.example.songsAndPlaylists.SongList
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
import androidx.media3.common.MimeTypes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.config.GlobalPreferencesConstants
import com.example.config.MainActivityPreferencesConstants
import com.example.config.PlayerStateSaver
import com.example.databaseStuff.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class SongListView : Fragment() {

    private var currentLayout = -1

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                createListView()
                if(mainSongList.isEmpty()) {
                    val textView = view?.findViewById<TextView>(R.id.explanation_text)
                    textView?.text = "No songs found"
                    textView?.visibility = View.VISIBLE
                }
                else
                    deleteExplanationText()

                CoroutineScope(Dispatchers.Default).launch {
                    checkSongsIntegrityDB()
                }
            } else {
                showPermissionExplanation()
            }
        }

    private suspend fun checkSongsIntegrityDB() {
        try {

            val dao = AppDatabase.getInstance(activity as Context).playlistDao()

            //Insert new songs in phone
            val songEntityArray = mainSongList.getList().map{it.toSongEntity()}.toTypedArray()
            //Update db with songs in device
            dao.insertSongs(*(songEntityArray))

            //Check songs deleted from phone that are still in db
            val songs = dao.getSongs()

            val deletedSongs = songs.filter { it !in songEntityArray }.map{it.path}.toTypedArray()
            var dbSongsChanged = false

            //If there are songs in db but not in device, delete them
            if(deletedSongs.isNotEmpty()){
                dao.deleteSongs(*deletedSongs)
                dbSongsChanged = true
            }

            //Notify user songs have been deleted
            if(dbSongsChanged){
                withContext(Dispatchers.Main) {
                    Toast.makeText(activity, "Some songs are no longer stored in this device", Toast.LENGTH_SHORT).show()
                }
            }

        }catch (e: Exception){
            withContext(Dispatchers.Main) {
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private val mainSongList = SongList("Main list", mutableListOf(), "")

    private val title = "Songs"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the create_playlist_dialog_view for this fragment
        return inflater.inflate(R.layout.fragment_song_list_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkForPermissions()
    }

    private fun checkForPermissions(){
        when {
            ContextCompat.checkSelfPermission(
                activity as Context
                ,
                Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                try {
                    createListView()
                    deleteExplanationText()

                    CoroutineScope(Dispatchers.Default).launch {
                        checkSongsIntegrityDB()
                    }
                }catch (e: Exception){
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_AUDIO) -> {
                showPermissionExplanation()
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                if(Build.VERSION.SDK_INT < 33)
                    requestPermissionLauncher.launch(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                else
                    requestPermissionLauncher.launch(
                        Manifest.permission.READ_MEDIA_AUDIO
                    )
            }
        }
    }

    private fun deleteExplanationText(){
        view?.findViewById<TextView>(R.id.explanation_text)?.visibility = View.GONE
    }

    private var listCreated = false
    private fun createListView(){

        findMusic()
        MainListHolder.setMainList(mainSongList)
        listCreated = true
        checkLayoutAndSetUpRecyclerView()

        //Load saved state
        CoroutineScope(Dispatchers.Default).launch {
            PlayerStateSaver.loadState(activity as Context)
        }
        //

        view?.findViewById<FragmentContainerView>(R.id.main_play_all_fragment)?.getFragment<PlayAllSongsFragment>()?.setList(mainSongList)

    }

    private fun checkLayoutAndSetUpRecyclerView(){

        val prefs = activity?.getSharedPreferences(GlobalPreferencesConstants.MAIN_ACT_PREFERENCES, Context.MODE_PRIVATE)
        var savedLayout = prefs?.getInt(MainActivityPreferencesConstants.SONGS_LAYOUT_KEY, -1)

        //No config
        if(savedLayout == -1) {
            prefs?.edit().apply {
                this!!.putInt(MainActivityPreferencesConstants.SONGS_LAYOUT_KEY, 1)
            }?.apply()

            savedLayout = 1
        }

        if(currentLayout != savedLayout){
            currentLayout = if(savedLayout !in 1..3) 1
                            else savedLayout!!

            prefs?.edit().apply {
                this!!.putInt(MainActivityPreferencesConstants.SONGS_LAYOUT_KEY, currentLayout)
            }?.apply()

            val recyclerView = view?.findViewById<RecyclerView>(R.id.main_song_list_view)
            recyclerView?.setHasFixedSize(true)
            val adapter : SongArrayAdapter

            val manager: LayoutManager
            when(currentLayout){
                FULL_WIDTH_LAYOUT -> {
                    manager = LinearLayoutManager(activity)
                    adapter = SongArrayAdapter.create(activity as Activity, mainSongList.getList(), childFragmentManager, R.layout.item_song_list_view1)
                }
                HORIZONTAL_CARD_LAYOUT -> {
                    manager = LinearLayoutManager(activity)
                    adapter = SongArrayAdapter.create(activity as Activity, mainSongList.getList(), childFragmentManager, R.layout.item_song_list_view2)
                }
                VERTICAL_CARD_LAYOUT -> {
                    manager = GridLayoutManager(activity, 2)
                    adapter = SongArrayAdapter.create(activity as Activity, mainSongList.getList(), childFragmentManager, R.layout.item_song_list_view3)
                }
                else -> {
                    manager = LinearLayoutManager(activity)
                    adapter = SongArrayAdapter.create(activity as Activity, mainSongList.getList(), childFragmentManager, R.layout.item_song_list_view1)
                    currentLayout = 1
                    prefs?.edit().apply {
                        this!!.putInt(MainActivityPreferencesConstants.SONGS_LAYOUT_KEY, currentLayout)
                    }?.apply()
                }
            }

            recyclerView?.layoutManager = manager
            recyclerView?.adapter = adapter
        }
    }

    override fun onStart() {
        super.onStart()
        if(listCreated)
            checkLayoutAndSetUpRecyclerView()
    }

    private fun findMusic(){

        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Video.Media.SIZE
        )

        val selection = "${MediaStore.Audio.Media.MIME_TYPE} in (?, ?)"
        val selectionArgs = arrayOf(MimeTypes.AUDIO_MPEG, MimeTypes.AUDIO_MP4)

        val sortOrder = "${MediaStore.Audio.Media.TITLE} COLLATE NOCASE ASC"

        val query = activity?.contentResolver?.query(
            collection,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        query?.use { cursor: Cursor ->
            // Cache column indices.
            val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val titleColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val path = cursor.getString(pathColumn)
                val title = cursor.getString(titleColumn)
                val duration = cursor.getInt(durationColumn)
                val artist = cursor.getString(artistColumn)
                val album = cursor.getString(albumColumn)
                val size = cursor.getInt(sizeColumn)

                mainSongList.addSong(Song(title, artist, album, duration.toUInt(), path, size, null))
            }
        }
    }

    private fun showPermissionExplanation(){
        view?.findViewById<TextView>(R.id.explanation_text)?.text = "No granted permissions"
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SongListView()

        private const val FULL_WIDTH_LAYOUT = 1
        private const val HORIZONTAL_CARD_LAYOUT = 2
        private const val VERTICAL_CARD_LAYOUT = 3

    }

    override fun toString(): String = title
}