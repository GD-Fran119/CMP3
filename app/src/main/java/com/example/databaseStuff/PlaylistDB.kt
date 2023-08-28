package com.example.databaseStuff

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database class to store songs and playlists
 */
@Database(entities = [SongPlaylistRelation::class, PlaylistEntity::class, SongEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Retrieves a DAO for the database
     * @return [PlaylistDAO] to interact with the database
     */
    abstract fun playlistDao(): PlaylistDAO

    companion object{
        //Singleton variable
        private var instance : AppDatabase? = null

        /**
         * Factory method to get an instance of the [AppDatabase] class
         * @param c [Context] in which this instance will be used
         * @return [AppDatabase] instance
         */
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