package com.example.cmp3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.config.GlobalPreferencesConstants
import com.example.recyclerviewAdapters.SearchSongAdapter
import com.example.songsAndPlaylists.MainListHolder
import com.google.android.material.button.MaterialButton

/**
 * Activity for searching and filtering device songs
 */
class SearchActivity : AppCompatActivity() {

    companion object{
        /**
         * Constant which refers to available layout 1
         */
        private const val FULL_WIDTH_LAYOUT = 1
        /**
         * Constant which refers to available layout 2
         */
        private const val HORIZONTAL_CARD_LAYOUT = 2
        /**
         * Constant which refers to available layout 3
         */
        private const val VERTICAL_CARD_LAYOUT = 3
    }

    private lateinit var editText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchSongAdapter

    //Custom established layout
    private var currentLayout = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        findViewById<MaterialButton>(R.id.topbar_back_button).setOnClickListener {
            onBackPressed()
        }

        findViewById<MaterialButton>(R.id.topbar_options_button).setOnClickListener {
            val popup = PopupMenu(this, it)
            popup.menuInflater.inflate(R.menu.customization_menu, popup.menu)
            popup.show()

            popup.setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.playlist_change_layout -> {
                        val intent = Intent(this@SearchActivity, ChangeLayoutActivity::class.java)
                        intent.putExtra(ChangeLayoutActivity.ACTIVITY_LAYOUT_CHANGE, ChangeLayoutActivity.SEARCH_ACTIVITY_LAYOUT)
                        startActivity(intent)
                    }

                    R.id.playlist_change_style -> {
                        val intent = Intent(this@SearchActivity, ChangeStyleActivity::class.java)
                        intent.putExtra(ChangeStyleActivity.ACTIVITY_STYLE_CHANGE, ChangeStyleActivity.SEARCH_ACTIVITY)
                        startActivity(intent)
                    }

                }
                true
            }
        }

        editText = findViewById(R.id.search_text)

        editText.addTextChangedListener(object: TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filterDataset(s.toString())
                recyclerView.scrollToPosition(0)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        findViewById<Button>(R.id.search_clear_button).setOnClickListener{
            editText.text.clear()
        }
    }

    override fun onStart() {
        super.onStart()
        checkAndSetupRecyclerView()
    }

    /**
     * Checks whether the current layout has been changed. If so, the new layout is set
     */
    private fun checkAndSetupRecyclerView() {

        val prefs = getSharedPreferences(GlobalPreferencesConstants.SEARCH_ACT_PREFERENCES, Context.MODE_PRIVATE)
        var savedLayout = prefs.getInt(GlobalPreferencesConstants.LAYOUT_KEY, -1)

        //No config
        if(savedLayout == -1) {
            prefs.edit().apply {
                putInt(GlobalPreferencesConstants.LAYOUT_KEY, 1)
            }.apply()

            savedLayout = 1
        }

        if(currentLayout == savedLayout) return

        currentLayout = if(savedLayout !in 1..3) 1
                        else savedLayout

        prefs?.edit().apply {
            this!!.putInt(GlobalPreferencesConstants.LAYOUT_KEY, currentLayout)
        }?.apply()

        recyclerView = findViewById(R.id.search_recyclerview)
        recyclerView.setHasFixedSize(true)

        val manager: RecyclerView.LayoutManager
        val layoutRes: Int

        when(currentLayout){
            FULL_WIDTH_LAYOUT -> {
                manager = LinearLayoutManager(this)
                layoutRes = R.layout.item_song_list_view1
            }
            HORIZONTAL_CARD_LAYOUT -> {
                manager = LinearLayoutManager(this)
                layoutRes = R.layout.item_song_list_view2
            }
            VERTICAL_CARD_LAYOUT -> {
                manager = GridLayoutManager(this, 2)
                layoutRes = R.layout.item_song_list_view3
            }
            else -> {
                manager = LinearLayoutManager(this)
                layoutRes = R.layout.item_song_list_view1
                currentLayout = 1
                prefs?.edit().apply {
                    this!!.putInt(GlobalPreferencesConstants.LAYOUT_KEY, currentLayout)
                }?.apply()
            }
        }

        adapter = SearchSongAdapter(this, MainListHolder.getMainList().getList(), supportFragmentManager, layoutRes)
        adapter.filterDataset(editText.text.toString())

        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter

    }

    /**
     * Class that stores keys for [SearchActivity]'s [SharedPreferences]
     */
    class PreferencesConstants private constructor(){
        companion object{
            /**
             * Key that refers to Activity style version
             */
            const val STYLE_VERSION_KEY = "version"

            /**
             * Key that refers to Activity background color
             */
            const val GENERAL_LAYOUT_BG_KEY = "general_layout_bg_color"

            /**
             * Key that refers to Activity back button foreground color
             */
            const val BACK_BTN_FG_KEY = "back_button_fg_color"

            /**
             * Key that refers to Activity back button background color
             */
            const val BACK_BTN_BG_KEY = "back_button_bg_color"

            /**
             * Key that refers to Activity options button foreground color
             */
            const val OPTIONS_BTN_FG_KEY = "options_button_fg_color"

            /**
             * Key that refers to Activity options button background color
             */
            const val OPTIONS_BTN_BG_KEY = "options_button_bg_color"

            /**
             * Key that refers to Activity search box text color
             */
            const val SEARCH_BOX_KEY = "search_box_text_color"

            /**
             * Key that refers to Activity clear button text color
             */
            const val CLEAR_BTN_KEY = "clear_button_text_color"

            /**
             * Key that refers to Activity items container background color
             */
            const val ITEMS_CONTAINER_BG_KEY = "items_container_color"

            /**
             * Key that refers to Activity item background color
             */
            const val ITEM_BG_KEY = "item_bg_color"

            /**
             * Key that refers to Activity item image placeholder foreground color
             */
            const val ITEM_IMG_FG_KEY = "item_image_fg_color"

            /**
             * Key that refers to Activity item image placeholder background color
             */
            const val ITEM_IMG_BG_KEY = "item_image_bg_color"

            /**
             * Key that refers to Activity item text color
             */
            const val ITEM_TEXT_KEY = "item_text_color"

            /**
             * Key that refers to Activity item options button foreground color
             */
            const val ITEM_BTN_FG_KEY = "item_button_fg_color"

            /**
             * Key that refers to Activity item options button background color
             */
            const val ITEM_BTN_BG_KEY = "item_button_bg_color"
        }
    }
}