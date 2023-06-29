package com.example.databaseStuff

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SongPlaylistRelation::class, PlaylistEntity::class, SongEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDAO

    companion object{
        private var instance : AppDatabase? = null

        fun getInstance(c: Context): AppDatabase{
            return instance ?: synchronized(c) {
                val newInstance = Room.databaseBuilder(
                    c,
                    AppDatabase::class.java,
                    "database"
                ).build()
                // return instance
                instance = newInstance

                newInstance
            }
        }
    }

}