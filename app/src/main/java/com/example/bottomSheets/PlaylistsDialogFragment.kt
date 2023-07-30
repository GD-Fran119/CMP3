package com.example.bottomSheets

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cmp3.R
import com.example.databaseStuff.AppDatabase
import com.example.databaseStuff.PlaylistEntity
import com.example.databaseStuff.SongPlaylistRelation
import com.example.recyclerviewAdapters.PlaylistBottomSheetAdapter
import com.example.songsAndPlaylists.Song
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistsDialogFragment(private val playlists: List<PlaylistEntity>, private val song: Song): BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_song_bottomsheet_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onItemClickListener = object: PlaylistBottomSheetAdapter.OnAddToPlaylistListener{
            override fun onSongAdded(path: String, id: Int){
                CoroutineScope(Dispatchers.Default).launch {
                    val dao = AppDatabase.getInstance(this@PlaylistsDialogFragment.requireContext()).playlistDao()
                    val relation = SongPlaylistRelation(path, id)
                    val inserted = dao.insertSongInPlaylist(relation)
                    withContext(Dispatchers.Main){
                        if(inserted == -1L)
                            Toast.makeText(context, "Song already added to playlist", Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(context, "Song added successfully", Toast.LENGTH_SHORT).show()
                    }
                }
                dismiss()
            }
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.bottom_box_playlist_recyclerview)
        recyclerView.adapter = PlaylistBottomSheetAdapter(playlists, song, onItemClickListener)
    }
}