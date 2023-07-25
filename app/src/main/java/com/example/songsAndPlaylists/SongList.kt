package com.example.songsAndPlaylists

import android.os.Parcel
import android.os.Parcelable
import com.example.databaseStuff.PlaylistEntity
import com.example.databaseStuff.SongPlaylistRelationData

class SongList(val title: String, private var songs: MutableList<Song>, val creationDate: String): Parcelable {

    var id = -1
    private set

    var duration: UInt = 0u
        private set

    fun getDuration(): String = DurationFormatter.format(duration.toLong())

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

    constructor(playlist: SongPlaylistRelationData):
            this(playlist.playlist.name,
                playlist.songs.map{Song(it)}.toMutableList(),
                playlist.playlist.date) {

        id = playlist.playlist.id
    }

    private fun computeDuration(){
        duration = 0.toUInt()
        songs.forEach { s -> duration += s.duration }
    }

    fun addSong(newSong: Song){
        songs += newSong
    }

    fun getSong(pos: UInt): Song {
        return songs[pos.toInt()]
    }

    fun getList() = songs.toList()

    fun getListSize(): UInt{
        return songs.size.toUInt()
    }

    fun removeSong(pos:UInt){
        if(pos < songs.size.toUInt())
            songs.removeAt(pos.toInt())
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
}