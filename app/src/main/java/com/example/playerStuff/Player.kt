package com.example.playerStuff
import com.example.songsAndPlaylists.Song
import com.example.songsAndPlaylists.SongList
import android.media.MediaPlayer
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * Class that represents the player itself. Wraps the native [MediaPlayer] class provided by Android.
 * This class applies the Singleton design pattern.
 */
class Player private constructor(){
    //Playlist to play
    private var songList : SongList? = null
    private var currentSong : Song? = null
    private var playMode : PlayMode = PlayMode.LIST_LOOP
    private var currentSongPos : UInt = 0.toUInt()
    private var mediaPLayer : MediaPlayer = MediaPlayer()
    private var isPlaying = false

    /**
     * Listener for current song change event
     */
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
        /**
         * Instance of the [Player].
         */
        val instance: Player by lazy{
            Player()
        }
    }

    /**
     * Establishes new play mode:
     * - [PlayMode.RANDOM] -> [PlayMode.SONG_LOOP]
     * - [PlayMode.SONG_LOOP] -> [PlayMode.LIST_LOOP]
     * - [PlayMode.LIST_LOOP] -> [PlayMode.RANDOM]
     */
    fun changePlayMode(){
        playMode = when(playMode){
            PlayMode.RANDOM -> PlayMode.SONG_LOOP
            PlayMode.SONG_LOOP -> PlayMode.LIST_LOOP
            PlayMode.LIST_LOOP -> PlayMode.RANDOM
        }
    }

    /**
     * Establishes [PlayMode.RANDOM] as play mode
     */
    fun setRandomLoop(){
        playMode = PlayMode.RANDOM
    }

    /**
     * Establishes [PlayMode.LIST_LOOP] as play mode
     */
    fun setListLoop(){
        playMode = PlayMode.LIST_LOOP
    }

    /**
     * Establishes [PlayMode.SONG_LOOP] as play mode
     */
    fun setSongLoop(){
        playMode = PlayMode.SONG_LOOP
    }

    /**
     * Checks if [PlayMode.LIST_LOOP] is set
     * @return true if [PlayMode.LIST_LOOP] is set, false otherwise
     */
    fun isListLoop(): Boolean{
        return playMode == PlayMode.LIST_LOOP
    }

    /**
     * Checks if [PlayMode.SONG_LOOP] is set
     * @return true if [PlayMode.SONG_LOOP] is set, false otherwise
     */
    fun isSongLoop(): Boolean{
        return playMode == PlayMode.SONG_LOOP
    }

    /**
     * Checks if [PlayMode.RANDOM] is set
     * @return true if [PlayMode.RANDOM] is set, false otherwise
     */
    fun isRandomLoop(): Boolean{
        return playMode == PlayMode.RANDOM
    }

    /**
     * Checks if the player is currently playing a song
     * @return true if the player is playing a song, false otherwise
     */
    fun isPlayingSong() = isPlaying

    /**
     * Pauses the player
     */
    fun pause(){
        if(mediaPLayer.isPlaying)
            mediaPLayer.pause()
        isPlaying = false
    }

    /**
     * Starts or resumes the player from where it was paused or set
     */
    fun play(){
        try {
            mediaPLayer.start()
            isPlaying = true
        }catch (_: Exception){}
    }

    /**
     * Method to get the playlist size
     * @return current playlist size, if no playlist set 0 is returned
     */
    fun listSize(): UInt{
        if(songList == null) return 0.toUInt()

        return songList!!.getListSize()
    }

    /**
     * Sets the current playlist. It establishes song at position 0 as the current song
     * @param newList list to be set
     */
    fun setList(newList: SongList){
        isPlayerAvailable = false
        songList = newList
        mediaPLayer.reset()
        setCurrentSong(0.toUInt())
        isPlayerAvailable = true
    }

    /**
     * Getter for the current playlist
     * @return playlist that is being played
     */
    fun getList() = songList

    /**
     * Skips to the next song chosen according the play mode set. It also starts playback
     */
    fun playNext(){
        isPlayerAvailable = false
        val nextSongPos = when(playMode){
            PlayMode.LIST_LOOP -> (currentSongPos + 1.toUInt()) % songList!!.getListSize()
            PlayMode.SONG_LOOP -> currentSongPos
            PlayMode.RANDOM -> Random.nextUInt(songList!!.getListSize())
        }
        setCurrentSongAndPLay(nextSongPos)
        isPlayerAvailable = true
    }

    /**
     * Goes back to the previous song chosen according the play mode set. It also starts playback
     */
    fun playPrevious(){
        isPlayerAvailable = false
        val nextSongPos = when(playMode){
            PlayMode.LIST_LOOP -> {
                                        if(currentSongPos > 0.toUInt()) {
                                            currentSongPos - 1.toUInt()
                                        } else {
                                            songList!!.getListSize() - 1.toUInt()
                                        }
                                    }
            PlayMode.SONG_LOOP -> currentSongPos
            PlayMode.RANDOM -> Random.nextUInt(0.toUInt(), songList!!.getListSize())
        }
        setCurrentSongAndPLay(nextSongPos)
        isPlayerAvailable = true
    }

    /**
     * Seeks to specified time position.
     * @param time the offset in milliseconds from the start to seek to
     */
    fun setTime(time: UInt){
        try{
            mediaPLayer.seekTo(time.toInt())
        }catch (_:Exception){}
    }

    /**
     * Establishes the current song
     * @param pos position of the new song. If it exceeds playlist size, this method has no effect.
     */
    fun setCurrentSong(pos: UInt){

        isPlayerAvailable = false

        if (pos < songList!!.getListSize()) {
            currentSong = songList!!.getSong(pos)
            currentSongPos = pos

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

    /**
     * Checks if progress can be consulted. This method should be invoked before retrieving playback progress
     * because player can be taken in song change.
     * @return true if progress can be consulted, false otherwise
     */
    fun isAvailableProgress() = isPlayerAvailable

    /**
     * Establishes current song and starts playback
     * @param pos position of the new song
     */
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

    /**
     * Retrieves current song
     * @return song that is being played
     */
    fun getCurrentSong(): Song? = currentSong

    /**
     * Retrieves current song duration
     * @return duration of the song that is being played
     */
    fun getCurrentSongDuration() = currentSong!!.duration

    /**
     * Retrieves current song progress
     * @return progress in milliseconds of the song that is being played
     */
    fun getCurrentSongProgress(): Int{
        return try {
            mediaPLayer.currentPosition
        }catch (_: Exception){
            0
        }
    }

    /**
     * Interface to implement when it is required to get notification when the current song is changed
     */
    interface OnSongChangedListener{
        /**
         * Method invoked when current song has been changed
         */
        fun listen()
    }

}