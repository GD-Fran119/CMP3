package com.example.songsAndPlaylists

import android.os.Parcel
import android.os.Parcelable
import com.example.databaseStuff.SongPlaylistRelationData

/**
 * Class that represents a playlist
 * @param title playlist title
 * @param songs songs the playlist have
 * @param creationDate date when the playlist was created
 */
class SongList(val title: String, private var songs: MutableList<Song>, val creationDate: String): Parcelable {

    /**
     * ID for database
     */
    var id = -1
    private set

    /**
     * Playlist total duration
     */
    var duration: UInt = 0u
        private set

    /**
     * Formats the playlist total duration
     * @return duration formatted like SSs, MM:SS or HH:MM:SS depending on the [duration]
     */
    fun getDuration(): String = DurationFormatter.format(duration.toLong())

    /**
     * Checks if the playlist is not empty
     * @return true if the playlist contains songs, false otherwise
     */
    fun isNotEmpty() = songs.isNotEmpty()

    /**
     * Checks if the playlist is empty
     * @return true if the playlist does not contain songs, false otherwise
     */
    fun isEmpty() = songs.isEmpty()

    /**
     * Constructor for [Parcel]
     */
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.createTypedArrayList(Song)!!.toMutableList(),
        parcel.readString()!!
    ) {
        id = parcel.readInt()
    }

    init {
        this.computeDuration()
    }

    /**
     * Constructor for [SongPlaylistRelationData]
     */
    constructor(playlist: SongPlaylistRelationData):
            this(playlist.playlist.name,
                playlist.songs.map{Song(it)}.toMutableList(),
                playlist.playlist.date) {

        id = playlist.playlist.id
    }

    /**
     * Calculates the duration of the whole playlist
     */
    private fun computeDuration(){
        duration = 0.toUInt()
        songs.forEach { s -> duration += s.duration }
    }

    /**
     * Adds a new song at the end of the playlist
     * @param newSong song to insert in the playlist
     */
    fun addSong(newSong: Song){
        songs += newSong
    }

    /**
     * Retrieves the song located at [pos]
     * @param pos position of the song in the playlist
     * @return playlist placed at [pos]
     */
    fun getSong(pos: UInt): Song {
        return songs[pos.toInt()]
    }

    /**
     * Getter for retrieving the songs in [List] format
     */
    fun getList() = songs.toList()

    /**
     * Getter for playlist songs count
     */
    fun getListSize(): UInt{
        return songs.size.toUInt()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeTypedList(songs)
        dest.writeString(creationDate)
        dest.writeInt(id)
    }

    companion object CREATOR : Parcelable.Creator<SongList> {
        override fun createFromParcel(parcel: Parcel): SongList {
            return SongList(parcel)
        }

        override fun newArray(size: Int): Array<SongList?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        if(other !is SongList) return false

        return id == other.id &&
                songs == other.songs &&
                title == other.title &&
                duration == other.duration &&
                creationDate == other.creationDate
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + songs.hashCode()
        result = 31 * result + creationDate.hashCode()
        result = 31 * result + id
        result = 31 * result + duration.hashCode()
        return result
    }
}