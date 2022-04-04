package com.country.emoji.ui.common.views.country

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.country.emoji.R
import com.country.emoji.data.CountriesProvider
import com.country.emoji.databinding.ViewCountryCodeBinding
import com.country.emoji.extensions.toEmojiFrag
import com.country.emoji.model.Country
import java.util.Locale

class CountryCodeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyle, defStyleRes) {

    private lateinit var binding: ViewCountryCodeBinding
    private var mode: Int = 3
    private var provider: CountriesProvider =
        CountriesProvider(context)

    init {

        binding = ViewCountryCodeBinding.inflate(LayoutInflater.from(context), this, true)
        attrs?.let { attr: AttributeSet ->

            val typedArray = context.obtainStyledAttributes(
                attr,
                R.styleable.CountryCodeView, 0, 0
            )

            val textSize = convertPixelsToDp(
                typedArray.getDimension(
                    R.styleable.CountryCodeView_ccv_textSize,
                    14f
                ),
                context
            )
            if (textSize > 0) {
                binding.tvCcpText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
            }

            val flagSize = convertPixelsToDp(
                typedArray.getDimension(
                    R.styleable.CountryCodeView_ccv_flagSize,
                    16f
                ),
                context
            )
            if (flagSize > 0) {
                binding.tvCcpFlag.setTextSize(TypedValue.COMPLEX_UNIT_SP, flagSize)
            }

            val showArrow = typedArray
                .getBoolean(
                    R.styleable
                        .CountryCodeView_ccv_showArrow,
                    true
                )

            if (!showArrow) {
                binding.tvCcpFlag.setCompoundDrawables(null, null, null, null)
                binding.tvCcpFlag.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }

            initDefaultCountry(typedArray.getString(R.styleable.CountryCodeView_ccv_defaultCountry))

            mode = typedArray
                .getInt(
                    R.styleable
                        .CountryCodeView_ccv_mode,
                    mode
                )

            typedArray.recycle()
        }
    }

    private fun initDefaultCountry(default: String?) {
        var locale: Locale? = null
        default?.let {
            locale = Locale(it)
        }
        locale?.let {
            if (it.country.isNullOrEmpty()) {
                setDefaultCountry(it)
                return@let
            }
        }
        setDefaultCountry(Locale.getDefault())
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (
            context.resources
                .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT
            )
    }

    fun setDefaultCountry(locale: Locale) {
        provider.findCountryByCode(locale.country)?.let {
            value = it
        }
    }

    private lateinit var country: Country

    var value: Country
        get() = country
        set(value) {
            country = value
            binding.tvCcpText.text = when (mode) {
                CODE -> "+${value.codeLocal}"
                FLAG -> ""
                CODE_FLAG -> "+${value.codeLocal}"
                else -> "${value.titleLocal} +${value.codeLocal}"
            }
            binding.tvCcpFlag.text = when (mode) {
                CODE -> ""
                FLAG -> value.iso.toEmojiFrag()
                CODE_FLAG -> value.iso.toEmojiFrag()
                else -> ""
            }
        }

    val countryCode: String
        get() = country.iso

    companion object {
        private const val FLAG = 1
        private const val CODE = 2
        private const val CODE_FLAG = 3
        private const val CODE_TITLE = 4
    }
}
