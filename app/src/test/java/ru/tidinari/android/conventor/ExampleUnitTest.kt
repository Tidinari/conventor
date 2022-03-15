package ru.tidinari.android.conventor

import kotlinx.coroutines.*
import org.junit.Test

import org.junit.Assert.*
import ru.tidinari.android.conventor.exchange.Currency
import ru.tidinari.android.conventor.exchange.Exchange
import ru.tidinari.android.conventor.exchange.ExchangeViewModel

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val viewModel = ExchangeViewModel()
        val resultMap = viewModel.getCurrency()
        assertNotEquals(mapOf<Currency, Exchange>(), resultMap)
        assertEquals(4, 2 + 2)
    }
}