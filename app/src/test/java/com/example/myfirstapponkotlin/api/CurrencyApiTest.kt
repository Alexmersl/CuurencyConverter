package com.example.myfirstapponkotlin.api

import kotlinx.coroutines.runBlocking
import org.junit.Test

class CurrencyApiTest {
    private val repository = CurrencyRepository()

    @Test
    fun testGetCurrencies() = runBlocking {
        val currencies = repository.getCurrencySet()
        println("Available currencies: $currencies")
        assert(currencies.isNotEmpty()) { "Currency list should not be empty" }
    }

    @Test
    fun testConvertCurrency() = runBlocking {
        val result = repository.getAmountResult("USD", "EUR", "100")
        println("Conversion result: $result")
        assert(result.isNotEmpty()) { "Conversion result should not be empty" }
    }
} 