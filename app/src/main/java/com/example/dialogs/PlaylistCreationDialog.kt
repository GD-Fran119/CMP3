package com.example.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

/**
 * Dialog shown when creating new playlist. Requests for the new playlist name, creates the playlist if name is confirmed by the user
 * and starts [AddSongsToPlaylistActivity]
 */
class PlaylistCreationDialog: DialogFragment() {

    private lateinit var view : View

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
            //Show keyboard for input when dialog is shown
            view.findViewById<EditText>(R.id.new_playlist_name).requestFocus()
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            //Establish positive button with empty input check
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener{
                _ ->
                val input = view.findViewById<EditText>(R.id.new_playlist_name).text.trim().toString()
                if(input.isEmpty()){
                    view.findViewById<TextView>(R.id.new_playlist_name_required_input).visibility = View.VISIBLE
                }
                else{
                    //Do important stuff for creating new playlist
                    CoroutineScope(Dispatchers.Default).launch {
                        val dao = AppDatabase.getInstance(activity as Context).playlistDao()
                        val date = if(Build.VERSION.SDK_INT < 26)
                                    SimpleDateFormat("yyyy-MM-dd").format(Date())
                                    else LocalDate.now().toString()
                        val newPlaylist = PlaylistEntity(input, date)
                        val id = dao.insertPlaylist(newPlaylist)

                        val intent = Intent(activity, AddSongsToPlaylistActivity::class.java)
                        intent.putExtra(AddSongsToPlaylistActivity.PLAYLIST_ID_KEY, id.toInt())
                        startActivity(intent)

                        dismiss()
                    }
                }
            }
        }
        return dialog
    }
}