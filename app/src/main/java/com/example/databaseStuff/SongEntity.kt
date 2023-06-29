package com.example.databaseStuff

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
)
