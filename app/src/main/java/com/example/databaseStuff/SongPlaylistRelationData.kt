package com.example.databaseStuff

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class SongPlaylistRelationData(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "songId",
        associateBy = Junction(SongPlaylistRelation::class)
    )
    val songs: List<SongEntity>
)

