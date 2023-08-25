package com.example.cmp3.changeStyleFragments

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.bottomSheets.ColorPickerBottomSheet1
import com.example.bottomSheets.ColorPickerBottomSheet2
import com.example.cmp3.ChangeStyleActivity
import com.example.cmp3.PlayControlView
import com.example.cmp3.R
import com.example.config.GlobalPreferencesConstants

/**
 * Fragment class used in [ChangeStyleActivity] to change [PlayControlView]'s style
 * @param layoutRes resource layout to use when creating the fragment. This parameter must be one of the
 * [PlayControlStyleFragment]'s available layouts
 */
class PlayControlStyleFragment(layoutRes: Int): StyleFragmentBase(layoutRes) {
    private lateinit var generalLayout: ConstraintLayout
    private lateinit var backButton: ImageView
    private lateinit var optionsButton: ImageView
    private lateinit var songImage: ImageView
    private lateinit var songTitle: ImageView
    private lateinit var songDesc: ImageView
    private lateinit var seekBar: ImageView
    private lateinit var playButton: ImageView
    private lateinit var nextButton: ImageView
    private lateinit var previousButton: ImageView
    private lateinit var playModeButton: ImageView
    private lateinit var listButton: ImageView

    override fun saveChanges() {
        activity?.getSharedPreferences(GlobalPreferencesConstants.PLAY_CONTROL_ACT_PREFERENCES, Context.MODE_PRIVATE)!!.edit().apply {
            val constants = PlayControlView.PreferencesConstants
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
            putInt(constants.SONG_TEXT_KEY,
                songTitle.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_play_control_text_color))
            putInt(constants.SONG_IMAGE_BG_KEY,
                songImage.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_image_placeholder_bg))
            putInt(constants.SONG_IMAGE_FG_KEY,
                songImage.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_image_placeholder_fg))
            putInt(constants.SEEK_BAR_KEY,
                seekBar.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_seek_bar_color))
            putInt(constants.PLAY_BTN_BG_KEY,
                playButton.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_bg))
            putInt(constants.PLAY_BTN_FG_KEY,
                playButton.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_fg))
            putInt(constants.PLAY_MODE_BTN_BG_KEY,
                playModeButton.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_bg))
            putInt(constants.PLAY_MODE_BTN_FG_KEY,
                playModeButton.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_fg))
            putInt(constants.LIST_BTN_BG_KEY,
                listButton.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_bg))
            putInt(constants.LIST_BTN_FG_KEY,
                listButton.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_fg))
            putInt(constants.PREVIOUS_BTN_BG_KEY,
                previousButton.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_bg))
            putInt(constants.PREVIOUS_BTN_FG_KEY,
                previousButton.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_fg))
            putInt(constants.NEXT_BTN_BG_KEY,
                nextButton.backgroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_bg))
            putInt(constants.NEXT_BTN_FG_KEY,
                nextButton.foregroundTintList?.defaultColor ?: requireContext().getColor(R.color.default_buttons_fg))
        }.apply()
    }

    override fun loadInitialStyle() {
        requireActivity().getSharedPreferences(GlobalPreferencesConstants.PLAY_CONTROL_ACT_PREFERENCES, Context.MODE_PRIVATE).apply {
            val constants = PlayControlView.PreferencesConstants
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

            val songInfoColor = getInt(constants.SONG_TEXT_KEY,
                                        requireContext().getColor(R.color.default_play_control_text_color)
                                        )
            songTitle.foregroundTintList = ColorStateList.valueOf(songInfoColor)
            songDesc.foregroundTintList = ColorStateList.valueOf(songInfoColor)

            songImage.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.SONG_IMAGE_BG_KEY,
                    requireContext().getColor(R.color.default_image_placeholder_bg)
                )
            )
            songImage.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.SONG_IMAGE_FG_KEY,
                    requireContext().getColor(R.color.default_image_placeholder_fg)
                )
            )
            seekBar.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.SEEK_BAR_KEY,
                    requireContext().getColor(R.color.default_seek_bar_color)
                )
            )
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
            playModeButton.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.PLAY_MODE_BTN_BG_KEY,
                    requireContext().getColor(R.color.default_buttons_bg)
                )
            )
            playModeButton.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.PLAY_MODE_BTN_FG_KEY,
                    requireContext().getColor(R.color.default_buttons_fg)
                )
            )
            listButton.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.LIST_BTN_BG_KEY,
                    requireContext().getColor(R.color.default_buttons_bg)
                )
            )
            listButton.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.LIST_BTN_FG_KEY,
                    requireContext().getColor(R.color.default_buttons_fg)
                )
            )
            previousButton.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.PREVIOUS_BTN_BG_KEY,
                    requireContext().getColor(R.color.default_buttons_bg)
                )
            )
            previousButton.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.PREVIOUS_BTN_FG_KEY,
                    requireContext().getColor(R.color.default_buttons_fg)
                )
            )
            nextButton.backgroundTintList = ColorStateList.valueOf(
                getInt(constants.NEXT_BTN_BG_KEY,
                    requireContext().getColor(R.color.default_buttons_bg)
                )
            )
            nextButton.foregroundTintList = ColorStateList.valueOf(
                getInt(constants.NEXT_BTN_FG_KEY,
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

        songImage = view.findViewById(R.id.change_style_song_image)
        songImage.setOnClickListener {
            val picker = ColorPickerBottomSheet2("Song image placeholder",
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

        view.findViewById<ConstraintLayout>(R.id.change_style_song_info_container).setOnClickListener {
            val picker = ColorPickerBottomSheet1("Song info",
                arrayOf("Text color"),
                intArrayOf((songTitle.foregroundTintList?.defaultColor ?: 0)))
            picker.setOnColorSelectedListener { itemPosition, color ->
                if(itemPosition == 0){
                    songTitle.foregroundTintList = ColorStateList.valueOf(color)
                    songDesc.foregroundTintList = ColorStateList.valueOf(color)
                }
                else if(itemPosition == 1){
                    it.backgroundTintList = ColorStateList.valueOf(color)
                }
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        seekBar = view.findViewById(R.id.change_style_seekbar)
        seekBar.setOnClickListener {
            val picker = ColorPickerBottomSheet1("Seekbar",
                arrayOf("Seekbar color"),
                intArrayOf((it.foregroundTintList?.defaultColor ?: 0)))
            picker.setOnColorSelectedListener { _, color ->
                it.foregroundTintList = ColorStateList.valueOf(color)
            }
            picker.show(childFragmentManager, "Bottom sheet")
        }

        playButton = view.findViewById(R.id.change_style_play_button)
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

        nextButton = view.findViewById(R.id.change_style_next_button)
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

        previousButton = view.findViewById(R.id.change_style_previous_button)
        previousButton.setOnClickListener {
            val picker = ColorPickerBottomSheet2("Previous button",
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

        listButton = view.findViewById(R.id.change_style_list_items_button)
        listButton.setOnClickListener {
            val picker = ColorPickerBottomSheet2("List button",
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

        playModeButton = view.findViewById(R.id.change_style_play_mode_button)
        playModeButton.setOnClickListener {
            val picker = ColorPickerBottomSheet2("Play mode button",
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