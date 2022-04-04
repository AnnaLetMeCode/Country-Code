package com.country.example

import android.app.UiModeManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.country.emoji.model.CountryListParams
import com.country.emoji.ui.list.ChooseCountryCodeFragmentDialog
import com.country.example.databinding.FragmentSampleBinding
import java.util.Locale

class SampleFragment : Fragment() {

    private lateinit var binding: FragmentSampleBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            cvvChooseCountry.setDefaultCountry(Locale.getDefault())
            cvvChooseCountry.setOnClickListener {
                val param = CountryListParams(
                    dayNightMode = UiModeManager.MODE_NIGHT_YES,
                    enableSearch = cbSearch.isChecked,
                    displayCountryCode = true,
                    excludeMode = cbExcludeMode.isChecked,
                    filter = arrayListOf(
                        Locale.FRANCE.country,
                        Locale.ENGLISH.country,
                        Locale.JAPAN.country
                    ),
                    country = cvvChooseCountry.countryCode
                )

                val dialog = ChooseCountryCodeFragmentDialog.newInstance(param) {
                    cvvChooseCountry.value = it
                    edPhone.formatNumber = it.formatMask
                }
                dialog.show(
                    childFragmentManager,
                    ChooseCountryCodeFragmentDialog::class.java.canonicalName
                )
            }

            edPhone.formatNumber = cvvChooseCountry.value.formatMask
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSampleBinding.inflate(inflater).apply { binding = this }.root
}
