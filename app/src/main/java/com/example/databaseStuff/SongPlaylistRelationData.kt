package com.example.databaseStuff

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * Class used for retrieving the whole data (info and songs) of a playlist
 * @param playlist playlist itself
 * @param songs songs the playlist has
 */
data class SongPlaylistRelationData(
    @Embedded var playlist: PlaylistEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "path",
        associateBy = Junction(SongPlaylistRelation::class)
    )
    var songs: List<SongEntity>
): Parcelable {
    /**
     * Constructor from [Parcel]
     * @param parcel parcel from where to read playlist data and create [SongPlaylistRelationData] instance
     */
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

