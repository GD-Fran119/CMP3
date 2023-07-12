package com.example.songsAndPlaylists

class MainListHolder private constructor(){
    companion object{

        private var mainSongList: SongList? = null

        fun setMainList(mainList: SongList){
            if(mainSongList == null)
                mainSongList = mainList
        }
        fun getMainList() : SongList = mainSongList!!
    }
}