package com.example.config

import com.example.songsAndPlaylists.MainListHolder
import SongFinishedNotifier
import com.example.songsAndPlaylists.SongList
import android.content.Context
import com.example.cmp3.R
import com.example.playerStuff.Player

class CurrentSongAndPlaylistConfigSaver private constructor() {

    companion object{

        private const val prefsKey = "current_playlist"
        private const val l_name = "c_playlist_name"
        private const val s_path = "c_song_path"
        private const val l_pos = "c_song_pos"
        private const val l_mode = "c_playlist_mode"
        private const val mode_c_song = 0
        private const val mode_c_list = 1
        private const val mode_rand = 2


        fun savePlayList(context: Context){
            val prefs = context.getSharedPreferences(prefsKey, Context.MODE_PRIVATE)
            val player = Player.instance
            with (prefs.edit()) {
                val song = player.getCurrentSong()
                val playlist = player.getList()
                putString(l_name, playlist?.title)
                putString(s_path, song?.path)
                putInt(l_pos, Player.instance.getCurrentPos().toInt())

                var mode = 1
                if(player.isRandomLoop()) {
                    putInt(l_mode, mode_rand)
                    mode = 2
                }
                else if(player.isSongLoop()) {
                    putInt(l_mode, mode_c_song)
                    mode = 0
                }
                else
                    putInt(l_mode, mode_c_list)

                apply()
            }
        }

        fun loadPlayList(context: Context){
            //Establish current playlist and song
            //If song does not exist use pos = 0
            //If playlist does not exist use mainlist
            val prefs = context.getSharedPreferences(prefsKey, Context.MODE_PRIVATE)
            val listname = prefs.getString(l_name, "")!!
            
            if(listname.isEmpty())
                loadDefaultList()

            else{
                val mode = prefs.getInt(l_mode, mode_c_list)
                val songPath = prefs.getString(s_path, "")
                setupList(context, listname, mode, songPath!!)
            }

            SongFinishedNotifier.notifySongChanged()

        }

        private fun setupList(context: Context, listname: String, mode: Int, path: String){
            if(listname == context.getString(R.string.main_song_list)){
                val songlist = MainListHolder.getMainList()!!
                Player.instance.setList(songlist)
                when(mode){
                    mode_c_song -> while(!Player.instance.isSongLoop()) Player.instance.changePlayMode()
                    mode_rand -> while(!Player.instance.isRandomLoop()) Player.instance.changePlayMode()
                    //else -> default settings are mode == list loop
                }

                Player.instance.setCurrentSong(findIndexCurrentSongInList(songlist, path) ?: 0.toUInt())
            }
            else{
                //TODO
                //Load list that is not main list :)
            }
        }

        private fun loadDefaultList(){
            Player.instance.setList(MainListHolder.getMainList()!!)
            Player.instance.setCurrentSong(0.toUInt())
        }
        
        private fun findIndexCurrentSongInList(list: SongList, path: String): UInt?{
            for(i in 0.toUInt()..list.getListSize()){
                if(list.getSong(i).path == path)
                    return i
            }
            return null
        }
    }
}