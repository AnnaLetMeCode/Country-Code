package com.country.emoji.ui.common.views.phone

import android.text.Editable
import android.text.TextWatcher

/**
 * Format text by selected mask
 */
class NumberFormatWatcher(var format: String = "") : TextWatcher {

    private var isRunning = false
    private var isDeleting = false

    private fun unmaskText(s: String): String {
        return s.replace("[.]".toRegex(), "").replace("[-]".toRegex(), "")
            .replace("[/]".toRegex(), "").replace("[(]".toRegex(), "")
            .replace("[)]".toRegex(), "").replace(" ".toRegex(), "")
            .replace(",".toRegex(), "")
    }

    private fun maskText(text: String): String {
        var i = 0
        var mascara = ""
        for (m in format.toCharArray()) {
            if (m != 'X') {
                mascara += m
                continue
            }
            mascara += try {
                text[i]
            } catch (e: Exception) {
                break
            }
            i++
        }
        return mascara
    }

    override fun afterTextChanged(editable: Editable?) {
        if (isRunning || isDeleting || format.isEmpty()) {
            return
        }
        isRunning = true
        editable?.let {
            val text = unmaskText(editable.toString())
            editable.clear()
            editable.append(maskText(text))
        }
        isRunning = false
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        isDeleting = count > after
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}
