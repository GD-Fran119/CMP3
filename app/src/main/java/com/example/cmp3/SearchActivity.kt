package com.example.cmp3

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.config.GlobalPreferencesConstants
import com.example.config.MainActivityPreferencesConstants
import com.example.recyclerviewAdapters.SearchSongAdapter
import com.example.recyclerviewAdapters.SongArrayAdapter
import com.example.songsAndPlaylists.MainListHolder
import com.google.android.material.button.MaterialButton

class SearchActivity : AppCompatActivity() {

    companion object{
        private const val FULL_WIDTH_LAYOUT = 1
        private const val HORIZONTAL_CARD_LAYOUT = 2
        private const val VERTICAL_CARD_LAYOUT = 3
    }

    private lateinit var editText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchSongAdapter

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

                    R.id.playlist_change_style -> Toast.makeText(
                        this@SearchActivity,
                        "Change style",
                        Toast.LENGTH_SHORT
                    ).show()

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

    private fun checkAndSetupRecyclerView() {

        val prefs = getSharedPreferences(ChangeLayoutActivity.SEARCH_ACT_PREFERENCES, Context.MODE_PRIVATE)
        var savedLayout = prefs.getInt(GlobalPreferencesConstants.LAYOUT_KEY, -1)

        //No config
        if(savedLayout == -1) {
            prefs.edit().apply {
                putInt(GlobalPreferencesConstants.LAYOUT_KEY, 1)
            }.apply()

            savedLayout = 1
        }

        if(currentLayout != savedLayout){
            currentLayout = if(savedLayout !in 1..3) 1
                            else savedLayout

            prefs?.edit().apply {
                this!!.putInt(GlobalPreferencesConstants.LAYOUT_KEY, currentLayout)
            }?.apply()

            recyclerView = findViewById(R.id.search_recyclerview)
            recyclerView.setHasFixedSize(true)

            val manager: RecyclerView.LayoutManager
            when(currentLayout){
                FULL_WIDTH_LAYOUT -> {
                    manager = LinearLayoutManager(this)
                    adapter = SearchSongAdapter(this, MainListHolder.getMainList().getList(), supportFragmentManager, R.layout.item_song_list_view1)
                }
                HORIZONTAL_CARD_LAYOUT -> {
                    manager = LinearLayoutManager(this)
                    adapter = SearchSongAdapter(this, MainListHolder.getMainList().getList(), supportFragmentManager, R.layout.item_song_list_view2)
                }
                VERTICAL_CARD_LAYOUT -> {
                    manager = GridLayoutManager(this, 2)
                    adapter = SearchSongAdapter(this, MainListHolder.getMainList().getList(), supportFragmentManager, R.layout.item_song_list_view3)
                }
                else -> {
                    manager = LinearLayoutManager(this)
                    adapter = SearchSongAdapter(this, MainListHolder.getMainList().getList(), supportFragmentManager, R.layout.item_song_list_view1)
                    currentLayout = 1
                    prefs?.edit().apply {
                        this!!.putInt(GlobalPreferencesConstants.LAYOUT_KEY, currentLayout)
                    }?.apply()
                }
            }

            adapter.filterDataset(editText.text.toString())

            recyclerView.layoutManager = manager
            recyclerView.adapter = adapter
        }
    }
}