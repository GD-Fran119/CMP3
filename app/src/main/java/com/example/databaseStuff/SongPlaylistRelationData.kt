package com.example.databaseStuff

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
)

