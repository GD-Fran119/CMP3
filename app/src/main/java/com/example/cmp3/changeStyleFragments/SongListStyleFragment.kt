package com.example.cmp3.changeStyleFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.cmp3.R

class SongListStyleFragment(private val layoutRes: Int) : StyleFragmentBase() {
    override fun saveChanges() {
        TODO("Not yet implemented")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ConstraintLayout>(R.id.change_style_background_layout).setOnClickListener{
            Toast.makeText(requireContext(), "General layout clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ConstraintLayout>(R.id.change_style_tab_layout).setOnClickListener{
            Toast.makeText(requireContext(), "Tab layout clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ConstraintLayout>(R.id.change_style_play_all_layout).setOnClickListener{
            Toast.makeText(requireContext(), "Play all layout clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ConstraintLayout>(R.id.change_style_item_background).setOnClickListener{
            Toast.makeText(requireContext(), "Item layout clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ConstraintLayout>(R.id.change_style_list_items_container).setOnClickListener{
            Toast.makeText(requireContext(), "List layout clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ConstraintLayout>(R.id.change_style_current_song_container).setOnClickListener{
            Toast.makeText(requireContext(), "Current song layout clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageView>(R.id.change_style_search).setOnClickListener {
            Toast.makeText(requireContext(), "Search button clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageView>(R.id.change_style_options).setOnClickListener {
            Toast.makeText(requireContext(), "Options button clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageView>(R.id.change_style_item_image).setOnClickListener {
            Toast.makeText(requireContext(), "Item image clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageView>(R.id.change_style_item_options).setOnClickListener {
            Toast.makeText(requireContext(), "Item button clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageView>(R.id.change_style_song_image).setOnClickListener {
            Toast.makeText(requireContext(), "Song image clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageView>(R.id.change_style_play).setOnClickListener {
            Toast.makeText(requireContext(), "Play button clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageView>(R.id.change_style_next).setOnClickListener {
            Toast.makeText(requireContext(), "Next button clicked", Toast.LENGTH_SHORT).show()
        }

    }
}