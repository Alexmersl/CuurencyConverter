package com.example.myfirstapponkotlin.api

import android.util.Log
import com.example.myfirstapponkotlin.api.RetrofitClient.currencyApi

class CurrencyRepository(private val api: CurrencyApi = currencyApi) {
    suspend fun getCurrencySet(): Set<String> {
        return try {
            Log.d("CurrencyConverter", "Fetching currencies...")
            val response = api.getCurrencies()
            Log.d("CurrencyConverter", "API Response: $response")
            
            val rates = response.conversionRates
            Log.d("CurrencyConverter", "Conversion rates: $rates")
            
            val currencies = rates.keys
            Log.d("CurrencyConverter", "Currencies set: $currencies")
            
            currencies
        } catch (e: Exception) {
            Log.e("CurrencyConverter", "Error fetching currencies", e)
            emptySet()
        }
    }

    suspend fun getAmountResult(from: String, to: String, amount: String): String {
        return try {
            Log.d("CurrencyConverter", "Converting: from=$from, to=$to, amount=$amount")
            val response = api.convertCurrency(from, to, amount)
            Log.d("CurrencyConverter", "Conversion response: $response")
            
            val result = response.conversionResult
            Log.d("CurrencyConverter", "Conversion result: $result")
            
            result.toString()
        } catch (e: Exception) {
            Log.e("CurrencyConverter", "Error during conversion", e)
            "0"
        }
    }
} 