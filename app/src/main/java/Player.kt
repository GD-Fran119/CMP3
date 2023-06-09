
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
            PlayMode.RANDOM -> PlayMode.CURRENT_LOOP
            PlayMode.CURRENT_LOOP -> PlayMode.LIST_LOOP
            PlayMode.LIST_LOOP -> PlayMode.RANDOM
        }
    }

    fun isListLoop(): Boolean{
        return playMode == PlayMode.LIST_LOOP
    }
    fun isSongLoop(): Boolean{
        return playMode == PlayMode.CURRENT_LOOP
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

        return songList!!.getListSize()}
    fun setList(newList: SongList){
        songList = newList
        mediaPLayer.reset()
        currentPos = 0.toUInt()
        currentSong = songList!!.getSong(currentPos)
    }

    fun getList() = songList
    fun next(){
        when(playMode){
            PlayMode.LIST_LOOP -> setCurrentSongAndPLay((currentPos + 1.toUInt()) % songList!!.getListSize())
            PlayMode.CURRENT_LOOP -> setCurrentSongAndPLay(currentPos)
            PlayMode.RANDOM -> setCurrentSongAndPLay(Random.nextUInt(songList!!.getListSize()))
        }
    }
    fun previous(){
        when(playMode){
            PlayMode.LIST_LOOP -> setCurrentSongAndPLay((currentPos - 1.toUInt()) % songList!!.getListSize())
            PlayMode.CURRENT_LOOP -> setCurrentSongAndPLay(currentPos)
            PlayMode.RANDOM -> setCurrentSongAndPLay(Random.nextUInt(0.toUInt(), songList!!.getListSize()))
        }
    }
    fun setTime(time: UInt){}

    fun setCurrentSong(pos: UInt){

        if (pos < songList!!.getListSize() && pos != currentPos) {
            currentSong = songList!!.getSong(pos)
            currentPos = pos

            mediaPLayer.reset()
            mediaPLayer.setDataSource(currentSong!!.path)
            mediaPLayer.prepareAsync()

        }

    }

    fun getCurrentPos() = currentPos
    fun setCurrentSongAndPLay(pos: UInt){
        setCurrentSong(pos)
        if(currentSong != null) {
            mediaPLayer.setOnPreparedListener {
                mediaPLayer.start()
                isPlaying = true
            }
            mediaPLayer.setOnCompletionListener {
                next()
                SongFinishedNotifier.notifySongChanged()
            }
        }
        SongFinishedNotifier.notifySongChanged()
    }

    fun getCurrentSong(): Song?{return currentSong}

}