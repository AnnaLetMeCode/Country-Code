package com.country.emoji.ui.list

import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.viewbinding.ViewBinding
import com.country.emoji.R
import com.country.emoji.databinding.FragmentChooseCountryBinding
import com.country.emoji.extensions.FragmentViewBindingDelegate
import com.country.emoji.extensions.doOnQueryChanged
import com.country.emoji.extensions.subscribe
import com.country.emoji.model.Country
import com.country.emoji.model.CountryListParams
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChooseCountryCodeFragmentDialog : BottomSheetDialogFragment() {

    companion object {
        private const val PARAMS = "key_params"

        fun newInstance(
            params: CountryListParams? = null,
            callback: ((Country) -> Unit)
        ): ChooseCountryCodeFragmentDialog {
            return ChooseCountryCodeFragmentDialog().apply {
                params?.let {
                    val args = Bundle()
                    args.putParcelable(PARAMS, params)
                    arguments = args
                    onResult = callback
                }
            }
        }
    }

    private var onResult: ((Country) -> Unit)? = null
    private lateinit var viewModel: ChooseCountryCodeViewModel
    private val binding by viewBinding(FragmentChooseCountryBinding::bind)
    private lateinit var adapter: CountriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_choose_country, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ChooseCountryCodeViewModel::class.java]
        viewModel.setupParams(
            arguments?.getParcelable(PARAMS) ?: CountryListParams()
        )
        with(binding) {
            rvCountry.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = CountriesAdapter() {
                viewModel.onCountryClicked(it.countryCode)
            }
            rvCountry.adapter = adapter
            searchView.doOnQueryChanged { query, isDebounce ->
                viewModel.queryCountries(query, isDebounce)
            }
        }
        subscribe {
            viewModel.state.collect {
                handleState(it)
            }
        }
        subscribe {
            viewModel.result.collect {
                handleResult(it)
            }
        }
        viewModel.loadCountries()
    }

    private fun handleState(state: ChooseCountryView.Data) {
        adapter.submitList(state.items.map { it.mapToItem(state) })
        binding.searchView.isVisible = state.enableSearch
    }

    private fun handleResult(result: ChooseCountryView.Result) {
        result.country?.let {
            onResult?.invoke(it)
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        with(dialog) {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
            behavior.isDraggable = true
        }
        dialog.setOnShowListener {
            val parentLayout =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            if (parentLayout != null) {
                val layoutParams = parentLayout.layoutParams
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                parentLayout.layoutParams = layoutParams
            }
        }
        return dialog
    }

    private fun Country.mapToItem(params: ChooseCountryView.Data) = CountryItem(
        countryCode = countryCode,
        codeText = "+$codeLocal",
        flag = flag,
        isCodeVisible = params.displayCountryCode,
        countryName = "$flag $titleLocal",
        typeface = if (iso == params.country) {
            Typeface.BOLD
        } else {
            Typeface.NORMAL
        }
    )

    override fun getTheme(): Int = R.style.BottomSheetDialog
}

fun <T : ViewBinding> DialogFragment.viewBinding(viewBindingFactory: (View) -> T) =
    FragmentViewBindingDelegate(this, viewBindingFactory)
