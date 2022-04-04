package com.country.emoji.data

import android.content.Context
import com.country.emoji.R
import com.country.emoji.extensions.moveToFirstIf
import com.country.emoji.extensions.toEmojiFrag
import com.country.emoji.model.Country
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringWriter
import java.io.Writer
import java.lang.reflect.Type
import java.text.NumberFormat
import java.util.Locale
import java.util.stream.Collectors

class CountriesProvider(private val context: Context) : CoroutineScope {

    /**
     * @param iso Country ios code
     * @return Located country name
     */
    private fun getLocalName(iso: String) = Locale(Locale.getDefault().country, iso).displayCountry

    /**
     * @param code Country ios code
     * @return Located phone code (actual for arabic)
     */
    private fun getLocalNumber(code: Int) =
        NumberFormat.getInstance(Locale.getDefault()).format(code)

    /**
     * Parsing JSON file from raw which is got via Resources
     * Init country name by current device locale
     * Find emoji symbol by iso
     * Filter countries via list of iso
     * @param isExcludeMode enable/disable support or exclude filter mode
     * @return list of Country
     */
    suspend fun fetchCountries(
        currentCountry: String?,
        filter: List<String>,
        isExcludeMode: Boolean,
        callback: ((List<Country>) -> Unit)
    ) {
        coroutineScope {
            launch {
                val output = loadDataFromFile().stream()
                    .filter { e -> if (isExcludeMode) !filter.contains(e.iso) else filter.contains(e.iso) }
                    .collect(Collectors.toList())
                    .map {
                        it.copy(
                            flag = it.iso.toEmojiFrag(),
                            titleLocal = getLocalName(it.iso),
                            codeLocal = getLocalNumber(it.countryCode)
                        )
                    }
                // sort list by localized name
                output.sortedBy { it.titleLocal.lowercase(Locale.getDefault()) }
                // move current country to first position
                callback(output.moveToFirstIf { item -> item.iso == currentCountry })
            }
        }
    }

    /**
     * Parsing JSON file from raw which is got via Resources
     * Init country name by current device locale
     * Find emoji symbol by iso
     * @return list of Country
     */
    suspend fun fetchCountries(callback: ((List<Country>) -> Unit)) =
        coroutineScope {
            launch {
                val countries = loadDataFromFile().map {
                    it.copy(
                        flag = it.iso.toEmojiFrag(),
                        titleLocal = getLocalName(it.iso)
                    )
                }.sortedBy { it.titleLocal.lowercase(Locale.getDefault()) }
                callback(countries)
            }
        }

    /**
     * Parsing JSON file from raw which is got via Resources
     * Init country name by current device locale
     * */
    private fun loadDataFromFile(): List<Country> {
        val countriesData = context.resources.openRawResource(R.raw.countries)
        val writer: Writer = StringWriter()
        val buffer = CharArray(CHAR_SIZE)
        countriesData.use { stream ->
            val reader: Reader = BufferedReader(InputStreamReader(stream, CHARSE_NAME))
            var n: Int
            while (reader.read(buffer).also { n = it } != -1) {
                writer.write(buffer, 0, n)
            }
        }
        val countriesJsonArray = JSONArray(writer.toString())
        val listType: Type = object : TypeToken<List<Country>>() {}.type
        return Gson().fromJson(countriesJsonArray.toString(), listType)
    }

    /**
     * @param code Country ISO code
     * @return Country model
     */
    fun findCountryByCode(code: String): Country? {
        val countries = loadDataFromFile()
        val country = countries.find { country -> country.iso == code }
        return country?.copy(
            flag = country.iso.toEmojiFrag(),
            codeLocal = getLocalNumber(country.countryCode),
            titleLocal = getLocalName(country.iso)
        )
    }

    companion object {
        const val CHAR_SIZE = 1024
        const val CHARSE_NAME = "UTF-8"
        const val NAME_SCOPE = "PROVIDER"
    }

    override val coroutineContext = Dispatchers.IO + SupervisorJob() + CoroutineName(NAME_SCOPE)
}
