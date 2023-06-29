package com.example.databaseStuff

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["id"])])
data class PlaylistEntity(
    @PrimaryKey(autoGenerate=true) var id : Int,
    var name: String,
    var date: String
)
