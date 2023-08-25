package com.example.bottomSheets

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.Size
import com.example.cmp3.R
import yuku.ambilwarna.AmbilWarnaDialog

/**
 * Class to generate a bottom sheet that asks for 1 single color
 * @param title title for bottom sheet
 * @param labels labels that will be shown when displaying the bottom sheet. Must have size 1
 * @param colors colors that will be shown with their corresponding label when displaying the bottom sheet. Must have size 1
 */
class ColorPickerBottomSheet1(title: String,
                              @Size(1)
                              labels: Array<String>,
                              @Size(1)
                              colors: IntArray): ColorPickerBottomSheetBase(R.layout.color_picker_bottomsheet_layout1, title, labels, colors) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO
        //Set labels, colors and click listeners
        view.findViewById<TextView>(R.id.change_style_picker_label1).text = "${labels[0]}:"

        val colorView = view.findViewById<View>(R.id.change_style_picker_color1)
        colorView.foregroundTintList = ColorStateList.valueOf(colors[0])
        colorView.setOnClickListener {
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

    }
}