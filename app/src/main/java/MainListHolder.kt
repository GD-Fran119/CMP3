class MainListHolder private constructor(){
    companion object{
        private var mainSonglist: SongList? = null

        fun setMainList(mainList: SongList){
            if(mainSonglist == null)
                mainSonglist = mainList
        }

        fun getMainList() = mainSonglist
    }
}