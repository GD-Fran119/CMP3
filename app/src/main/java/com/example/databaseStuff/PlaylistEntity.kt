package com.example.databaseStuff

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Class that defines the Playlists table schema in the database
 * @param name name of the playlist
 * @param date date the playlist was created
 */
@Entity(indices = [Index(value = ["id"])])
data class PlaylistEntity(
    var name: String,
    var date: String
): Parcelable {
    /**
     * Id of the playlist in the database
     */
    @PrimaryKey(autoGenerate=true) var id : Int = 0

    /**
     * Constructor from [Parcel]
     * @param parcel parcel from where to read playlist data and create [PlaylistEntity] instance
     */
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    ) {
        id = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(date)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlaylistEntity> {
        override fun createFromParcel(parcel: Parcel): PlaylistEntity {
            return PlaylistEntity(parcel)
        }

        override fun newArray(size: Int): Array<PlaylistEntity?> {
            return arrayOfNulls(size)
        }
    }
}
