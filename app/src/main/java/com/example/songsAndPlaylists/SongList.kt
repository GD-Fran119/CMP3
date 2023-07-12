package com.example.songsAndPlaylists

class SongList(val title: String, private var songs: MutableList<Song>, val creationDate: String) {

    val DEFAULT_DATE = "1970-01-01"
    var duration: UInt = 0u
        private set

    init {
        duration = 0.toUInt()
        this.computeDuration()
    }

    private fun computeDuration(){
        duration = 0.toUInt()
        songs.forEach { s -> duration += s.duration }
    }

    fun addSong(newSong: Song){
        songs += newSong
    }

    fun getSong(pos: UInt): Song {
        return songs[pos.toInt()]
    }

    fun getList() = songs.toList()

    fun getListSize(): UInt{
        return songs.size.toUInt()
    }
    fun setSongs(newList: Array<Song>){
        this.songs = newList.toMutableList()
    }
}