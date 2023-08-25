package com.example.songsAndPlaylists

import android.os.Parcel
import android.os.Parcelable
import com.example.databaseStuff.SongEntity
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Class that represents a song
 * @param title song title
 * @param artist song artist
 * @param album song album
 * @param duration song duration in milliseconds
 * @param path song file path
 * @param fileSize file size in bytes
 * @param lyricsPath song lyrics file path
 */
class Song(
    val title: String, val artist: String,
    val album: String, val duration: UInt,
    val path: String, private val fileSize: Int,
    val lyricsPath: String?): Parcelable {


    /**
     * Constructor for [Parcel]
     */
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt().toUInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    /**
     * Constructor for [SongEntity]
     */
    constructor(song: SongEntity) : this(song.title, song.artist, song.album, song.duration.toUInt(),
                                         song.path, song.size, song.lyricsPath)

    /**
     * Formats the file size into megabytes
     * @return file size formatted to megabytes (E.g. "5.6", "10.2")
     */
    fun getSizeMB(): String {
        //1024 * 1024 = 1048567
        val TAMANO_MB = 1048567.0f

        //1 decimal number
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.HALF_EVEN

        return df.format(fileSize.toFloat() / TAMANO_MB)
    }


    /**
     * Formats the song duration
     * @return duration formatted like SSs, MM:SS or HH:MM:SS depending on the [duration]
     */
    fun getDuration(): String = DurationFormatter.format(duration.toLong())

    /**
     * Converts a [Song] into [SongEntity]
     * @return the song as [SongEntity]
     */
    fun toSongEntity() = SongEntity(path, title, artist, album, duration.toInt(), fileSize, lyricsPath)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(artist)
        parcel.writeString(album)
        parcel.writeInt(duration.toInt())
        parcel.writeString(path)
        parcel.writeInt(fileSize)
        parcel.writeString(lyricsPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean{
        if(other !is Song) return false
        return title == other.title &&
                artist == other.artist &&
                album == other.album &&
                duration == other.duration &&
                path == other.path &&
                fileSize == other.fileSize &&
                lyricsPath == other.lyricsPath
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + artist.hashCode()
        result = 31 * result + album.hashCode()
        result = 31 * result + duration.hashCode()
        result = 31 * result + path.hashCode()
        result = 31 * result + fileSize
        result = 31 * result + (lyricsPath?.hashCode() ?: 0)
        return result
    }

}