package com.example.bottomSheets

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.cmp3.R
import com.example.cmp3.databinding.FragmentItemListDialogListDialogItemBinding
import com.example.cmp3.databinding.FragmentItemListDialogListDialogBinding
import com.example.playerStuff.Player
import com.example.songsAndPlaylists.SongList
import java.lang.Exception

/**
 * Class that shows a bottom sheet where a playlist's songs are displayed
 * @param songList playlist whose songs wil be displayed
 */
class SongListItemsDialogFragment(private val songList: SongList?) : BottomSheetDialogFragment() {

    private var _binding: FragmentItemListDialogListDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentItemListDialogListDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<TextView>(R.id.bottom_box_list_name)?.text = songList?.title ?: "Unknown"

        view.findViewById<TextView>(R.id.bottom_box_list_number)?.text =  if(songList?.getListSize() != 1.toUInt()) "${songList?.getListSize()} songs"
                                                                            else "1 song"
        val list = view.findViewById<RecyclerView>(R.id.bottom_box_list)
        list?.adapter = ListItemAdapter(Player.instance.listSize().toInt())
    }

    /**
     * ViewHolder class for [RecyclerView]
     */
    private inner class ViewHolder(binding: FragmentItemListDialogListDialogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val title = binding.root.findViewById<TextView>(R.id.titleBottomBox)!!
        val desc = binding.root.findViewById<TextView>(R.id.albumNArtistBottomBox)!!
    }

    /**
     * Adapter class for [RecyclerView]
     * @param mItemCount number of items to display in [RecyclerView]
     */
    private inner class ListItemAdapter(private val mItemCount: Int) :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            return ViewHolder(
                FragmentItemListDialogListDialogItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currentSong = Player.instance.getList()?.getSong(position.toUInt())
            holder.title.text = currentSong?.title ?: "Unknown"
            val artist =  if (currentSong?.artist == "<unknown>") "Unknown" else currentSong?.artist
            val album = if (currentSong?.album == "<unknown>") "Unknown" else currentSong?.album
            holder.desc.text = "${artist} - ${album}"
            holder.itemView.setOnClickListener {
                try {
                    Player.instance.setCurrentSongAndPLay(position.toUInt())
                }catch (e: Exception){
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }

            }
        }

        override fun getItemCount(): Int {
            return mItemCount
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}