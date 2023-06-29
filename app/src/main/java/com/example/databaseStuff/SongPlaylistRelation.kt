package com.example.databaseStuff

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index


@Entity(primaryKeys = ["path", "id"],
    foreignKeys = [ForeignKey(
        entity = SongEntity::class,
        parentColumns = arrayOf("path"),
        childColumns = arrayOf("path"),
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = PlaylistEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("id"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["path"]), Index(value = ["id"])])
data class SongPlaylistRelation(
    var path: String,
    var id: Int
)
