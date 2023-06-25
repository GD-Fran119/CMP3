package com.example.databaseStuff

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey val path: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: UInt,
    val size: Int,
    val lyricsPath: String?
)
