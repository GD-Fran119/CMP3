package com.example.songsAndPlaylists

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Song Test, checks if [getSizeMB()][Song.getSizeMB] and [getDuration()][Song.getDuration] operate properly.
 */
@RunWith(AndroidJUnit4::class)
class SongTest {

    private lateinit var song : Song
    private lateinit var song2 : Song
    //Used only for testing duration formatting
    private lateinit var song3 : Song
    private lateinit var song4 : Song

    /**
     * Creates initial songs to be used in the test.
     */
    @Before
    fun createSongs(){
        //04:07
        val duration1 = 247824U
        //01:20:00
        val duration2 = 4800000U
        //14s
        val duration3 = 14000U
        //4s
        val duration4 = 4000U
        //5.7MB
        val size1 = 5948396
        //6.7MB
        val size2 = 6973691
        //Other needed variables(not relevant for the test but for creating objects)
        val title = "title"
        val artist = "artist"
        val album = "album"
        val path = "path"
        val lyrics : String? = null

        song = Song(title, artist, album, duration1, path, size1, lyrics)

        song2 = Song(title, artist, album, duration2, path, size2, lyrics)

        song3 = Song(title, artist, album, duration3, path, size2, lyrics)

        song4 = Song(title, artist, album, duration4, path, size2, lyrics)
    }

    /**
     * Test for checking [getSizeMB()][Song.getSizeMB].
     */
    @Test
    fun getSizeMB() {
        val sizeMB1 = song.getSizeMB()
        assertEquals(sizeMB1, "5.7")

        val sizeMB2 = song2.getSizeMB()
        assertEquals(sizeMB2, "6.7")
    }

    /**
     * Test for checking [getSizeMB()][Song.getDuration].
     */
    @Test
    fun getDuration() {
        val duration1 = song.getDuration()
        assertEquals(duration1, "04:07")

        val duration2 = song2.getDuration()
        assertEquals(duration2, "01:20:00")

        val duration3 = song3.getDuration()
        assertEquals(duration3, "14s")

        val duration4 = song4.getDuration()
        assertEquals(duration4, "4s")
    }
}