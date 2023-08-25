package com.example.cmp3.changeStyleFragments

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.bottomSheets.ColorPickerBottomSheet1
import com.example.bottomSheets.ColorPickerBottomSheet2
import com.example.bottomSheets.ColorPickerBottomSheet3
import com.example.cmp3.ChangeStyleActivity
import com.example.cmp3.CurrentSongFragment
import com.example.cmp3.R
import com.example.cmp3.SearchActivity
import com.example.config.GlobalPreferencesConstants

/**
 * Fragment class used in [ChangeStyleActivity] to change [SearchActivity]'s style
 * @param layoutRes resource layout to use when creating the fragment. This parameter must be one of the
 * [SearchStyleFragment]'s available layouts
 */
class SearchStyleFragment(layoutRes: Int): StyleFragmentBase(layoutRes) {
    private lateinit var generalLayout: ConstraintLayout
    private lateinit var backButton: ImageView
    private lateinit var optionsButton: ImageView
    private lateinit var searchBox: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var itemsContainer: ConstraintLayout
    private lateinit var itemLayout: ConstraintLayout
    private lateinit var itemTitle: ImageView
    private lateinit var itemDesc: ImageView
    private lateinit var itemImage: ImageView
    private lateinit var itemOptionsButton: ImageView
    private lateinit var songTitle: ImageView
    private lateinit var songDesc: ImageView
    private lateinit var songImage: ImageView
    private lateinit var progressBar: ImageView
    private lateinit var songContainer: ConstraintLayout
    private lateinit var playButton: ImageView
    private lateinit var nextButton: ImageView
    override fun saveChanges() {
        //Activity prefs
        activity?.getSharedPreferences(GlobalPreferencesConstants.SEARCH_ACT_PREFERENCES, Context.MODE_PRIVATE)!!.edit().apply {
            val constants = SearchActivity.PreferencesConstants
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
                itemLayout.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_layout_bg))
            putInt(constants.ITEM_IMG_BG_KEY,
                itemImage.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_image_placeholder_bg))
            putInt(constants.ITEM_IMG_FG_KEY,
                itemImage.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_image_placeholder_fg))
            putInt(constants.ITEM_TEXT_KEY,
                itemTitle.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_text_color))
            putInt(constants.ITEM_BTN_BG_KEY,
                itemOptionsButton.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_bg))
            putInt(constants.ITEM_BTN_FG_KEY,
                itemOptionsButton.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_fg))
        }.apply()

        //Current song fragment prefs
        activity?.getSharedPreferences(GlobalPreferencesConstants.CURRENT_SONG_FRAGMENT_PREFERENCES, Context.MODE_PRIVATE)!!.edit().apply {
            val constants = CurrentSongFragment.PreferencesConstants
            putInt(constants.FRAGMENT_LAYOUT_KEY,
                songContainer.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_layout_bg))
            putInt(constants.PROGRESS_BAR_KEY,
                progressBar.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_progress_bar_color))
            putInt(constants.IMAGE_BG_KEY,
                songImage.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_image_placeholder_bg))
            putInt(constants.IMAGE_FG_KEY,
                songImage.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_image_placeholder_fg))
            putInt(constants.TEXT_KEY,
                songTitle.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_text_color))
            putInt(constants.PLAY_BTN_BG_KEY,
                playButton.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_bg))
            putInt(constants.PLAY_BTN_FG_KEY,
                playButton.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_fg))
            putInt(constants.NEXT_BTN_BG_KEY,
                nextButton.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_bg))
            putInt(constants.PLAY_BTN_FG_KEY,
                nextButton.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_fg))
        }.apply()
    }

    override fun loadInitialStyle() {
        //Activity prefs
        requireActivity().getSharedPreferences(GlobalPreferencesConstants.SEARCH_ACT_PREFERENCES, Context.MODE_PRIVATE).apply {
            val constants = SearchActivity.PreferencesConstants
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
            itemLayout.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.ITEM_BG_KEY,
                    requireContext().getColor(R.color.default_layout_bg)
                )
            )
            itemImage.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.ITEM_IMG_BG_KEY,
                    requireContext().getColor(R.color.default_image_placeholder_bg)
                )
            )
            itemImage.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.ITEM_IMG_FG_KEY,
                    requireContext().getColor(R.color.default_image_placeholder_fg)
                )
            )

            val itemTextColor = getInt(constants.ITEM_TEXT_KEY,
                requireContext().getColor(R.color.default_text_color)
            )
            itemTitle.foregroundTintList = ColorStateList.valueOf(itemTextColor)
            itemDesc.foregroundTintList = ColorStateList.valueOf(itemTextColor)

            itemOptionsButton.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.ITEM_BTN_BG_KEY,
                    requireContext().getColor(R.color.default_buttons_bg)
                )
            )
            itemOptionsButton.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.ITEM_BTN_FG_KEY,
                    requireContext().getColor(R.color.default_buttons_fg)
                )
            )
        }

        //Current song fragment prefs
        requireActivity().getSharedPreferences(GlobalPreferencesConstants.CURRENT_SONG_FRAGMENT_PREFERENCES, Context.MODE_PRIVATE).apply {
            val constants = CurrentSongFragment.PreferencesConstants
            songContainer.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.FRAGMENT_LAYOUT_KEY,
                    requireContext().getColor(R.color.default_layout_bg)
                )
            )
            progressBar.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.PROGRESS_BAR_KEY,
                    requireContext().getColor(R.color.default_progress_bar_color)
                )
            )
            songImage.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.IMAGE_BG_KEY,
                    requireContext().getColor(R.color.default_image_placeholder_bg)
                )
            )
            songImage.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.IMAGE_FG_KEY,
                    requireContext().getColor(R.color.default_image_placeholder_fg)
                )
            )

            val songInfoColor = getInt(constants.TEXT_KEY,
                requireContext().getColor(R.color.default_text_color)
            )
            songTitle.foregroundTintList = ColorStateList.valueOf(songInfoColor)
            songDesc.foregroundTintList = ColorStateList.valueOf(songInfoColor)

            playButton.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.PLAY_BTN_BG_KEY,
                    requireContext().getColor(R.color.default_buttons_bg)
                )
            )
            playButton.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.PLAY_BTN_FG_KEY,
                    requireContext().getColor(R.color.default_buttons_fg)
                )
            )
            nextButton.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.NEXT_BTN_BG_KEY,
                    requireContext().getColor(R.color.default_buttons_bg)
                )
            )
            nextButton.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.PLAY_BTN_FG_KEY,
                    requireContext().getColor(R.color.default_buttons_fg)
                )
            )
        }
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

        itemTitle = view.findViewById(R.id.change_style_item_title)
        itemDesc = view.findViewById(R.id.change_style_item_desc)
        itemLayout = view.findViewById(R.id.change_style_item_background)
        itemLayout.setOnClickListener{
            val picker = ColorPickerBottomSheet2("Item layout",
                arrayOf("Text color", "Background color"),
                intArrayOf((itemTitle.foregroundTintList?.defaultColor ?: 0), (it.backgroundTintList?.defaultColor ?: 0)))
            picker.setOnColorSelectedListener { itemPosition, color ->
                if(itemPosition == 0){
                    itemTitle.foregroundTintList = ColorStateList.valueOf(color)
                    itemDesc.foregroundTintList = ColorStateList.valueOf(color)
                }
                else if(itemPosition == 1){
                    it.backgroundTintList = ColorStateList.valueOf(color)
                }
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        itemImage = view.findViewById(R.id.change_style_item_image)
        itemImage.setOnClickListener {
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

        itemOptionsButton = view.findViewById(R.id.change_style_item_options)
        itemOptionsButton.setOnClickListener {
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

        songTitle = view.findViewById(R.id.change_style_song_title)
        songDesc = view.findViewById(R.id.change_style_song_desc)
        progressBar = view.findViewById(R.id.change_style_song_progressbar)
        songContainer = view.findViewById(R.id.change_style_current_song_container)
        songContainer.setOnClickListener{
            val picker = ColorPickerBottomSheet3("Current song container",
                arrayOf("Progress bar color","Text color", "Background color"),
                intArrayOf((progressBar.foregroundTintList?.defaultColor ?: 0), (songTitle.foregroundTintList?.defaultColor ?: 0), (it.backgroundTintList?.defaultColor ?: 0)))
            picker.setOnColorSelectedListener { itemPosition, color ->
                if(itemPosition == 0){
                    progressBar.foregroundTintList = ColorStateList.valueOf(color)
                }
                else if(itemPosition == 1){
                    songTitle.foregroundTintList = ColorStateList.valueOf(color)
                    songDesc.foregroundTintList = ColorStateList.valueOf(color)
                }
                else if(itemPosition == 2){
                    it.backgroundTintList = ColorStateList.valueOf(color)
                }
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        songImage = view.findViewById(R.id.change_style_song_image)
        songImage.setOnClickListener {
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

        playButton = view.findViewById(R.id.change_style_play)
        playButton.setOnClickListener {
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

        nextButton = view.findViewById(R.id.change_style_next)
        nextButton.setOnClickListener {
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

        //End of setting up listeners

        loadInitialStyle()
    }
}