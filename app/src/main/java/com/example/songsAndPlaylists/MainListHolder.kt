package com.example.songsAndPlaylists

/**
 * Class that holds the main list (the list with all songs in the user's device)
 */
class MainListHolder private constructor(){
    companion object{

        private var mainSongList: SongList? = null

        /**
         * Sets the main song list only once
         * @param mainList list to which set the main list
         */
        fun setMainList(mainList: SongList){
            if(mainSongList == null)
                mainSongList = mainList
        }

        /**
         * Getter for main list
         */
        fun getMainList() : SongList = mainSongList!!
    }
}