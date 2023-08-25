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
import com.example.cmp3.MainActivity
import com.example.cmp3.PlayAllSongsFragment
import com.example.cmp3.R
import com.example.cmp3.SongListView
import com.example.config.GlobalPreferencesConstants

/**
 * Fragment class used in [ChangeStyleActivity] to change [MainActivity] and [SongListView]'s style
 * @param layoutRes resource layout to use when creating the fragment. This parameter must be one of the
 * [SongListStyleFragment]'s available layouts
 */
class SongListStyleFragment(layoutRes: Int) : StyleFragmentBase(layoutRes) {
    private lateinit var generalLayout: ConstraintLayout
    private lateinit var searchButton: ImageView
    private lateinit var optionsButton: ImageView
    private lateinit var selectedTab: ImageView
    private lateinit var unselectedTab: ImageView
    private lateinit var tabIndicator: ImageView
    private lateinit var playAllIcon: ImageView
    private lateinit var playAllText: ImageView
    private lateinit var playAllSongs: ImageView
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
        activity?.getSharedPreferences(GlobalPreferencesConstants.MAIN_ACT_PREFERENCES, Context.MODE_PRIVATE)!!.edit().apply {
            val constants = MainActivity.PreferencesConstants
            putInt(constants.GENERAL_LAYOUT_BG_KEY,
                generalLayout.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_layout_bg))
            putInt(constants.SEARCH_BTN_BG_KEY,
                searchButton.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_bg))
            putInt(constants.SEARCH_BTN_FG_KEY,
                searchButton.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_fg))
            putInt(constants.OPTIONS_BTN_BG_KEY,
                optionsButton.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_bg))
            putInt(constants.OPTIONS_BTN_FG_KEY,
                optionsButton.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_fg))
            putInt(constants.TAB_COLOR_KEY,
                selectedTab.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_tabs_color))
            putInt(constants.SONGS_ITEMS_CONTAINER_BG_KEY,
                itemsContainer.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_layout_bg))
            putInt(constants.SONGS_ITEM_BG_KEY,
                itemLayout.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_layout_bg))
            putInt(constants.SONGS_ITEM_IMG_BG_KEY,
                itemImage.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_image_placeholder_bg))
            putInt(constants.SONGS_ITEM_IMG_FG_KEY,
                itemImage.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_image_placeholder_fg))
            putInt(constants.SONGS_ITEM_TEXT_KEY,
                itemTitle.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_text_color))
            putInt(constants.SONGS_ITEM_BTN_BG_KEY,
                itemOptionsButton.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_bg))
            putInt(constants.SONGS_ITEM_BTN_FG_KEY,
                itemOptionsButton.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_fg))
        }.apply()

        //Play all fragment prefs
        activity?.getSharedPreferences(GlobalPreferencesConstants.PLAY_ALL_FRAGMENT_PREFERENCES, Context.MODE_PRIVATE)!!.edit().apply {
            val constants = PlayAllSongsFragment.PreferencesConstants
            putInt(constants.TEXT_KEY,
                playAllIcon.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_play_all_icon_color))
            putInt(constants.SONGS_KEY,
                playAllSongs.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_play_all_text_color))
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
        requireActivity().getSharedPreferences(GlobalPreferencesConstants.MAIN_ACT_PREFERENCES, Context.MODE_PRIVATE).apply {
            val constants = MainActivity.PreferencesConstants
            generalLayout.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.GENERAL_LAYOUT_BG_KEY,
                    requireContext().getColor(R.color.default_layout_bg)
                )
            )
            searchButton.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.SEARCH_BTN_BG_KEY,
                    requireContext().getColor(R.color.default_buttons_bg)
                )
            )
            searchButton.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.SEARCH_BTN_FG_KEY,
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

            val tabColor = getInt(constants.TAB_COLOR_KEY,
                requireContext().getColor(R.color.default_tabs_color)
            )
            selectedTab.foregroundTintList = ColorStateList.valueOf(tabColor)
            tabIndicator.foregroundTintList = ColorStateList.valueOf(tabColor)
            unselectedTab.foregroundTintList = ColorStateList.valueOf(tabColor).withAlpha(0x40)

            itemsContainer.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.SONGS_ITEMS_CONTAINER_BG_KEY,
                    requireContext().getColor(R.color.default_layout_bg)
                )
            )
            itemLayout.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.SONGS_ITEM_BG_KEY,
                    requireContext().getColor(R.color.default_layout_bg)
                )
            )
            itemImage.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.SONGS_ITEM_IMG_BG_KEY,
                    requireContext().getColor(R.color.default_image_placeholder_bg)
                )
            )
            itemImage.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.SONGS_ITEM_IMG_FG_KEY,
                    requireContext().getColor(R.color.default_image_placeholder_fg)
                )
            )

            val itemTextColor = getInt(constants.SONGS_ITEM_TEXT_KEY,
                requireContext().getColor(R.color.default_text_color)
            )
            itemTitle.foregroundTintList = ColorStateList.valueOf(itemTextColor)
            itemDesc.foregroundTintList = ColorStateList.valueOf(itemTextColor)

            itemOptionsButton.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.SONGS_ITEM_BTN_BG_KEY,
                    requireContext().getColor(R.color.default_buttons_bg)
                )
            )
            itemOptionsButton.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.SONGS_ITEM_BTN_FG_KEY,
                    requireContext().getColor(R.color.default_buttons_fg)
                )
            )
        }

        //Play all fragment prefs
        requireActivity().getSharedPreferences(GlobalPreferencesConstants.PLAY_ALL_FRAGMENT_PREFERENCES, Context.MODE_PRIVATE).apply {
            val constants = PlayAllSongsFragment.PreferencesConstants

            val iconColor = getInt(constants.TEXT_KEY,
                requireContext().getColor(R.color.default_play_all_icon_color)
            )
            playAllIcon.foregroundTintList = ColorStateList.valueOf(iconColor)
            playAllText.foregroundTintList = ColorStateList.valueOf(iconColor)

            playAllSongs.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.SONGS_KEY,
                    requireContext().getColor(R.color.default_play_all_text_color)
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

        selectedTab = view.findViewById(R.id.change_style_tab_selected)
        unselectedTab = view.findViewById(R.id.change_style_tab_unselected)
        tabIndicator = view.findViewById(R.id.change_style_tab_indicator)

        view.findViewById<ConstraintLayout>(R.id.change_style_tab_layout).setOnClickListener{
            val picker = ColorPickerBottomSheet1("Tabs",
                arrayOf("Color"),
                intArrayOf(selectedTab.foregroundTintList?.defaultColor ?: 0))
            picker.setOnColorSelectedListener { _, color ->
                selectedTab.foregroundTintList = ColorStateList.valueOf(color)
                unselectedTab.foregroundTintList = ColorStateList.valueOf(color).withAlpha(0x40)
                tabIndicator.foregroundTintList = ColorStateList.valueOf(color)
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        playAllIcon = view.findViewById(R.id.change_style_play_all_icon)
        playAllText = view.findViewById(R.id.change_style_play_all_text)
        playAllSongs = view.findViewById(R.id.change_style_play_all_songs)

        view.findViewById<ConstraintLayout>(R.id.change_style_play_all_layout).setOnClickListener{
            val picker = ColorPickerBottomSheet2("Play all button",
                arrayOf("Text color", "Songs color"),
                intArrayOf((playAllText.foregroundTintList?.defaultColor ?: 0), (playAllSongs.foregroundTintList?.defaultColor ?: 0)))
            picker.setOnColorSelectedListener { itemPosition, color ->
                if(itemPosition == 0){
                    playAllText.foregroundTintList = ColorStateList.valueOf(color)
                    playAllIcon.foregroundTintList = ColorStateList.valueOf(color)
                }
                else if(itemPosition == 1){
                    playAllSongs.foregroundTintList = ColorStateList.valueOf(color)
                }
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        searchButton = view.findViewById(R.id.change_style_search)
        searchButton.setOnClickListener {
            val picker = ColorPickerBottomSheet2("Search button",
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