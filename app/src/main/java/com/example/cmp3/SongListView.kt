package com.example.cmp3

import CurrentSongAndPlaylistConfigSaver
import MainListHolder
import Song
import SongArrayAdapter
import SongList
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.media3.common.MimeTypes
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception
import kotlin.random.Random
import kotlin.random.nextUInt


class SongListView : Fragment() {

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                createListView()
                deleteExplanationText()
                enablePlayAllSongsClickAndSetSongsCount()
            } else {
                showPermissionExplanation()
            }
        }

    private val mainSongList = SongList("Main list", mutableListOf(), "")

    private val title = "Songs"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
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
                    enablePlayAllSongsClickAndSetSongsCount()
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
        view?.findViewById<TextView>(R.id.textoPrimeraTab)?.visibility = View.GONE
    }

    private fun enablePlayAllSongsClickAndSetSongsCount(){
        view?.findViewById<LinearLayout>(R.id.play_all_songs_container)?.setOnClickListener{
            val pos = Random.nextUInt(mainSongList.getListSize())
            Player.instance.setList(mainSongList)
            Player.instance.setCurrentSongAndPLay(pos)
            val intent = Intent(activity, PlayControlView::class.java)
            activity?.startActivity(intent)
        }

        view?.findViewById<TextView>(R.id.play_all_songs_number)?.text = "${mainSongList.getListSize()} songs"

    }
    private fun createListView(){

        val listView = view?.findViewById<RecyclerView>(R.id.mainSongListView)
        listView?.setHasFixedSize(true)

        findMusic()
        MainListHolder.setMainList(mainSongList)

        CurrentSongAndPlaylistConfigSaver.loadPlayList(activity as Context)

        val adapter = SongArrayAdapter.create(activity as Activity, mainSongList)

        listView?.adapter = adapter
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

        val selection = "${MediaStore.Audio.Media.MIME_TYPE} like ?"
        val selectionArgs = arrayOf(MimeTypes.AUDIO_MPEG)

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
        view?.findViewById<TextView>(R.id.textoPrimeraTab)?.text = "Permisos no garantizados"
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SongListView()
    }

    override fun toString(): String {
        return title
    }
}