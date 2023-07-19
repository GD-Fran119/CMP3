package com.example.databaseStuff

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


data class SongPlaylistRelationData(
    @Embedded var playlist: PlaylistEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "path",
        associateBy = Junction(SongPlaylistRelation::class)
    )
    var songs: List<SongEntity>
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(PlaylistEntity::class.java.classLoader)!!,
        parcel.createTypedArrayList(SongEntity)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(playlist, flags)
        parcel.writeTypedList(songs)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SongPlaylistRelationData> {
        override fun createFromParcel(parcel: Parcel): SongPlaylistRelationData {
            return SongPlaylistRelationData(parcel)
        }

        override fun newArray(size: Int): Array<SongPlaylistRelationData?> {
            return arrayOfNulls(size)
        }
    }
}

