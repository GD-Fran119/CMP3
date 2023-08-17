package com.example.cmp3.changeStyleFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.cmp3.R

class PlayControlStyleFragment(layoutRes: Int): StyleFragmentBase(layoutRes) {
    override fun saveChanges() {
        TODO("Not yet implemented")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ConstraintLayout>(R.id.change_style_background_layout).setOnClickListener {
            Toast.makeText(activity, "General layout clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageView>(R.id.change_style_back).setOnClickListener {
            Toast.makeText(activity, "Back button clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageView>(R.id.change_style_options).setOnClickListener {
            Toast.makeText(activity, "Options button clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageView>(R.id.change_style_song_image).setOnClickListener {
            Toast.makeText(activity, "Song image clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ConstraintLayout>(R.id.change_style_song_info_container).setOnClickListener {
            Toast.makeText(activity, "Song info layout clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageView>(R.id.change_style_seekbar).setOnClickListener {
            Toast.makeText(activity, "Seekbar clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageView>(R.id.change_style_play_button).setOnClickListener {
            Toast.makeText(activity, "Play button clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageView>(R.id.change_style_next_button).setOnClickListener {
            Toast.makeText(activity, "Next button clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageView>(R.id.change_style_previous_button).setOnClickListener {
            Toast.makeText(activity, "Previous button clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageView>(R.id.change_style_list_items_button).setOnClickListener {
            Toast.makeText(activity, "List button clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageView>(R.id.change_style_play_mode_button).setOnClickListener {
            Toast.makeText(activity, "PLay mode button clicked", Toast.LENGTH_SHORT).show()
        }

    }

}