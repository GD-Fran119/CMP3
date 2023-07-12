package com.example.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.cmp3.AddSongsToPlaylistActivity
import com.example.cmp3.R
import com.example.databaseStuff.AppDatabase
import com.example.databaseStuff.PlaylistEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Date

class PlaylistCreationDialog: DialogFragment() {

    private lateinit var view : View
    private set

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val dialog = activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the create_playlist_dialog_view inflater
            val inflater = requireActivity().layoutInflater;

            // Inflate and set the create_playlist_dialog_view for the dialog
            // Pass null as the parent view because its going in the dialog create_playlist_dialog_view
            view = inflater.inflate(R.layout.create_playlist_dialog_view, null)
            builder.setView(view)
                .setTitle("Create new playlist")
                // Add action buttons
                .setPositiveButton("Create",null)
                .setNegativeButton("Cancel", null)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

        dialog.setOnShowListener{
            //Establish positive button with empty input check
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener{
                _ ->
                val input = view.findViewById<EditText>(R.id.newPlaylistName).text.trim().toString()
                if(input.isEmpty()){
                    view.findViewById<TextView>(R.id.newPlaylistNameRequiredInput).visibility = View.VISIBLE
                }
                else{
                    //Do important stuff for creating new playlist
                    CoroutineScope(Dispatchers.Default).launch {
                        val dao = AppDatabase.getInstance(activity as Context).playlistDao()
                        val date = LocalDate.now().toString()
                        val newPlaylist = PlaylistEntity(input, date)
                        val id = dao.insertPlaylist(newPlaylist)

                        val intent = Intent(activity, AddSongsToPlaylistActivity::class.java)
                        intent.putExtra("id", id.toInt())
                        startActivity(intent)

                        dismiss()
                    }
                }
            }
        }
        return dialog

    }

}