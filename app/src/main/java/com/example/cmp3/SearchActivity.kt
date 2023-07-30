package com.example.cmp3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewAdapters.SearchSongAdapter
import com.example.songsAndPlaylists.MainListHolder
import com.google.android.material.button.MaterialButton

class SearchActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        findViewById<MaterialButton>(R.id.search_back_button).setOnClickListener {
            onBackPressed()
        }

        val adapter = SearchSongAdapter(this, MainListHolder.getMainList().getList(), supportFragmentManager)
        val recyclerView = findViewById<RecyclerView>(R.id.search_recyclerview)
        recyclerView.adapter = adapter

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
}