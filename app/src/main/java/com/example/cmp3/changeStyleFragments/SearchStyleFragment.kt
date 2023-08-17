package com.example.cmp3.changeStyleFragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.cmp3.R

class SearchStyleFragment(layoutRes: Int): StyleFragmentBase(layoutRes) {
    override fun saveChanges() {
        TODO("Not yet implemented")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Listenerssss

        view.findViewById<ConstraintLayout>(R.id.change_style_background_layout).setOnClickListener {
            Toast.makeText(activity, "General layout clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageView>(R.id.change_style_back).setOnClickListener {
            Toast.makeText(activity, "Back button clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageView>(R.id.change_style_options).setOnClickListener {
            Toast.makeText(activity, "Options button clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageView>(R.id.change_style_search_box).setOnClickListener {
            Toast.makeText(activity, "Search box clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageView>(R.id.change_style_clear_button).setOnClickListener {
            Toast.makeText(activity, "Clear button clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ConstraintLayout>(R.id.change_style_list_items_container).setOnClickListener{
            Toast.makeText(requireContext(), "List layout clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ConstraintLayout>(R.id.change_style_item_background).setOnClickListener{
            Toast.makeText(requireContext(), "Item layout clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageView>(R.id.change_style_item_image).setOnClickListener {
            Toast.makeText(activity, "Item image clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageView>(R.id.change_style_item_options).setOnClickListener {
            Toast.makeText(activity, "Item options clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ConstraintLayout>(R.id.change_style_current_song_container).setOnClickListener{
            Toast.makeText(requireContext(), "Current song layout clicked", Toast.LENGTH_SHORT).show()
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