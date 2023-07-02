package com.example.databaseStuff

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["id"])])
data class PlaylistEntity(
    var name: String,
    var date: String
){
    @PrimaryKey(autoGenerate=true) var id : Int = 0
}
