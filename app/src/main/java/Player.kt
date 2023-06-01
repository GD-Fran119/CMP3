
import android.media.MediaPlayer
import kotlin.random.Random
import kotlin.random.nextUInt

class Player private constructor(){
    private var songList : SongList? = null
    private var currentSong : Song? = null
    private var playMode : PlayMode = PlayMode.LIST_LOOP
    private var currentPos : UInt = 0.toUInt()
    private var playState = false
    private var mediaPLayer : MediaPlayer = MediaPlayer()

    init{
        mediaPLayer.reset()
    }

    companion object {
        val instance: Player by lazy{
            Player()
        }
    }

    public fun changePlayMode(){
        playMode = when(playMode){
            PlayMode.RANDOM -> PlayMode.CURRENT_LOOP
            PlayMode.CURRENT_LOOP -> PlayMode.LIST_LOOP
            PlayMode.LIST_LOOP -> PlayMode.RANDOM
        }
    }

    public fun isListLoop(): Boolean{
        return playMode == PlayMode.LIST_LOOP
    }
    public fun isSongLoop(): Boolean{
        return playMode == PlayMode.CURRENT_LOOP
    }
    public fun isRandomLoop(): Boolean{
        return playMode == PlayMode.RANDOM
    }
    public fun isPlayingSong(): Boolean{

        val isPlaying: Boolean = try{
            mediaPLayer.isPlaying
        }catch (e: Exception){
            false
        }

        return isPlaying
    }
    public fun pause(){
        if(mediaPLayer.isPlaying) mediaPLayer.pause()
        playState = false
    }
    public fun play(){
        try {
            mediaPLayer.start()
            playState = true
        }catch (_: Exception){}
    }
    public fun listSize(): UInt{
        if(songList == null) return 0.toUInt()

        return songList!!.getListSize()}
    public fun setList(newList: SongList){
        songList = newList
        mediaPLayer.reset()
        currentPos = 0.toUInt()
        currentSong = songList!!.getSong(currentPos)
    }
    public fun next(){
        when(playMode){
            PlayMode.LIST_LOOP -> setCurrentSongAndPLay((currentPos + 1.toUInt()) % songList!!.getListSize())
            PlayMode.CURRENT_LOOP -> setCurrentSongAndPLay(currentPos)
            PlayMode.RANDOM -> setCurrentSongAndPLay(Random.nextUInt(0.toUInt(), songList!!.getListSize()))
        }
    }
    public fun previous(){
        when(playMode){
            PlayMode.LIST_LOOP -> setCurrentSongAndPLay((currentPos - 1.toUInt()) % songList!!.getListSize())
            PlayMode.CURRENT_LOOP -> setCurrentSongAndPLay(currentPos)
            PlayMode.RANDOM -> setCurrentSongAndPLay(Random.nextUInt(0.toUInt(), songList!!.getListSize()))
        }
    }
    public fun setTime(time: UInt){}

    public fun setCurrentSong(pos: UInt){

        if (pos < songList!!.getListSize()) {
            currentSong = songList!!.getSong(pos)
            currentPos = pos

            mediaPLayer.reset()
            mediaPLayer.setDataSource(currentSong!!.path)
            mediaPLayer.prepareAsync()

        }

    }
    public fun setCurrentSongAndPLay(pos: UInt){
        setCurrentSong(pos)
        if(currentSong != null) {
            mediaPLayer.setOnPreparedListener {
                mediaPLayer.start()
            }
            playState = true
            mediaPLayer.setOnCompletionListener {
                next()
            }
        }
    }

    public fun getCurrentSong(): Song?{return currentSong}

}