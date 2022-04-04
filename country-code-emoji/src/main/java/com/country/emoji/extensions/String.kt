package com.country.emoji.extensions

/**
 * @param iso Country ISO code  which will be split  to convert to symbol letter
 * @return Emoji symbol
 */
fun String.toEmojiFrag(): String {
    return if (this.isNotEmpty()) {
        val flagOffset = 0x1F1E6
        val asciiOffset = 0x41
        val firstChar = Character.codePointAt(this, 0) - asciiOffset + flagOffset
        val secondChar = Character.codePointAt(this, 1) - asciiOffset + flagOffset
        (String(Character.toChars(firstChar)) + String(Character.toChars(secondChar)))
    } else this
}
