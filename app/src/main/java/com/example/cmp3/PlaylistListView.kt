package com.example.cmp3

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.config.MainActivityPreferencesConstants
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


class PlaylistListView : Fragment() {

    private val title = "Playlists"
    private var infoUpdateJob : Job? = null
    private lateinit var recyclerView: RecyclerView
    private var currentLayout = -1
    private lateinit var adapter: PlaylistArrayAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_playlist_list_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.playlistListView)

        val permission = if(Build.VERSION.SDK_INT < 33)
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        else
                            Manifest.permission.READ_MEDIA_AUDIO

        val res = requireContext().checkCallingOrSelfPermission(permission)
        if(res == PackageManager.PERMISSION_GRANTED) {
            view.findViewById<MaterialButton>(R.id.playlistsFAB).setOnClickListener {
                PlaylistCreationDialog().show(childFragmentManager, "Create playlist")
            }
        }
    }

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

    private fun setRecyclerViewLayout(playlists: List<SongPlaylistRelationData>) {
        val prefs = activity?.getSharedPreferences(ChangeLayoutActivity.MAIN_ACT_PREFERENCES, Context.MODE_PRIVATE)
        var savedLayout = prefs?.getInt(MainActivityPreferencesConstants.PLAYLISTS_LAYOUT_KEY, -1)

        //No config
        if(savedLayout == -1) {
            prefs?.edit().apply {
                this!!.putInt(MainActivityPreferencesConstants.PLAYLISTS_LAYOUT_KEY, 1)
            }?.apply()

            savedLayout = 1
        }

        if(currentLayout != savedLayout){
            currentLayout = if(savedLayout !in 1..3) 1
                            else savedLayout!!

            prefs?.edit().apply {
                this!!.putInt(MainActivityPreferencesConstants.PLAYLISTS_LAYOUT_KEY, currentLayout)
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
                ROUNDED_CARD_LAYOUT -> {
                    manager = GridLayoutManager(activity, 2)
                    adapter = PlaylistArrayAdapter.create(activity as Activity, playlists, R.layout.playlists_item_view3)
                }
                else -> {
                    manager = LinearLayoutManager(activity)
                    adapter = PlaylistArrayAdapter.create(activity as Activity, playlists, R.layout.playlists_item_view1)
                    currentLayout = 1
                    prefs?.edit().apply {
                        this!!.putInt(MainActivityPreferencesConstants.SONGS_LAYOUT_KEY, currentLayout)
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
                ROUNDED_CARD_LAYOUT -> {
                    adapter = PlaylistArrayAdapter.create(activity as Activity, playlists, R.layout.playlists_item_view3)
                }
                else -> {
                    adapter = PlaylistArrayAdapter.create(activity as Activity, playlists, R.layout.playlists_item_view1)
                    currentLayout = 1
                    prefs?.edit().apply {
                        this!!.putInt(MainActivityPreferencesConstants.SONGS_LAYOUT_KEY, currentLayout)
                    }?.apply()
                }
            }
            recyclerView.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        //TODO
        //Update view
        infoUpdateJob = CoroutineScope(Dispatchers.Default).launch {
            loadPlaylists()
        }
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

        private const val FULL_WIDTH_LAYOUT = 1
        private const val CARD_LAYOUT = 2
        private const val ROUNDED_CARD_LAYOUT = 3
    }

    override fun toString(): String {
        return title
    }

}