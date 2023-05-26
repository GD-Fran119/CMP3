class Player private constructor(){
    private var songList : SongList? = null
    private var currentSong : Song? = null
    private var playMode : PlayMode = PlayMode.LIST_LOOP
    private var currentPos : UInt = 0.toUInt()
    private var playState = false

    companion object {
        val instance: Player by lazy{
            Player()
        }
    }

    public fun pause(){playState = false}
    public fun play(){playState = true}
    public fun setPlayMode(newPlayMode: PlayMode){playMode = newPlayMode}
    public fun setList(newList: SongList){songList = newList}
    public fun next(){}
    public fun previous(){}
    public fun setTime(time: UInt){}
    public fun setCurrentSong(pos: UInt){
        if (pos < songList!!.getListSize()) {
            currentSong = songList!!.getSong(pos)
            currentPos = pos
        }
    }

    public fun getCurrentSong(): Song?{return currentSong}

}