package com.example.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.cmp3.AddSongsToPlaylistActivity
import com.example.cmp3.R
import com.example.databaseStuff.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Dialog shown when creating new playlist. Requests for the new playlist name and renames the playlist if name is confirmed by the user
 */
class PlaylistRenameDialog(private val playlistId: Int): DialogFragment() {
    private lateinit var view : View
    private lateinit var dialog: AlertDialog
    var onConfirmationListener : OnConfirmListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        dialog = activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the create_playlist_dialog_view inflater
            val inflater = requireActivity().layoutInflater;

            // Inflate and set the create_playlist_dialog_view for the dialog
            // Pass null as the parent view because its going in the dialog create_playlist_dialog_view
            view = inflater.inflate(R.layout.create_playlist_dialog_view, null)
            builder.setView(view)
                .setTitle("Rename playlist")
                // Add action buttons
                .setPositiveButton("Rename",null)
                .setNegativeButton("Cancel", null)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

        dialog.setOnShowListener{
            //Show keyboard for input when dialog is shown
            view.findViewById<EditText>(R.id.new_playlist_name).requestFocus()
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

                val input = getInput()
                if (input == "") {
                    view.findViewById<TextView>(R.id.new_playlist_name_required_input)?.visibility =
                        View.VISIBLE
                } else {

                    CoroutineScope(Dispatchers.Default).launch {
                        val dao = AppDatabase.getInstance(requireActivity()).playlistDao()
                        dao.updatePlaylistName(playlistId, input)
                    }

                    Toast.makeText(requireContext(), "Playlist renamed", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    onConfirmationListener?.notifyConfirmation()
                }
            }
        }

        return dialog

    }

    /**
     * Retrieves the text of the text input used to get the playlist new name
     * @return text written in the [EditText] used to get the playlist new name
     */
    fun getInput() = view.findViewById<EditText>(R.id.new_playlist_name).text.trim().toString()

    /**
     * Interface to implement if confirmation notification is required when user accepts the playlist new name
     */
    interface OnConfirmListener{
        /**
         * Method invoked when the user confirms the playlist new name
         */
        fun notifyConfirmation()
    }
}
