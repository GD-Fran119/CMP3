package com.example.config

import android.content.Context
import com.example.databaseStuff.AppDatabase
import com.example.playerStuff.Player
import com.example.songsAndPlaylists.MainListHolder
import com.example.songsAndPlaylists.Song
import com.example.songsAndPlaylists.SongList


class PlayerStateSaver {
    companion object{
        private const val SAVER_NAME = "PLAYER_STATE"
        private const val LIST_ID = "LIST_ID"
        private const val LIST_CURRENT_SONG = "LIST_CURRENT_SONG"
        private const val PLAY_MODE = "PLAY_MODE"
        private const val LIST_MODE = 0
        private const val CURRENT_SONG_MODE = 1
        private const val RANDOM_MODE = 2

        suspend fun saveState(context: Context){
            val prefs = context.getSharedPreferences(SAVER_NAME, Context.MODE_PRIVATE)
            prefs.edit().apply {
                val player = Player.instance
                player.getList()?.id?.let { putInt(LIST_ID, it) }
                player.getCurrentSong()?.path?.let { putString(LIST_CURRENT_SONG, it) }
                val mode = if(player.isListLoop()) LIST_MODE
                            else if(player.isSongLoop()) CURRENT_SONG_MODE
                            else RANDOM_MODE
                putInt(PLAY_MODE, mode)
            }.apply()
        }

        suspend fun loadState(context: Context){
            //Retrieve list
            //When establish main as current playlist:
                //-> No info available
                //-> Non-existing id
                //-> Empty playlist found
            //When establish custom playlist:
                //-> Info available and existing id
                //-> Non empty playlist

            //Establishing current playlist position
                //If song exists and in playlist -> find pos and set
                //If song exists but not in playlist -> use 0 as pos
                //If song does not exist -> use 0 as pos

            val prefs = context.getSharedPreferences(SAVER_NAME, Context.MODE_PRIVATE)
            val listID = prefs.getInt(LIST_ID, -1)
            val currentSong = prefs.getString(LIST_CURRENT_SONG, "")!!

            when(prefs.getInt(PLAY_MODE, LIST_MODE)){
                1 -> Player.instance.setSongLoop()
                2 -> Player.instance.setRandomLoop()
                else -> Player.instance.setListLoop()
            }

            if(listID < 1){
                //Set main as current
                //Use currentSong path
                val mainList = MainListHolder.getMainList()
                val pos = findSongInPlaylist(mainList.getList(), currentSong)
                setListAndSongAsCurrent(mainList, pos)
                return
            }

            val dao = AppDatabase.getInstance(context).playlistDao()
            val playlist = dao.getPlaylistSongs(listID)

            if(playlist == null || playlist.songs.isEmpty()){
                setListAndSongAsCurrent(MainListHolder.getMainList(), 0u)
                return
            }

            val list = SongList(playlist)
            val pos = findSongInPlaylist(list.getList(), currentSong)
            setListAndSongAsCurrent(list, pos)
        }

        private fun setListAndSongAsCurrent(list: SongList, pos: UInt){
            Player.instance.setList(list)
            Player.instance.setCurrentSong(pos)
        }

        private fun findSongInPlaylist(playlist:List<Song>, path: String): UInt{
            var pos = 0u
            for(i in playlist.indices){
                if(playlist[i].path == path) {
                    pos = i.toUInt()
                    break
                }
            }
            return pos
        }
    }
}