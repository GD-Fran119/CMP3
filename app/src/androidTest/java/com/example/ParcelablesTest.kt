package com.example

import android.os.Parcel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.databaseStuff.PlaylistEntity
import com.example.databaseStuff.SongEntity
import com.example.databaseStuff.SongPlaylistRelationData
import com.example.songsAndPlaylists.Song
import com.example.songsAndPlaylists.SongList
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import android.os.Parcelable


/**
 * Parcelables test, it checks if the [Parcelable] implementation
 * of [SongEntity], [PlaylistEntity], [SongPlaylistRelationData],
 * [Song] and [SongList] works properly.
 */
@RunWith(AndroidJUnit4::class)
class ParcelablesTest {

    private val names = listOf("A", "B", "C", "D", "E")
    private val songs = mutableListOf<SongEntity>()
    private var playlist : PlaylistEntity? = null
    private var playlistData : SongPlaylistRelationData? = null

    /**
     * Sets up the initial variables so the test can be run properly.
     * Creates 5 [SongEntity] and 1 [PlaylistEntity], and it inserts all songs in the playlist [SongPlaylistRelationData].
     */
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

    /**
     * Test for checking that a [SongEntity] object can be created from a [Parcelable].
     * @throws Exception
     */
    @Test
    @Throws(Exception::class)
    fun createSongEntityFromParcelable(){

        val parcel = Parcel.obtain()
        val song = songs[0]

        song.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)

        val checkSong = SongEntity(parcel)
        assert(checkSong == song)
    }

    /**
     * Test for checking that a [PlaylistEntity] object can be created from a [Parcelable].
     * @throws Exception
     */
    @Test
    @Throws(Exception::class)
    fun createPlaylistEntityFromParcelable(){
        val parcel = Parcel.obtain()

        playlist!!.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)

        val checkPlaylist = PlaylistEntity(parcel)
        assert(checkPlaylist == playlist!!)
    }

    /**
     * Test for checking that a [SongPlaylistRelationData] object can be created from a [Parcelable].
     * @throws Exception
     */
    @Test
    @Throws(Exception::class)
    fun createPlaylistRelationDataFromParcelable(){
        val parcel = Parcel.obtain()

        playlistData!!.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)

        val checkData = SongPlaylistRelationData(parcel)
        assert(checkData == playlistData!!)

    }

    /**
     * Test for checking that a [Song] object can be created from a [Parcelable]
     * @throws Exception
     */
    @Test
    @Throws(Exception::class)
    fun createSongFromParcelable(){
        val parcel = Parcel.obtain()

        val song = Song(songs[0])

        song.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)

        val checkData = Song(parcel)
        assert(checkData == song)

    }

    /**
     * Test for checking that a [SongList] object can be created from a [Parcelable]
     * @throws Exception
     */
    @Test
    @Throws(Exception::class)
    fun createSongListFromParcelable(){
        val parcel = Parcel.obtain()

        val list = SongList(playlistData!!)

        list.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)

        val checkData = SongList(parcel)
        assert(checkData == list)
    }
}