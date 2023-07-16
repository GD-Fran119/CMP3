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
import java.lang.Exception

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    ListItemListDialogFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */
class ListItemListDialogFragment : BottomSheetDialogFragment() {

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
        val currentList = Player.instance.getList()
        view.findViewById<TextView>(R.id.bottom_box_list_name)?.text = currentList?.title ?: "Unknown"
        view.findViewById<TextView>(R.id.bottom_box_list_number)?.text =  "${currentList?.getListSize().toString()} songs"
        val list = view.findViewById<RecyclerView>(R.id.bottom_box_list)
        list?.adapter = ListItemAdapter(Player.instance.listSize().toInt())
    }

    private inner class ViewHolder(binding: FragmentItemListDialogListDialogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val title = binding.root.findViewById<TextView>(R.id.titleBottomBox)!!
        val desc = binding.root.findViewById<TextView>(R.id.albumNArtistBottomBox)!!
    }

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
            val album = if (currentSong?.artist == "<unknown>") "Unknown" else currentSong?.artist
            holder.desc.text = "${artist} - ${album}"
            holder.itemView.setOnClickListener {
                //New intent to play control view with song playing
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