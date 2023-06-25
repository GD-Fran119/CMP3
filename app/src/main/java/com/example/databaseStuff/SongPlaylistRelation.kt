package com.example.databaseStuff

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = ["songID", "playlistID"],
        foreignKeys = [ForeignKey(
    entity = SongEntity::class,
    parentColumns = arrayOf("path"),
    childColumns = arrayOf("songID"),
    onDelete = ForeignKey.CASCADE
), ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("playlistID"),
            onDelete = ForeignKey.CASCADE
        )]
)
data class SongPlaylistRelation(
    val songID: String,
    val playlistID: Int
)
