package com.example

import android.os.Parcel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.databaseStuff.PlaylistEntity
import com.example.databaseStuff.SongEntity
import com.example.databaseStuff.SongPlaylistRelationData
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ParcelablesTest {

    private val names = listOf<String>("A", "B", "C", "D", "E")
    private val songs = mutableListOf<SongEntity>()
    private var playlist : PlaylistEntity? = null
    private var playlistData : SongPlaylistRelationData? = null

    @Before
    fun setUpData(){

        //Insert songs in
        for(element in names){
            songs += SongEntity("path", element,"artist",
                "album", duration=12345,size=12345,null)
        }

        playlist = PlaylistEntity("name", "date")
        playlist!!.id = 100

        playlistData = SongPlaylistRelationData(playlist!!,songs)

        assert(playlist != null)
        assert(songs.isNotEmpty())
        assert(songs.size == names.size)
        assert(playlistData != null)
    }

    @Test
    @Throws(Exception::class)
    fun createSongFromParcelable(){

        val parcel = Parcel.obtain()
        val song = songs[0]

        song.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)

        val checkSong = SongEntity(parcel)
        assert(checkSong == song)
    }

    @Test
    @Throws(Exception::class)
    fun createPlaylistEntityFromParcelable(){
        val parcel = Parcel.obtain()

        playlist!!.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)

        val checkPlaylist = PlaylistEntity(parcel)
        assert(checkPlaylist == playlist!!)
    }

    @Test
    @Throws(Exception::class)
    fun createPlaylistRelationDataFromParcelable(){
        val parcel = Parcel.obtain()

        playlistData!!.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)

        val checkData = SongPlaylistRelationData(parcel)
        assert(checkData == playlistData!!)

    }

}