package com.country.emoji.ui.list

import com.country.emoji.model.Country
import java.util.Locale

class ChooseCountryView {
    data class Data(
        val items: List<Country> = emptyList(),
        val enableSearch: Boolean = true,
        val displayCountryCode: Boolean = true,
        val title: String? = null,
        val filter: List<String> = emptyList(),
        val excludeMode: Boolean = true,
        val country: String = Locale.getDefault().country
    )

    data class Result(
        val country: Country? = null
    )
}
