package com.country.emoji.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
    @SerializedName("format_mask")
    val formatMask: String,
    @SerializedName("country_code")
    val countryCode: Int,
    @SerializedName("iso")
    val iso: String,
    @SerializedName("min_length")
    val minLength: Int,
    @SerializedName("max_length")
    val maxLength: Int,
    @SerializedName("title")
    val titleDefault: String,
    @Expose(serialize = false, deserialize = false)
    var titleLocal: String,
    @Expose(serialize = false, deserialize = false)
    var codeLocal: String,
    @Expose(serialize = false, deserialize = false)
    var flag: String
) : Parcelable
