package com.example.songsAndPlaylists

import androidx.media3.common.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class SongTest {

    private lateinit var song : Song
    private lateinit var song2 : Song

    @Before
    fun createSong(){
        val title = "title"
        val artist = "artist"
        val album = "album"
        //04:07
        val duration1 = 247824U
        //01:20:00
        val duration2 = 4800000U
        //5.7MB
        val size1 = 5948396
        //6.7MB
        val size2 = 6973691
        val path = "path"
        val lyrics : String? = null

        song = Song(title, artist, album, duration1, path, size1, lyrics)
        song2 = Song(title, artist, album, duration2, path, size2, lyrics)
    }

    @Test
    fun getSizeMB() {
        val sizeMB1 = song.getSizeMB()
        assertEquals(sizeMB1, 5.7f)

        val sizeMB2 = song2.getSizeMB()
        assertEquals(sizeMB2, 6.7f)
    }

    @Test
    fun getDuration() {
        val duration1 = song.getDuration()
        assertEquals(duration1, "04:07")

        val duration2 = song2.getDuration()
        assertEquals(duration2, "01:20:00")
    }
}