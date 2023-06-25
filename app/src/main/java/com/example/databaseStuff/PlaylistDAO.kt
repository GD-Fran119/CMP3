package com.example.databaseStuff

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistDAO {

    /*****************/
    /* Songs methods */
    /*****************/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSongs(vararg songs: SongEntity)

    //Deletes also all entities in relationship with PlaylistEntity due to onDelete = CASCADE
    @Query("DELETE FROM songs where path in (:path)")
    fun deleteSongs(vararg path: String)

    /*********************/
    /* Playlists methods */
    /*********************/

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPlaylist(playlist: PlaylistEntity)

    //Deletes also all entities in relationship with SongEntity due to onDelete = CASCADE
    @Query("DELETE FROM playlistentity where id in (:id)")
    fun deletePlaylist(vararg id: Int)

    /********************/
    /* Relation methods */
    /********************/

    @Query("INSERT INTO songplaylistrelation values(:song, :playlist)")
    fun insertSongInPlaylist(song: String, playlist: Int)

    @Delete
    fun deleteSongFromPlaylist(relation: SongPlaylistRelation)

    @Query("SELECT * FROM playlistentity")
    fun getAllPlaylists(): List<SongPlaylistRelationData>

    @Query("SELECT * FROM playlistentity where id = :id")
    fun getSongs(id: Int): SongPlaylistRelationData
}