package com.example.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.cmp3.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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

    public fun interface OnColorSelectedListener{
        fun listen(itemPosition: Int, color: Int)
    }

}