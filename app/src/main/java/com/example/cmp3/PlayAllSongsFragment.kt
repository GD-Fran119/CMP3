package com.example.cmp3

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.databaseStuff.SongPlaylistRelationData
import com.example.playerStuff.Player
import com.example.songsAndPlaylists.SongList
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * A simple [Fragment] subclass.
 * Use the [PlayAllSongsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayAllSongsFragment : Fragment() {

    private lateinit var list: SongList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play_all_songs, container, false)
    }

    fun setList(newList: SongList){
        list = newList

        view?.findViewById<TextView>(R.id.play_all_songs_number)?.text = if (list.getListSize() != 1.toUInt()) "${list.getListSize()} songs"
                                                                            else "1 song"
        view?.findViewById<LinearLayout>(R.id.play_all_songs_container)?.setOnClickListener{
            if(list.getListSize() == 0.toUInt())
                Toast.makeText(activity, "There are no songs to play", Toast.LENGTH_SHORT).show()
            else{
                val player = Player.instance
                player.setList(list)
                player.setCurrentSongAndPLay(Random.nextUInt(list.getListSize()))
                activity?.startActivity(Intent(activity, PlayControlView::class.java))
            }
        }
    }


    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment PlayAllSongsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = PlayAllSongsFragment
    }
}