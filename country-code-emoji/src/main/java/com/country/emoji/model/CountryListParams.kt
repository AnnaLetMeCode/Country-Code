package com.country.emoji.model

import android.os.Parcelable
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.parcelize.Parcelize
import java.util.Locale

@Parcelize
data class CountryListParams(
    val dayNightMode: Int = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
    val enableSearch: Boolean = true,
    val displayCountryCode: Boolean = true,
    val title: String? = null,
    val filter: List<String> = emptyList(),
    val excludeMode: Boolean = true,
    val country: String = Locale.getDefault().country
) : Parcelable
