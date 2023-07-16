package com.example.songsAndPlaylists

import android.util.Log
import com.example.databaseStuff.SongEntity
import java.math.RoundingMode
import java.text.DecimalFormat

class Song(
    val title: String, val artist: String,
    val album: String, val duration: UInt,
    val path: String, private val fileSize: Int,
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

    private class DurationFormatter{
        companion object{
            fun format(time: Long): String{
                val sec = (time / 1000) % 60
                val min = (time / 60000) % 60
                val hour = time / 3600000

                var formatted = if(sec < 10) ":0$sec" else sec.toString()
                formatted = (if(min < 10) "0$min" else min.toString()) + formatted
                formatted = when{
                    hour == 0L -> formatted
                    hour < 10L -> "0$hour:$formatted"
                    else -> "$hour:$formatted"
                }

                return formatted
            }
        }
    }

    fun getDuration(): String = DurationFormatter.format(duration.toLong())

    fun toSongEntity() = SongEntity(path, title, artist, album, duration.toInt(), fileSize, lyricsPath)
}