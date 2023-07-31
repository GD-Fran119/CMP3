package com.example.playerStuff
import com.example.songsAndPlaylists.Song
import com.example.songsAndPlaylists.SongList
import android.media.MediaPlayer
import kotlin.random.Random
import kotlin.random.nextUInt

class Player private constructor(){
    private var songList : SongList? = null
    private var currentSong : Song? = null
    private var playMode : PlayMode = PlayMode.LIST_LOOP
    private var currentPos : UInt = 0.toUInt()
    private var mediaPLayer : MediaPlayer = MediaPlayer()
    private var isPlaying = false
    var onSongChangedListener : OnSongChangedListener? = null
        set(value){
            field = value
            field?.listen()
        }

    private var isPlayerAvailable = false

    init{
        mediaPLayer.reset()
    }

    companion object {
        val instance: Player by lazy{
            Player()
        }
    }

    fun changePlayMode(){
        playMode = when(playMode){
            PlayMode.RANDOM -> PlayMode.SONG_LOOP
            PlayMode.SONG_LOOP -> PlayMode.LIST_LOOP
            PlayMode.LIST_LOOP -> PlayMode.RANDOM
        }
    }
    fun setRandomLoop(){
        playMode = PlayMode.RANDOM
    }

    fun setListLoop(){
        playMode = PlayMode.LIST_LOOP
    }

    fun setSongLoop(){
        playMode = PlayMode.SONG_LOOP
    }

    fun isListLoop(): Boolean{
        return playMode == PlayMode.LIST_LOOP
    }
    fun isSongLoop(): Boolean{
        return playMode == PlayMode.SONG_LOOP
    }
    fun isRandomLoop(): Boolean{
        return playMode == PlayMode.RANDOM
    }
    fun isPlayingSong() = isPlaying
    fun pause(){
        if(mediaPLayer.isPlaying)
            mediaPLayer.pause()
        isPlaying = false
    }
    fun play(){
        try {
            mediaPLayer.start()
            isPlaying = true
        }catch (_: Exception){}
    }
    fun listSize(): UInt{
        if(songList == null) return 0.toUInt()

        return songList!!.getListSize()
    }
    fun setList(newList: SongList){
        isPlayerAvailable = false
        songList = newList
        mediaPLayer.reset()
        setCurrentSong(0.toUInt())
        isPlayerAvailable = true
    }

    fun getList() = songList
    fun playNext(){
        isPlayerAvailable = false
        val nextSongPos = when(playMode){
            PlayMode.LIST_LOOP -> (currentPos + 1.toUInt()) % songList!!.getListSize()
            PlayMode.SONG_LOOP -> currentPos
            PlayMode.RANDOM -> Random.nextUInt(songList!!.getListSize())
        }
        setCurrentSongAndPLay(nextSongPos)
        isPlayerAvailable = true
    }
    fun playPrevious(){
        isPlayerAvailable = false
        val nextSongPos = when(playMode){
            PlayMode.LIST_LOOP -> {
                                        if(currentPos > 0.toUInt()) {
                                            currentPos - 1.toUInt()
                                        } else {
                                            songList!!.getListSize() - 1.toUInt()
                                        }
                                    }
            PlayMode.SONG_LOOP -> currentPos
            PlayMode.RANDOM -> Random.nextUInt(0.toUInt(), songList!!.getListSize())
        }
        setCurrentSongAndPLay(nextSongPos)
        isPlayerAvailable = true
    }
    fun setTime(time: UInt){
        try{
            mediaPLayer.seekTo(time.toInt())
        }catch (_:Exception){}
    }

    fun setCurrentSong(pos: UInt){

        isPlayerAvailable = false

        if (pos < songList!!.getListSize()) {
            currentSong = songList!!.getSong(pos)
            currentPos = pos

            mediaPLayer.reset()
            mediaPLayer.setDataSource(currentSong!!.path)
            mediaPLayer.prepareAsync()
            mediaPLayer.setOnPreparedListener{
                //Notify change
                onSongChangedListener?.listen()
                isPlayerAvailable = true
            }
            mediaPLayer.setOnCompletionListener {
                playNext()
            }
        }

    }

    fun isAvailableProgress() = isPlayerAvailable

    fun getCurrentPos() = currentPos
    fun setCurrentSongAndPLay(pos: UInt){
        isPlayerAvailable = false
        setCurrentSong(pos)
        if(currentSong != null) {
            mediaPLayer.setOnPreparedListener {
                mediaPLayer.start()
                //Notify change
                onSongChangedListener?.listen()
                isPlayerAvailable = true
                isPlaying = true
            }
        }
    }

    fun getCurrentSong(): Song? = currentSong

    fun getCurrentSongDuration() = currentSong!!.duration

    fun getCurrentSongProgress(): Int{
        return try {
            mediaPLayer.currentPosition
        }catch (_: Exception){
            0
        }
    }

    interface OnSongChangedListener{
        fun listen()
    }

}