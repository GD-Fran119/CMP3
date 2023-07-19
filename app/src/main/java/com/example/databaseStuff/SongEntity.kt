package com.example.databaseStuff

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

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
