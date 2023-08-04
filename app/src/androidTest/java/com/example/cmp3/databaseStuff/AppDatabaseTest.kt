package com.example.cmp3.databaseStuff

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.databaseStuff.AppDatabase
import com.example.databaseStuff.PlaylistDAO
import com.example.databaseStuff.PlaylistEntity
import com.example.databaseStuff.SongEntity
import com.example.databaseStuff.SongPlaylistRelation
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * [AppDatabase] test, checks if some operations over database can be done without errors.
 * These operations are:
 * - Insertion of a [SongEntity] in a [PlaylistEntity].
 * - Deletion of the [SongPlaylistRelation] when the [SongEntity] is removed from database.
 */
@RunWith(AndroidJUnit4::class)
class AppDatabaseTest{

    private lateinit var dao: PlaylistDAO
    private lateinit var db: AppDatabase

    /**
     * Creates database and establish dao for accessing data.
     */
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        dao = db.playlistDao()
    }

    /**
     * Closes database when test is finished.
     */
    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    /**
     * Test for inserting a song in a playlist and check the insertion runs correctly.
     * @throws Exception
     */
    @Test
    @Throws(Exception::class)
    fun insertSongInPlaylist() = runBlocking {
        //Create new song and Playlist
        val song = SongEntity("a", "a", "a", "a", 0, 0, null)
        val playlist = PlaylistEntity("a", "a")
        //Insert both song and playlist
        dao.insertSongs(song)
        dao.insertPlaylist(playlist)
        //Retrieve inserted playlist with its new id and songs
        val playlistSongs = dao.getAllPlaylists()[0]
        //Insert relation between song and playlist
        dao.insertSongInPlaylist(SongPlaylistRelation(song.path, playlistSongs.playlist.id))

        val songs = dao.getPlaylistSongs(playlistSongs.playlist.id)!!.songs
        //Check relation has been created properly
        assertTrue(songs.contains(song))
    }

    /**
     * Test for deleting a song from the database and check the deletion erases all the relations
     * the song had with any playlist.
     * @throws Exception
     */
    @Test
    @Throws(Exception::class)
    fun deleteSongRelationshipAfterInsertingSongAndPlaylistAndDeleteSong() = runBlocking {
        //Create new song and Playlist
        val song = SongEntity("a", "a", "a", "a", 0, 0, null)
        val playlist = PlaylistEntity("a", "a")
        //Insert both song and playlist
        dao.insertSongs(song)
        val id = dao.insertPlaylist(playlist)
        //Insert relation between song and playlist
        dao.insertSongInPlaylist(SongPlaylistRelation(song.path, id.toInt()))
        //Delete song
        dao.deleteSongs(song.path)

        val songs = dao.getPlaylistSongs(id.toInt())!!.songs
        //Check relation has been deleted too
        assertEquals(songs.size, 0)
    }
}