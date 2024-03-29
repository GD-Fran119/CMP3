package com.example.cmp3

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.config.GlobalPreferencesConstants
import com.example.databaseStuff.AppDatabase
import com.example.databaseStuff.SongPlaylistRelationData
import com.example.dialogs.PlaylistCreationDialog
import com.example.recyclerviewAdapters.PlaylistArrayAdapter
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Fragment to display all the playlists th user has created
 */
class PlaylistListView : Fragment() {

    private val title = "Playlists"
    //Job to get track of the UI info updating
    private var infoUpdateJob : Job? = null
    private lateinit var recyclerView: RecyclerView
    private var currentLayout = -1
    private var currentStyleVersion = -1
    private var mustChangeStyle = false
    private lateinit var adapter: PlaylistArrayAdapter
    private lateinit var createPlaylistButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_playlist_list_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.playlistListView)

        createPlaylistButton = view.findViewById(R.id.playlistsFAB)

        val permission = if(Build.VERSION.SDK_INT < 33)
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        else
                            Manifest.permission.READ_MEDIA_AUDIO

        val res = requireContext().checkCallingOrSelfPermission(permission)
        if(res == PackageManager.PERMISSION_GRANTED) {
            createPlaylistButton.setOnClickListener {
                PlaylistCreationDialog().show(childFragmentManager, "Create playlist")
            }
        }
    }

    /**
     * Retrieves the playlists info from the database
     * and displays it. If [Manifest.permission.READ_MEDIA_AUDIO], for SDK > 33, or [Manifest.permission.READ_EXTERNAL_STORAGE], for the rest,
     * permission is not granted the fragment displays a message with this issue.
     */
    private suspend fun loadPlaylists() {
        val db = AppDatabase.getInstance(activity as Context)

        val dao = db.playlistDao()

        val playlists = dao.getAllPlaylists()

        //Clean database
        //Development requirement :)
        /*
        val playlistEntities = playlists.map { it.playlist.id }.toIntArray()
        dao.deletePlaylist(*playlistEntities)
        */

        withContext(Dispatchers.Main) {
            val permission = if(Build.VERSION.SDK_INT < 33)
                Manifest.permission.READ_EXTERNAL_STORAGE
            else
                Manifest.permission.READ_MEDIA_AUDIO

            val res = requireContext().checkCallingOrSelfPermission(permission)
            if(res != PackageManager.PERMISSION_GRANTED) {
                view?.findViewById<TextView>(R.id.nullPlaylistInfo)?.text =
                    "No granted permissions"
                recyclerView.adapter = null
            }
            else if (playlists.isEmpty()) {
                view?.findViewById<TextView>(R.id.nullPlaylistInfo)?.text =
                    "There are no playlists created yet"
                recyclerView.adapter = null
            } else {
                view?.findViewById<TextView>(R.id.nullPlaylistInfo)?.text = ""
                setRecyclerViewLayout(playlists)
            }
        }

    }

    /**
     * Establishes how the [RecyclerView] will display the playlists items, according to the layout settings
     */
    private fun setRecyclerViewLayout(playlists: List<SongPlaylistRelationData>) {
        val prefs = activity?.getSharedPreferences(GlobalPreferencesConstants.MAIN_ACT_PREFERENCES, Context.MODE_PRIVATE)
        var savedLayout = prefs?.getInt(MainActivity.PreferencesConstants.PLAYLISTS_LAYOUT_KEY, -1)

        //No config
        if(savedLayout == -1) {
            prefs?.edit().apply {
                this!!.putInt(MainActivity.PreferencesConstants.PLAYLISTS_LAYOUT_KEY, 1)
            }?.apply()

            savedLayout = 1
        }

        if(currentLayout != savedLayout){
            currentLayout = if(savedLayout !in 1..3) 1
                            else savedLayout!!

            prefs?.edit().apply {
                this!!.putInt(MainActivity.PreferencesConstants.PLAYLISTS_LAYOUT_KEY, currentLayout)
            }?.apply()

            val recyclerView = view?.findViewById<RecyclerView>(R.id.playlistListView)
            recyclerView?.setHasFixedSize(true)

            val manager: RecyclerView.LayoutManager
            when(currentLayout){
                FULL_WIDTH_LAYOUT -> {
                    manager = LinearLayoutManager(activity)
                    adapter = PlaylistArrayAdapter.create(activity as Activity, playlists, R.layout.playlists_item_view1)
                }
                CARD_LAYOUT -> {
                    manager = GridLayoutManager(activity, 2)
                    adapter = PlaylistArrayAdapter.create(activity as Activity, playlists, R.layout.playlists_item_view2)
                }
                ROUNDED_IMAGE_LAYOUT -> {
                    manager = GridLayoutManager(activity, 2)
                    adapter = PlaylistArrayAdapter.create(activity as Activity, playlists, R.layout.playlists_item_view3)
                }
                else -> {
                    manager = LinearLayoutManager(activity)
                    adapter = PlaylistArrayAdapter.create(activity as Activity, playlists, R.layout.playlists_item_view1)
                    currentLayout = 1
                    prefs?.edit().apply {
                        this!!.putInt(MainActivity.PreferencesConstants.PLAYLISTS_LAYOUT_KEY, currentLayout)
                    }?.apply()
                }
            }

            recyclerView?.layoutManager = manager
            recyclerView?.adapter = adapter
        }
        else{
            when(currentLayout){
                FULL_WIDTH_LAYOUT -> {
                    adapter = PlaylistArrayAdapter.create(activity as Activity, playlists, R.layout.playlists_item_view1)
                }
                CARD_LAYOUT -> {
                    adapter = PlaylistArrayAdapter.create(activity as Activity, playlists, R.layout.playlists_item_view2)
                }
                ROUNDED_IMAGE_LAYOUT -> {
                    adapter = PlaylistArrayAdapter.create(activity as Activity, playlists, R.layout.playlists_item_view3)
                }
                else -> {
                    adapter = PlaylistArrayAdapter.create(activity as Activity, playlists, R.layout.playlists_item_view1)
                    currentLayout = 1
                    prefs?.edit().apply {
                        this!!.putInt(MainActivity.PreferencesConstants.PLAYLISTS_LAYOUT_KEY, currentLayout)
                    }?.apply()
                }
            }
            recyclerView.adapter = adapter
            mustChangeStyle = true
        }
    }

    override fun onStart() {
        super.onStart()

        checkAndSetUpStyle()
    }

    override fun onResume() {
        super.onResume()
        //Check permissions
        val permission = if(Build.VERSION.SDK_INT < 33)
            Manifest.permission.READ_EXTERNAL_STORAGE
        else
            Manifest.permission.READ_MEDIA_AUDIO

        //Update UI
        val res = requireContext().checkCallingOrSelfPermission(permission)
        if(res == PackageManager.PERMISSION_GRANTED) {
            infoUpdateJob = CoroutineScope(Dispatchers.Default).launch {
                loadPlaylists()
            }
        }
    }

    /**
     * Checks whether the current style has been changed. If so, the new style is set
     */
    private fun checkAndSetUpStyle() {
        val prefs = requireContext().getSharedPreferences(GlobalPreferencesConstants.MAIN_ACT_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
        var version = prefs.getInt(MainActivity.PreferencesConstants.PLAYLIST_STYLE_VERSION, 0)

        if(version == 0) {
            createStylePreferences(prefs)
            version = prefs.getInt(MainActivity.PreferencesConstants.PLAYLIST_STYLE_VERSION, 0)
        }

        if(!mustChangeStyle && version == currentStyleVersion) return

        currentStyleVersion = version

        prefs.apply {
            val constants = MainActivity.PreferencesConstants
            recyclerView.backgroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.PLAYLIST_ITEMS_CONTAINER_BG_KEY,
                    requireContext().getColor(R.color.default_layout_bg)
                )
            )

            createPlaylistButton.backgroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.CREATE_PLAYLIST_BTN_BG_KEY,
                    requireContext().getColor(R.color.default_buttons_bg)
                )
            )

            createPlaylistButton.foregroundTintList = ColorStateList.valueOf(
                getInt(
                    constants.CREATE_PLAYLIST_BTN_FG_KEY,
                    requireContext().getColor(R.color.default_buttons_fg)
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
            putInt(constants.PLAYLIST_ITEMS_CONTAINER_BG_KEY, requireContext().getColor(R.color.default_layout_bg))
            putInt(constants.PLAYLIST_ITEM_BG_KEY, requireContext().getColor(R.color.default_layout_bg))
            putInt(constants.PLAYLIST_ITEM_IMG_BG_KEY, requireContext().getColor(R.color.default_image_placeholder_bg))
            putInt(constants.PLAYLIST_ITEM_IMG_FG_KEY, requireContext().getColor(R.color.default_image_placeholder_fg))
            putInt(constants.PLAYLIST_ITEM_TEXT_KEY, requireContext().getColor(R.color.default_text_color))
            putInt(constants.PLAYLIST_ITEM_ICON_KEY, requireContext().getColor(R.color.default_icon_color))
            putInt(constants.PLAYLIST_STYLE_VERSION, 1)
        }.apply()
    }

    override fun onPause() {
        super.onPause()
        infoUpdateJob?.cancel()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment PlaylistListView.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            PlaylistListView()

        /**
         * Constant which refers to available layout 1
         */
        private const val FULL_WIDTH_LAYOUT = 1
        /**
         * Constant which refers to available layout 2
         */
        private const val CARD_LAYOUT = 2
        /**
         * Constant which refers to available layout 3
         */
        private const val ROUNDED_IMAGE_LAYOUT = 3
    }

    override fun toString(): String {
        return title
    }

}