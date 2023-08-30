package com.example.cmp3

import com.example.songsAndPlaylists.MainListHolder
import com.example.songsAndPlaylists.Song
import com.example.recyclerviewAdapters.SongArrayAdapter
import com.example.songsAndPlaylists.SongList
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
import androidx.media3.common.MimeTypes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.config.GlobalPreferencesConstants
import com.example.config.PlayerStateSaver
import com.example.databaseStuff.AppDatabase
import com.example.databaseStuff.PlaylistDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import com.google.android.material.tabs.TabLayout

/**
 * Fragment that displays the information of the available music files stored in the user's device.
 * It also looks for this music files in the device.
 */
class SongListView : Fragment() {

    //Custom established layout
    private var currentLayout = -1
    private lateinit var recyclerView: RecyclerView
    //Custom style version
    private var currentStyleVersion = -1
    //Check whether style must be set no matter the version
    private var mustChangeStyle = false

    /**
     * Launcher for requesting reading storage permission
     */
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

    /**
     * Checks that the information stored in the database is consistent with the device information.
     * This method checks:
     * - If there are new songs in the device, then insert the new info in the database.
     * - If some songs are no longer in the device, they are deleted from the database with their side effects
     * @see [PlaylistDAO.deleteSongs]
     */
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

        } catch (e: Exception){
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

        recyclerView = view.findViewById(R.id.main_song_list_view)
        recyclerView.setHasFixedSize(true)

        checkForPermissions()
    }

    /**
     * Check if permissions are granted ([Manifest.permission.READ_EXTERNAL_STORAGE] if SDK < 33, [Manifest.permission.READ_MEDIA_AUDIO] otherwise).
     * If permissions are granted, songs are loaded from the device
     */
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

    /**
     * Hides explanation text (used when permissions are not granted or when there are no songs in the device)
     */
    private fun deleteExplanationText(){
        view?.findViewById<TextView>(R.id.explanation_text)?.visibility = View.GONE
    }

    private var listCreated = false

    /**
     * Sets up the view itself:
     * - Finds music
     * - Establishes the current layout in use
     * - Sets up the [PlayAllSongsFragment] so it gets functional
     */
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

    /**
     * Checks whether the current layout has been changed. If so, the new layout is set
     */
    private fun checkLayoutAndSetUpRecyclerView(){

        val prefs = activity?.getSharedPreferences(GlobalPreferencesConstants.MAIN_ACT_PREFERENCES, Context.MODE_PRIVATE)
        var savedLayout = prefs?.getInt(MainActivity.PreferencesConstants.SONGS_LAYOUT_KEY, -1) ?: -1

        //No config
        if(savedLayout == -1) {
            prefs?.edit().apply {
                this!!.putInt(MainActivity.PreferencesConstants.SONGS_LAYOUT_KEY, 1)
            }?.apply()

            savedLayout = 1
        }

        if(currentLayout == savedLayout) return

        currentLayout = if(savedLayout !in 1..3) 1
                        else savedLayout

        prefs?.edit().apply {
            this!!.putInt(MainActivity.PreferencesConstants.SONGS_LAYOUT_KEY, currentLayout)
        }?.apply()

        val adapter: SongArrayAdapter

        val manager: LayoutManager
        val layoutRes: Int
        when(currentLayout){
            FULL_WIDTH_LAYOUT -> {
                manager = LinearLayoutManager(activity)
                layoutRes = R.layout.item_song_list_view1
            }
            HORIZONTAL_CARD_LAYOUT -> {
                manager = LinearLayoutManager(activity)
                layoutRes = R.layout.item_song_list_view2
            }
            VERTICAL_CARD_LAYOUT -> {
                manager = GridLayoutManager(activity, 2)
                layoutRes = R.layout.item_song_list_view3
            }
            else -> {
                manager = LinearLayoutManager(activity)
                layoutRes = R.layout.item_song_list_view1
                currentLayout = 1
                prefs?.edit().apply {
                    this!!.putInt(MainActivity.PreferencesConstants.SONGS_LAYOUT_KEY, currentLayout)
                }?.apply()
            }
        }

        adapter = SongArrayAdapter.create(activity as Activity, mainSongList.getList(), childFragmentManager, layoutRes)
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter

        mustChangeStyle = true
    }

    override fun onStart() {
        super.onStart()
        if(listCreated)
            checkLayoutAndSetUpRecyclerView()

        checkAndSetUpStyle()
    }

    /**
     * Checks whether the current style has been changed. If so, the new style is set
     */
    private fun checkAndSetUpStyle() {
        val prefs = requireContext().getSharedPreferences(GlobalPreferencesConstants.MAIN_ACT_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
        var version = prefs.getInt(MainActivity.PreferencesConstants.SONGS_STYLE_VERSION, 0)

        if(version == 0) {
            createStylePreferences(prefs)
            version = prefs.getInt(MainActivity.PreferencesConstants.SONGS_STYLE_VERSION, 0)
        }

        if(!mustChangeStyle && version == currentStyleVersion) return

        currentStyleVersion = version

        prefs.apply {
            val constants = MainActivity.PreferencesConstants
            recyclerView.backgroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.SONGS_ITEMS_CONTAINER_BG_KEY,
                    requireContext().getColor(R.color.default_layout_bg)
                )
            )
        }
        //TODO
        //Get pos and scroll to it
        recyclerView.adapter = recyclerView.adapter

        mustChangeStyle = false
    }

    /**
     * Creates the default style for the view
     * @param preferences [SharedPreferences] where the style will be stored
     */
    private fun createStylePreferences(preferences: SharedPreferences) {
        preferences.edit().apply {
            val constants = MainActivity.PreferencesConstants
            putInt(constants.SONGS_ITEMS_CONTAINER_BG_KEY, requireContext().getColor(R.color.default_layout_bg))
            putInt(constants.SONGS_ITEM_BG_KEY, requireContext().getColor(R.color.default_layout_bg))
            putInt(constants.SONGS_ITEM_IMG_BG_KEY, requireContext().getColor(R.color.default_image_placeholder_bg))
            putInt(constants.SONGS_ITEM_IMG_FG_KEY, requireContext().getColor(R.color.default_image_placeholder_fg))
            putInt(constants.SONGS_ITEM_TEXT_KEY, requireContext().getColor(R.color.default_text_color))
            putInt(constants.SONGS_ITEM_BTN_BG_KEY, requireContext().getColor(R.color.default_buttons_bg))
            putInt(constants.SONGS_ITEM_BTN_FG_KEY, requireContext().getColor(R.color.default_buttons_fg))
            putInt(constants.SONGS_STYLE_VERSION, 1)
        }.apply()
    }

    /**
     * Loads all the music files info in one [SongList] (main song list)
     */
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
                // Get values of columns for a given audio file.
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

    /**
     * Shows permission explanation text. It is used when permissions are not granted and music can not be loaded
     * to the UI. It informs the user that permissions are denied
     */
    private fun showPermissionExplanation(){
        view?.findViewById<TextView>(R.id.explanation_text)?.text = "No granted permissions"
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SongListView()

        /**
         * Constant which refers to available layout 1
         */
        private const val FULL_WIDTH_LAYOUT = 1
        /**
         * Constant which refers to available layout 2
         */
        private const val HORIZONTAL_CARD_LAYOUT = 2
        /**
         * Constant which refers to available layout 3
         */
        private const val VERTICAL_CARD_LAYOUT = 3

    }

    /**
     * Returns the title of the fragment
     * @return title of the fragment (used in [TabLayout])
     */
    override fun toString(): String = title
}