package com.example.cmp3.changeStyleFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cmp3.ChangeStyleActivity

/**
 * Base fragment class that declares a general behaviour for derived classes used in [ChangeStyleActivity].
 * @param layoutRes resource layout from which inflate the fragment view
 */
abstract class StyleFragmentBase(private val layoutRes: Int): Fragment() {
    /**
     * Method used to save changes made in the UI style. This method will ensure changes are stored so those Activities
     * that can change their style can do it properly
     */
    abstract fun saveChanges()

    /**
     * Method used to load initial style for the fragment. This method establishes the initial colors that are currently in use
     * by the UI so the user can know which style the activity has
     */
    abstract fun loadInitialStyle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes, container, false)
    }
}