package com.example.cmp3.changeStyleFragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.bottomSheets.ColorPickerBottomSheet1
import com.example.bottomSheets.ColorPickerBottomSheet2
import com.example.cmp3.R

class AddSongsStyleFragment(layoutRes: Int): StyleFragmentBase(layoutRes) {
    override fun saveChanges() {
        TODO("Not yet implemented")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        view.findViewById<ConstraintLayout>(R.id.change_style_item_background1).setOnClickListener{
            val titleView1 = view.findViewById<ImageView>(R.id.change_style_item_title1)
            val titleView2 = view.findViewById<ImageView>(R.id.change_style_item_title2)
            val desc1 = view.findViewById<ImageView>(R.id.change_style_item_desc1)
            val desc2 = view.findViewById<ImageView>(R.id.change_style_item_desc2)
            val itemLayout2 = view.findViewById<ConstraintLayout>(R.id.change_style_item_background2)
            val picker = ColorPickerBottomSheet2("Item layout",
                arrayOf("Text color", "Background color"),
                intArrayOf((titleView1.foregroundTintList?.defaultColor ?: 0), (it.backgroundTintList?.defaultColor ?: 0)))
            picker.setOnColorSelectedListener { itemPosition, color ->
                if(itemPosition == 0){
                    titleView1.foregroundTintList = ColorStateList.valueOf(color)
                    titleView2.foregroundTintList = ColorStateList.valueOf(color)
                    desc1.foregroundTintList = ColorStateList.valueOf(color)
                    desc2.foregroundTintList = ColorStateList.valueOf(color)
                }
                else if(itemPosition == 1){
                    it.backgroundTintList = ColorStateList.valueOf(color)
                    itemLayout2.backgroundTintList = ColorStateList.valueOf(color)
                }
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        view.findViewById<ConstraintLayout>(R.id.change_style_item_background2).setOnClickListener{
            val titleView1 = view.findViewById<ImageView>(R.id.change_style_item_title1)
            val titleView2 = view.findViewById<ImageView>(R.id.change_style_item_title2)
            val desc1 = view.findViewById<ImageView>(R.id.change_style_item_desc1)
            val desc2 = view.findViewById<ImageView>(R.id.change_style_item_desc2)
            val itemLayout1 = view.findViewById<ConstraintLayout>(R.id.change_style_item_background1)
            val picker = ColorPickerBottomSheet2("Item layout",
                arrayOf("Text color", "Background color"),
                intArrayOf((titleView2.foregroundTintList?.defaultColor ?: 0), (it.backgroundTintList?.defaultColor ?: 0)))
            picker.setOnColorSelectedListener { itemPosition, color ->
                if(itemPosition == 0){
                    titleView1.foregroundTintList = ColorStateList.valueOf(color)
                    titleView2.foregroundTintList = ColorStateList.valueOf(color)
                    desc1.foregroundTintList = ColorStateList.valueOf(color)
                    desc2.foregroundTintList = ColorStateList.valueOf(color)
                }
                else if(itemPosition == 1){
                    it.backgroundTintList = ColorStateList.valueOf(color)
                    itemLayout1.backgroundTintList = ColorStateList.valueOf(color)
                }
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        view.findViewById<ImageView>(R.id.change_style_item_icon1).setOnClickListener{
            val itemIcon2 = view.findViewById<ImageView>(R.id.change_style_item_icon2)
            val picker = ColorPickerBottomSheet1("Item image icon",
                arrayOf("Icon color"),
                intArrayOf((it.foregroundTintList?.defaultColor ?: 0)))
            picker.setOnColorSelectedListener { _, color ->
                it.foregroundTintList = ColorStateList.valueOf(color)
                itemIcon2.foregroundTintList = ColorStateList.valueOf(color)
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        view.findViewById<ImageView>(R.id.change_style_item_icon2).setOnClickListener{
            val itemIcon1 = view.findViewById<ImageView>(R.id.change_style_item_icon1)
            val picker = ColorPickerBottomSheet1("Item image icon",
                arrayOf("Icon color"),
                intArrayOf((it.foregroundTintList?.defaultColor ?: 0)))
            picker.setOnColorSelectedListener { _, color ->
                it.foregroundTintList = ColorStateList.valueOf(color)
                itemIcon1.foregroundTintList = ColorStateList.valueOf(color)
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }
    }
}