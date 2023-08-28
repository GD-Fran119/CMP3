package com.example.databaseStuff

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index


/**
 * Class that defines the Relation between Songs and Playlists table schema in the database
 * @param path path of the song
 * @param id id of the playlist
 * @see [SongEntity]
 * @see [PlaylistEntity]
 */
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
