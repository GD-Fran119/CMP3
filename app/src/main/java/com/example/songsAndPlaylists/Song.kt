package com.example.songsAndPlaylists

import com.example.databaseStuff.SongEntity
import java.math.RoundingMode
import java.text.DecimalFormat

class Song(
    val title: String, val artist: String,
    val album: String, val duration: UInt,
    val path: String, val fileSize: Int,
    val lyricsPath: String?) {

    constructor(song: SongEntity) : this(song.title, song.artist, song.album, song.duration.toUInt(),
                                         song.path, song.size, song.lyricsPath)
    fun getSizeMB(): Float{
        //1024 * 1024 = 1048567
        val TAMANO_MB = 1048567.0f

        //1 decimal number
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.HALF_EVEN

        return df.format(fileSize.toFloat() / TAMANO_MB).toFloat()
    }

    fun toSongEntity() = SongEntity(path, title, artist, album, duration.toInt(), fileSize, lyricsPath)
}