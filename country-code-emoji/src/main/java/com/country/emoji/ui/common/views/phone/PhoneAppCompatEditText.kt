package com.country.emoji.ui.common.views.phone

import android.content.Context
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class PhoneAppCompatEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var watcher: NumberFormatWatcher =
        NumberFormatWatcher()

    init {
        inputType = InputType.TYPE_CLASS_PHONE;
        keyListener = DigitsKeyListener.getInstance(INPUT_CHARS);
        addTextChangedListener(watcher)
    }

    var formatNumber: String
        get() = watcher.format
        set(value) {
            watcher.format = value.replace("C", "").trim()
            hint = watcher.format
            setText(text.toString())
        }

    companion object{
        const val INPUT_CHARS : String = "0123456789-"
    }
}





