package ru.tidinari.android.conventor.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*
import java.net.URL

class ExchangeViewModel : ViewModel() {

    private var _cachedMap: Map<Currency, Exchange>? = null
        get() = if (field == null) getCurrencyExchange() else field

    val cachedMap: Map<Currency, Exchange>
        get() = _cachedMap!!

    var selectedCurrency: Currency? = null

    private fun getCurrencyExchange(): Map<Currency, Exchange> {
        val result = mutableMapOf<Currency, Exchange>()
        viewModelScope.launch(Dispatchers.IO) {
            val url = URL("https://www.cbr-xml-daily.ru/daily_json.js")
            val json = Json.parseToJsonElement(url.readText())
            json.jsonObject["Valute"]!!.jsonObject.forEach {
                val currencyInformation = it.value.jsonObject
                val charCode = it.key
                val name = currencyInformation["Name"]!!.jsonPrimitive.content
                val currency = Currency(charCode, name)

                val nominal = currencyInformation["Nominal"]!!.jsonPrimitive.int
                val value = currencyInformation["Value"]!!.jsonPrimitive.float
                val exchange = Exchange(nominal, value)

                result[currency] = exchange
                println(result)
            }
            _cachedMap = result
        }
        return result
    }

    fun convert(rubles: Int, currency: Currency): Float {
        val exchange = cachedMap[currency]
        return if (exchange == null) {
            0f
        } else {
            rubles / exchange.value * exchange.nominal
        }
    }

    fun convert(rubles: Int): Float {
        return convert(rubles, selectedCurrency!!)
    }

    fun getCurrency() = ArrayList(cachedMap.keys)
}

/**
 * Present currency
 */
data class Currency(val charCode: String, val name: String)

/**
 * Present exchange for rubles
 */
data class Exchange(val nominal: Int, val value: Float)