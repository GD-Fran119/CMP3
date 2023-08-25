package com.example.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.cmp3.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Base class to generate a bottom sheet that ask for colors
 * @param layoutRes resource layout to inflate the view
 * @param title title for bottom sheet
 * @param labels labels that will be shown when displaying the bottom sheet
 * @param colors colors that will be shown with their corresponding label when displaying the bottom sheet. Must have same size as [labels]
 */
abstract class ColorPickerBottomSheetBase(
    private val layoutRes: Int,
    protected val title: String,
    protected val labels: Array<String>,
    protected val colors: IntArray): BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.change_style_picker_title).text = title
    }

    /**
     * Notifies listener if set
     * @param itemPosition position of the bottom sheet item that has changed its color
     * @param color color to which the bottom sheet item has set its value
     */
    protected fun notifyListener(itemPosition: Int, color: Int){
        listener?.listen(itemPosition, color)
    }

    private var listener: OnColorSelectedListener? = null


    //TODO
    fun setOnColorSelectedListener(newListener: OnColorSelectedListener?){
        listener = newListener
    }

    fun setOnColorSelectedListener(newListener: (itemPosition: Int, color: Int) -> Unit){
        listener = OnColorSelectedListener(newListener)
    }

    /**
     * Interface for OnColorSelected event
     */
    fun interface OnColorSelectedListener{
        /**
         * Method to invoke when an item has changed its color
         * @param itemPosition position of the bottom sheet item that has changed its color
         * @param color color to which the bottom sheet item has set its value
         */
        fun listen(itemPosition: Int, color: Int)
    }

}