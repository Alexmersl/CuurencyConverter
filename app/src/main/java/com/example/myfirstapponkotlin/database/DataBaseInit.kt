package com.example.myfirstapponkotlin.database

import android.util.Log
import com.example.myfirstapponkotlin.api.CurrencyApi

class DataBaseInit(
    private val api: CurrencyApi,
    private val db: AppDatabase
) {
    private val TAG = "DataBaseInit"

    suspend fun getCurrencyMap(): Map<String, Double> {
        return try {
            val response = api.getCurrencies()
            response.conversionRates
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching currencies from API", e)
            emptyMap()
        }
    }

    suspend fun saveCurrenciesToDb() {
        try {
            val map = getCurrencyMap()
            val list = map.map { (code, value) ->
                ListOfCurrencies(currencyName = code, value = value)
            }
            
            db.getStaticDao().insertNewStatisticData(*list.toTypedArray())
            Log.d(TAG, "Saved to DB: ${list.size} currencies")

            val savedData = db.getStaticDao().getAllStatisticData()
            Log.d(TAG, "DB content: ${savedData.map { "${it.currencyName}=${it.value}" }.joinToString(", ")}")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving to database", e)
        }
    }

    fun getCurrenciesFromDb(): List<StatisticInfoTuple> {
        return try {
            val currencies = db.getStaticDao().getAllStatisticData()
            Log.d(TAG, "Read from DB: ${currencies.map { "${it.currencyName}=${it.value}" }.joinToString(", ")}")
            currencies
        } catch (e: Exception) {
            Log.e(TAG, "Error reading from database", e)
            emptyList()
        }
    }
}