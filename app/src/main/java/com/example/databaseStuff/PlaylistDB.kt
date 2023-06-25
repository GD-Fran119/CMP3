package com.example.databaseStuff

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SongEntity::class, PlaylistEntity::class, SongPlaylistRelation::class, SongPlaylistRelationData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDAO
}