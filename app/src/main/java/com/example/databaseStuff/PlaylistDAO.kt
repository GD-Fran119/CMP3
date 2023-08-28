package com.example.databaseStuff

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

/**
 * DAO to access playlists database data
 */
@Dao
interface PlaylistDAO {

    /*****************/
    /* Songs methods */
    /*****************/

    /**
     * Inserts songs into database
     * @param songs songs to insert on the database. If there is a song that already exists, the song is not inserted again
     * @return [LongArray] with the row numbers of those songs that were successfully inserted
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSongs(vararg songs: SongEntity): LongArray

    /**
     * Retrieves all the songs the database stores
     * @return a [List] with all the songs the database saves
     */
    @Query("SELECT * FROM songentity")
    fun getSongs(): List<SongEntity>

    /**
     * Deletes the songs specified by their paths. If the song was added to a playlist, this song is deleted from the playlist too.
     * @param path paths of the songs that are going to be deleted
     * @return number of the songs that were successfully deleted
     * @see [SongPlaylistRelation]
     */
    @Query("DELETE FROM songentity where path in (:path)")
    fun deleteSongs(vararg path: String) : Int

    /*********************/
    /* Playlists methods */
    /*********************/

    /**
     * Inserts a playlist into database
     * @param playlist playlist to insert on the database
     * @return [Long] id of the playlist if inserted, 0 otherwise
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPlaylist(playlist: PlaylistEntity) : Long

    /**
     * Updates the name of the specified playlist by its id
     * @param id id of the playlist to update
     * @param newName new name of the playlist
     * @return [Int] of the affected rows
     */
    @Query("UPDATE playlistentity set name = :newName where id = :id")
    fun updatePlaylistName(id: Int, newName: String) : Int

    /**
     * Deletes the playlists specified by their ids. If the playlist had songs added, the songs are deleted from the playlist
     * @param id ids of the playlists to remove
     * @return number of the songs that were successfully deleted
     * @see [SongPlaylistRelation]
     */
    @Query("DELETE FROM playlistentity where id in (:id)")
    fun deletePlaylist(vararg id: Int) : Int

    /**
     * Retrieves the info of all playlists stored in the database
     * @return [List] of [PlaylistEntity] with all playlists in the database
     */
    @Query("Select * from playlistentity")
    fun getPlaylistsInfo() : List<PlaylistEntity>

    /********************/
    /* Relation methods */
    /********************/

    /**
     * Adds a song to a playlist
     * @param relation song and playlist to create new relation in the database
     * @return [Long] row number of the inserted relation, -1 if it was not inserted
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSongInPlaylist(relation: SongPlaylistRelation) : Long

    /**
     * Removes a song from a playlist
     * @param relation song and playlist relation to delete from the database
     * @return [Int] rows affected
     */
    @Delete
    fun deleteSongFromPlaylist(relation: SongPlaylistRelation): Int

    /**
     * Retrieves all the playlists and their songs stored in the database
     * @return [List] of [SongPlaylistRelationData] with all the playlist info, including the songs that the playlist is made up
     */
    @Transaction
    @Query("SELECT * FROM playlistentity")
    fun getAllPlaylists(): List<SongPlaylistRelationData>

    /**
     * Retrieves a playlist and their songs stored in the database
     * @param id id of the playlist whose data will be retrieved
     * @return [SongPlaylistRelationData] with all the songs the playlist has
     */
    @Transaction
    @Query("SELECT * FROM playlistentity where id = :id")
    fun getPlaylistSongs(id: Int): SongPlaylistRelationData?
}