package com.country.emoji.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.country.emoji.data.CountriesProvider
import com.country.emoji.model.Country
import com.country.emoji.model.CountryListParams
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChooseCountryCodeViewModel(application: Application) : AndroidViewModel(application) {
    private val countriesProvider =
        CountriesProvider(getApplication<Application>().applicationContext)

    private val _state = MutableStateFlow(ChooseCountryView.Data())
    val state: StateFlow<ChooseCountryView.Data> get() = _state.asStateFlow()

    private val _result = MutableStateFlow(ChooseCountryView.Result())
    val result: StateFlow<ChooseCountryView.Result> get() = _result.asStateFlow()

    private var countries: List<Country>? = null

    fun loadCountries() =
        viewModelScope.launch {
            with(_state.value) {
                countriesProvider.fetchCountries(
                    country,
                    filter,
                    excludeMode
                ) {
                    countries = it
                    _state.value = _state.value.copy(items = it)
                }
            }
        }

    fun queryCountries(
        query: String,
        isDebounce: Boolean = true
    ) {
        viewModelScope.launch {
            if (isDebounce) {
                delay(DELAY_DEBOUNCE)
            }
            val q = query.lowercase()
            _state.value =
                _state.value.copy(
                    items = countries?.filter {
                        it.titleLocal.lowercase().contains(q) || it.countryCode.toString().contains(q)
                    }
                        .orEmpty()
                )
        }
    }

    fun onCountryClicked(code: Int) {
        countries?.find { it.countryCode == code }?.let {
            _result.value = _result.value.copy(country = it)
        }
    }

    fun setupParams(params: CountryListParams) {
        _state.value = _state.value.copy(
            country = params.country,
            enableSearch = params.enableSearch,
            displayCountryCode = params.displayCountryCode,
            excludeMode = params.excludeMode,
            filter = params.filter,
            title = params.title
        )
    }

    companion object {
        const val DELAY_DEBOUNCE = 300L
    }
}
