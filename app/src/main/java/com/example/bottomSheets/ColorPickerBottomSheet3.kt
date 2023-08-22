package com.example.bottomSheets

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Size
import com.example.cmp3.R
import yuku.ambilwarna.AmbilWarnaDialog

class ColorPickerBottomSheet3(title: String,
                              @Size(3)
                              labels: Array<String>,
                              @Size(3)
                              colors:IntArray): ColorPickerBottomSheetBase(R.layout.color_picker_bottomsheet_layout3, title, labels, colors) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.change_style_picker_label1).text = "${labels[0]}:"
        val colorView1 = view.findViewById<View>(R.id.change_style_picker_color1)
        colorView1.foregroundTintList = ColorStateList.valueOf(colors[0])
        colorView1.setOnClickListener {
            val colorPickerDialogue = AmbilWarnaDialog(requireContext(), it.foregroundTintList?.defaultColor ?: 0, true,
                object : AmbilWarnaDialog.OnAmbilWarnaListener {
                    override fun onCancel(dialog: AmbilWarnaDialog?) {
                        // leave this function body as
                        // blank, as the dialog
                        // automatically closes when
                        // clicked on cancel button
                    }

                    override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                        // change the mDefaultColor to
                        // change the GFG text color as
                        // it is returned when the OK
                        // button is clicked from the
                        // color picker dialog
                        it.foregroundTintList = ColorStateList.valueOf(color)
                        notifyListener(0, color)
                    }
                })
            colorPickerDialogue.show()
        }

        view.findViewById<TextView>(R.id.change_style_picker_label2).text = "${labels[1]}:"
        val colorView2 = view.findViewById<View>(R.id.change_style_picker_color2)
        colorView2.foregroundTintList = ColorStateList.valueOf(colors[1])
        colorView2.setOnClickListener {
            val colorPickerDialogue = AmbilWarnaDialog(requireContext(), it.foregroundTintList?.defaultColor ?: 0, true,
                object : AmbilWarnaDialog.OnAmbilWarnaListener {
                    override fun onCancel(dialog: AmbilWarnaDialog?) {
                        // leave this function body as
                        // blank, as the dialog
                        // automatically closes when
                        // clicked on cancel button
                    }

                    override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                        // change the mDefaultColor to
                        // change the GFG text color as
                        // it is returned when the OK
                        // button is clicked from the
                        // color picker dialog
                        it.foregroundTintList = ColorStateList.valueOf(color)
                        notifyListener(1, color)
                    }
                })
            colorPickerDialogue.show()
        }

        view.findViewById<TextView>(R.id.change_style_picker_label3).text = "${labels[2]}:"
        val colorView3 = view.findViewById<View>(R.id.change_style_picker_color3)
        colorView3.foregroundTintList = ColorStateList.valueOf(colors[2])
        colorView3.setOnClickListener {
            val colorPickerDialogue = AmbilWarnaDialog(requireContext(), it.foregroundTintList?.defaultColor ?: 0, true,
                object : AmbilWarnaDialog.OnAmbilWarnaListener {
                    override fun onCancel(dialog: AmbilWarnaDialog?) {
                        // leave this function body as
                        // blank, as the dialog
                        // automatically closes when
                        // clicked on cancel button
                    }

                    override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                        // change the mDefaultColor to
                        // change the GFG text color as
                        // it is returned when the OK
                        // button is clicked from the
                        // color picker dialog
                        it.foregroundTintList = ColorStateList.valueOf(color)
                        notifyListener(2, color)
                    }
                })
            colorPickerDialogue.show()
        }
    }
}