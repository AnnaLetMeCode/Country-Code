package com.country.emoji.ui.list

data class CountryItem(
    val countryCode: Int,
    val codeText: String,
    val isCodeVisible: Boolean,
    val flag: String,
    val countryName: String,
    val typeface: Int
)
