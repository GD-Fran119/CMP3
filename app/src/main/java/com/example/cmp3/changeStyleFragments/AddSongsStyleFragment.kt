package com.example.cmp3.changeStyleFragments

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.bottomSheets.ColorPickerBottomSheet1
import com.example.bottomSheets.ColorPickerBottomSheet2
import com.example.cmp3.AddSongsToPlaylistActivity
import com.example.cmp3.R
import com.example.config.GlobalPreferencesConstants
import com.example.cmp3.ChangeStyleActivity
import kotlin.properties.Delegates

/**
 * Fragment class used in [ChangeStyleActivity] to change [AddSongsToPlaylistActivity]'s style
 * @param layoutRes resource layout to use when creating the fragment. This parameter must be one of the
 * [AddSongsStyleFragment]'s available layouts
 */
class AddSongsStyleFragment(layoutRes: Int): StyleFragmentBase(layoutRes) {
    private lateinit var generalLayout: ConstraintLayout
    private lateinit var backButton: ImageView
    private lateinit var optionsButton: ImageView
    private lateinit var searchBox: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var itemsContainer: ConstraintLayout
    private lateinit var itemLayout1: ConstraintLayout
    private lateinit var titleView1: ImageView
    private lateinit var desc1: ImageView
    private lateinit var itemIcon1: ImageView
    private lateinit var itemLayout2: ConstraintLayout
    private lateinit var titleView2: ImageView
    private lateinit var desc2: ImageView
    private lateinit var itemIcon2: ImageView
    private var version by Delegates.notNull<Int>()

    override fun saveChanges() {
        activity?.getSharedPreferences(GlobalPreferencesConstants.ADD_SONGS_ACT_PREFERENCES, Context.MODE_PRIVATE)!!.edit().apply {
            val constants = AddSongsToPlaylistActivity.PreferencesConstants
            putInt(constants.GENERAL_LAYOUT_BG_KEY,
                generalLayout.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_layout_bg))
            putInt(constants.BACK_BTN_BG_KEY,
                backButton.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_bg))
            putInt(constants.BACK_BTN_FG_KEY,
                backButton.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_fg))
            putInt(constants.OPTIONS_BTN_BG_KEY,
                optionsButton.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_bg))
            putInt(constants.OPTIONS_BTN_FG_KEY,
                optionsButton.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_fg))
            putInt(constants.SEARCH_BOX_KEY,
                searchBox.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_text_color))
            putInt(constants.CLEAR_BTN_KEY,
                clearButton.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_fg))
            putInt(constants.ITEMS_CONTAINER_BG_KEY,
                itemsContainer.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_layout_bg))
            putInt(constants.ITEM_BG_KEY,
                itemLayout1.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_layout_bg))
            putInt(constants.ITEM_TEXT_KEY,
                titleView1.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_text_color))
            putInt(constants.ITEM_ICON_KEY,
                itemIcon1.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_icon_color))
            putInt(constants.STYLE_VERSION_KEY, version + 1)
        }.apply()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Setting up listeners and expected behaviour when user selects a color for an UI item

        generalLayout = view.findViewById(R.id.change_style_background_layout)
        generalLayout.setOnClickListener {
            val picker = ColorPickerBottomSheet1("Global layout",
                arrayOf("Background color"),
                intArrayOf(it.backgroundTintList?.defaultColor ?: 0))
            picker.setOnColorSelectedListener { _, color ->
                it.backgroundTintList = ColorStateList.valueOf(color)
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        backButton = view.findViewById(R.id.change_style_back)
        backButton.setOnClickListener {
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

        optionsButton = view.findViewById(R.id.change_style_options)
        optionsButton.setOnClickListener {
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

        searchBox = view.findViewById(R.id.change_style_search_box)
        searchBox.setOnClickListener {
            val picker = ColorPickerBottomSheet1("Search box",
                arrayOf("Text color"),
                intArrayOf((it.foregroundTintList?.defaultColor ?: 0)))
            picker.setOnColorSelectedListener { _, color ->
                it.foregroundTintList = ColorStateList.valueOf(color)
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        clearButton = view.findViewById(R.id.change_style_clear_button)
        clearButton.setOnClickListener {
            val picker = ColorPickerBottomSheet1("Clear button",
                arrayOf("Text color"),
                intArrayOf((it.foregroundTintList?.defaultColor ?: 0)))
            picker.setOnColorSelectedListener { _, color ->
                it.foregroundTintList = ColorStateList.valueOf(color)
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        itemsContainer = view.findViewById(R.id.change_style_list_items_container)
        itemsContainer.setOnClickListener{
            val picker = ColorPickerBottomSheet1("Item list",
                arrayOf("Background color"),
                intArrayOf(it.backgroundTintList?.defaultColor ?: 0))
            picker.setOnColorSelectedListener { _, color ->
                it.backgroundTintList = ColorStateList.valueOf(color)
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        itemLayout1 = view.findViewById(R.id.change_style_item_background1)
        titleView1 = view.findViewById(R.id.change_style_item_title1)
        desc1 = view.findViewById(R.id.change_style_item_desc1)
        itemLayout2 = view.findViewById(R.id.change_style_item_background2)
        titleView2 = view.findViewById(R.id.change_style_item_title2)
        desc2 = view.findViewById(R.id.change_style_item_desc2)

        itemLayout1.setOnClickListener{
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

        itemLayout2.setOnClickListener{
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

        itemIcon1 = view.findViewById(R.id.change_style_item_icon1)
        itemIcon1.setOnClickListener{
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

        itemIcon2 = view.findViewById(R.id.change_style_item_icon2)
        itemIcon2.setOnClickListener{
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

        //End of setting up listeners

        loadInitialStyle()
    }

    override fun loadInitialStyle() {
        requireActivity().getSharedPreferences(GlobalPreferencesConstants.ADD_SONGS_ACT_PREFERENCES, Context.MODE_PRIVATE).apply {
            val constants = AddSongsToPlaylistActivity.PreferencesConstants
            generalLayout.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.GENERAL_LAYOUT_BG_KEY,
                    requireContext().getColor(R.color.default_layout_bg)
                )
            )
            backButton.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.BACK_BTN_BG_KEY,
                    requireContext().getColor(R.color.default_buttons_bg)
                )
            )
            backButton.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.BACK_BTN_FG_KEY,
                    requireContext().getColor(R.color.default_buttons_fg)
                )
            )
            optionsButton.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.OPTIONS_BTN_BG_KEY,
                    requireContext().getColor(R.color.default_buttons_bg)
                )
            )
            optionsButton.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.OPTIONS_BTN_FG_KEY,
                    requireContext().getColor(R.color.default_buttons_fg)
                )
            )
            searchBox.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.SEARCH_BOX_KEY,
                    requireContext().getColor(R.color.default_text_color)
                )
            )
            clearButton.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.CLEAR_BTN_KEY,
                    requireContext().getColor(R.color.default_buttons_fg)
                )
            )
            itemsContainer.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.ITEMS_CONTAINER_BG_KEY,
                    requireContext().getColor(R.color.default_layout_bg)
                )
            )

            val itemBGColor = getInt(constants.ITEM_BG_KEY,
                                        requireContext().getColor(R.color.default_layout_bg)
                                        )
            itemLayout1.backgroundTintList = ColorStateList.valueOf(itemBGColor)
            itemLayout2.backgroundTintList = ColorStateList.valueOf(itemBGColor)

            val itemTextColor = getInt(constants.ITEM_TEXT_KEY,
                                        requireContext().getColor(R.color.default_text_color)
                                        )
            titleView1.foregroundTintList = ColorStateList.valueOf(itemTextColor)
            titleView2.foregroundTintList = ColorStateList.valueOf(itemTextColor)
            desc1.foregroundTintList = ColorStateList.valueOf(itemTextColor)
            desc2.foregroundTintList = ColorStateList.valueOf(itemTextColor)

            val itemIconColor = getInt(constants.ITEM_ICON_KEY,
                                        requireContext().getColor(R.color.default_icon_color)
                                        )
            itemIcon1.foregroundTintList = ColorStateList.valueOf(itemIconColor)
            itemIcon2.foregroundTintList = ColorStateList.valueOf(itemIconColor)

            version = getInt(constants.STYLE_VERSION_KEY, 0)
        }
    }
}