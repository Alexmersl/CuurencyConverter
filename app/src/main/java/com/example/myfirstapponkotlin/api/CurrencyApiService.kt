package com.example.myfirstapponkotlin.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import com.google.gson.Gson
import com.google.gson.JsonObject

class CurrencyApiService {
    private val gson = Gson()
    private val baseUrl = "https://api.exchangerate-api.com/v4/latest/"

    suspend fun getLatestRates(baseCurrency: String = "USD"): Map<String, Double> = withContext(Dispatchers.IO) {
        try {
            val url = URL("$baseUrl$baseCurrency")
            val json = url.readText()
            val jsonObject = gson.fromJson(json, JsonObject::class.java)
            val ratesObject = jsonObject.getAsJsonObject("rates")
            
            ratesObject.entrySet().associate { entry ->
                entry.key to entry.value.asDouble
            }
        } catch (e: Exception) {
            emptyMap()
        }
    }
} 