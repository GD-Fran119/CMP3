package com.example.cmp3

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dialogs.PlaylistCreationDialog
import com.example.databaseStuff.AppDatabase
import com.example.databaseStuff.SongPlaylistRelationData
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_playlist_list_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.playlistListView)
        view.findViewById<MaterialButton>(R.id.playlistsFAB).setOnClickListener{
            PlaylistCreationDialog().show(childFragmentManager, "Create playlist")
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
            if (playlists.isEmpty()) {
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
        val adapter = PlaylistArrayAdapter.create(requireActivity(), playlists)
        val manager = GridLayoutManager(activity, 2)
        recyclerView.adapter = adapter
        //val manager = LinearLayoutManager(activity)
        recyclerView.layoutManager = manager
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
    }

    override fun toString(): String {
        return title
    }

}