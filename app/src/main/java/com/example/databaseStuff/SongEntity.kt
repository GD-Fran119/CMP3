package com.example.databaseStuff

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Class that defines the Songs table schema in the database
 * @param path file path that acts like primary key
 * @param title title of the song
 * @param artist artist of the song
 * @param album album of the song
 * @param duration duration of the song in milliseconds
 * @param size file size in Bytes
 * @param lyricsPath path of this song's lyrics file
 */
@Entity(indices = [Index(value = ["path"])])
data class SongEntity(
    @PrimaryKey var path: String,
    var title: String,
    var artist: String,
    var album: String,
    var duration: Int,
    var size: Int,
    var lyricsPath: String?
):Parcelable{

    /**
     * Constructor from [Parcel]
     * @param parcel parcel from where to read song data and create [SongEntity] instance
     */
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(path)
        parcel.writeString(title)
        parcel.writeString(artist)
        parcel.writeString(album)
        parcel.writeInt(duration)
        parcel.writeInt(size)
        parcel.writeString(lyricsPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SongEntity> {
        override fun createFromParcel(parcel: Parcel): SongEntity {
            return SongEntity(parcel)
        }

        override fun newArray(size: Int): Array<SongEntity?> {
            return arrayOfNulls(size)
        }
    }
}
