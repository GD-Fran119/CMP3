package com.example.cmp3.changeStyleFragments


import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.bottomSheets.ColorPickerBottomSheet1
import com.example.bottomSheets.ColorPickerBottomSheet2
import com.example.bottomSheets.ColorPickerBottomSheet3
import com.example.cmp3.R

class SearchStyleFragment(layoutRes: Int): StyleFragmentBase(layoutRes) {
    override fun saveChanges() {
        TODO("Not yet implemented")
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Listenerssss

        view.findViewById<ConstraintLayout>(R.id.change_style_background_layout).setOnClickListener {
            val picker = ColorPickerBottomSheet1("Global layout",
                arrayOf("Background color"),
                intArrayOf(it.backgroundTintList?.defaultColor ?: 0))
            picker.setOnColorSelectedListener { _, color ->
                it.backgroundTintList = ColorStateList.valueOf(color)
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        view.findViewById<ImageView>(R.id.change_style_back).setOnClickListener {
            val picker = ColorPickerBottomSheet2("Back button",
                arrayOf("Foreground color", "Background color"),
                intArrayOf((it.foregroundTintList?.defaultColor ?: 0), (it.backgroundTintList?.defaultColor ?: 0)))
            picker.setOnColorSelectedListener { itemPosition, color ->
                if(itemPosition == 0){
                    it.foregroundTintList = ColorStateList.valueOf(color)
                }
                else if(itemPosition == 1){
                    it.backgroundTintList = ColorStateList.valueOf(color)
                }
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        view.findViewById<ImageView>(R.id.change_style_options).setOnClickListener {
            val picker = ColorPickerBottomSheet2("Options button",
                arrayOf("Foreground color", "Background color"),
                intArrayOf((it.foregroundTintList?.defaultColor ?: 0), (it.backgroundTintList?.defaultColor ?: 0)))
            picker.setOnColorSelectedListener { itemPosition, color ->
                if(itemPosition == 0){
                    it.foregroundTintList = ColorStateList.valueOf(color)
                }
                else if(itemPosition == 1){
                    it.backgroundTintList = ColorStateList.valueOf(color)
                }
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        view.findViewById<ImageView>(R.id.change_style_search_box).setOnClickListener {
            val picker = ColorPickerBottomSheet1("Search box",
                arrayOf("Text color"),
                intArrayOf((it.foregroundTintList?.defaultColor ?: 0)))
            picker.setOnColorSelectedListener { _, color ->
                it.foregroundTintList = ColorStateList.valueOf(color)
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        view.findViewById<ImageView>(R.id.change_style_clear_button).setOnClickListener {
            val picker = ColorPickerBottomSheet1("Clear button",
                arrayOf("Text color"),
                intArrayOf((it.foregroundTintList?.defaultColor ?: 0)))
            picker.setOnColorSelectedListener { _, color ->
                it.foregroundTintList = ColorStateList.valueOf(color)
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        view.findViewById<ConstraintLayout>(R.id.change_style_list_items_container).setOnClickListener{
            val picker = ColorPickerBottomSheet1("Item list",
                arrayOf("Background color"),
                intArrayOf(it.backgroundTintList?.defaultColor ?: 0))
            picker.setOnColorSelectedListener { _, color ->
                it.backgroundTintList = ColorStateList.valueOf(color)
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        view.findViewById<ConstraintLayout>(R.id.change_style_item_background).setOnClickListener{
            val titleView = view.findViewById<ImageView>(R.id.change_style_item_title)
            val desc = view.findViewById<ImageView>(R.id.change_style_item_desc)
            val picker = ColorPickerBottomSheet2("Item layout",
                arrayOf("Text color", "Background color"),
                intArrayOf((titleView.foregroundTintList?.defaultColor ?: 0), (it.backgroundTintList?.defaultColor ?: 0)))
            picker.setOnColorSelectedListener { itemPosition, color ->
                if(itemPosition == 0){
                    titleView.foregroundTintList = ColorStateList.valueOf(color)
                    desc.foregroundTintList = ColorStateList.valueOf(color)
                }
                else if(itemPosition == 1){
                    it.backgroundTintList = ColorStateList.valueOf(color)
                }
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        view.findViewById<ImageView>(R.id.change_style_item_image).setOnClickListener {
            val picker = ColorPickerBottomSheet2("Item image placeholder",
                arrayOf("Foreground color", "Background color"),
                intArrayOf((it.foregroundTintList?.defaultColor ?: 0), (it.backgroundTintList?.defaultColor ?: 0)))
            picker.setOnColorSelectedListener { itemPosition, color ->
                if(itemPosition == 0){
                    it.foregroundTintList = ColorStateList.valueOf(color)
                }
                else if(itemPosition == 1){
                    it.backgroundTintList = ColorStateList.valueOf(color)
                }
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        view.findViewById<ImageView>(R.id.change_style_item_options).setOnClickListener {
            val picker = ColorPickerBottomSheet2("Item options button",
                arrayOf("Foreground color", "Background color"),
                intArrayOf((it.foregroundTintList?.defaultColor ?: 0), (it.backgroundTintList?.defaultColor ?: 0)))
            picker.setOnColorSelectedListener { itemPosition, color ->
                if(itemPosition == 0){
                    it.foregroundTintList = ColorStateList.valueOf(color)
                }
                else if(itemPosition == 1){
                    it.backgroundTintList = ColorStateList.valueOf(color)
                }
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        view.findViewById<ConstraintLayout>(R.id.change_style_current_song_container).setOnClickListener{
            val titleView = view.findViewById<ImageView>(R.id.change_style_song_title)
            val descView = view.findViewById<ImageView>(R.id.change_style_song_desc)
            val progressBar = view.findViewById<ImageView>(R.id.change_style_song_progressbar)

            val picker = ColorPickerBottomSheet3("Current song container",
                arrayOf("Progress bar color","Text color", "Background color"),
                intArrayOf((progressBar.foregroundTintList?.defaultColor ?: 0), (titleView.foregroundTintList?.defaultColor ?: 0), (it.backgroundTintList?.defaultColor ?: 0)))
            picker.setOnColorSelectedListener { itemPosition, color ->
                if(itemPosition == 0){
                    progressBar.foregroundTintList = ColorStateList.valueOf(color)
                }
                else if(itemPosition == 1){
                    titleView.foregroundTintList = ColorStateList.valueOf(color)
                    descView.foregroundTintList = ColorStateList.valueOf(color)
                }
                else if(itemPosition == 2){
                    it.backgroundTintList = ColorStateList.valueOf(color)
                }
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        view.findViewById<ImageView>(R.id.change_style_song_image).setOnClickListener {
            val picker = ColorPickerBottomSheet2("Current song image placeholder",
                arrayOf("Foreground color", "Background color"),
                intArrayOf((it.foregroundTintList?.defaultColor ?: 0), (it.backgroundTintList?.defaultColor ?: 0)))
            picker.setOnColorSelectedListener { itemPosition, color ->
                if(itemPosition == 0){
                    it.foregroundTintList = ColorStateList.valueOf(color)
                }
                else if(itemPosition == 1){
                    it.backgroundTintList = ColorStateList.valueOf(color)
                }
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        view.findViewById<ImageView>(R.id.change_style_play).setOnClickListener {
            val picker = ColorPickerBottomSheet2("Play button",
                arrayOf("Foreground color", "Background color"),
                intArrayOf((it.foregroundTintList?.defaultColor ?: 0), (it.backgroundTintList?.defaultColor ?: 0)))
            picker.setOnColorSelectedListener { itemPosition, color ->
                if(itemPosition == 0){
                    it.foregroundTintList = ColorStateList.valueOf(color)
                }
                else if(itemPosition == 1){
                    it.backgroundTintList = ColorStateList.valueOf(color)
                }
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        view.findViewById<ImageView>(R.id.change_style_next).setOnClickListener {
            val picker = ColorPickerBottomSheet2("Next button",
                arrayOf("Foreground color", "Background color"),
                intArrayOf((it.foregroundTintList?.defaultColor ?: 0), (it.backgroundTintList?.defaultColor ?: 0)))
            picker.setOnColorSelectedListener { itemPosition, color ->
                if(itemPosition == 0){
                    it.foregroundTintList = ColorStateList.valueOf(color)
                }
                else if(itemPosition == 1){
                    it.backgroundTintList = ColorStateList.valueOf(color)
                }
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }
    }
}