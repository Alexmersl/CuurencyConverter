package com.example.myfirstapponkotlin.data

import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Calendar

class CurrencyRepository(private val currencyDao: CurrencyDao) {
    private val api: CurrencyApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.exchangerate.host/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApi::class.java)
    }

    val allRates: Flow<List<CurrencyRate>> = currencyDao.getAllRates()
    val conversionHistory: Flow<List<ConversionHistory>> = currencyDao.getConversionHistory()

    suspend fun getRate(currencyCode: String): CurrencyRate? {
        try {
            // First try to get from local database
            var rate = currencyDao.getRate(currencyCode)
            
            // If not found or too old, fetch from API
            if (rate == null || isRateTooOld(rate.lastUpdated)) {
                val response = api.getLatestRates()
                val newRates = response.rates.map { (code, rate) ->
                    CurrencyRate(
                        currencyCode = code,
                        rate = rate,
                        lastUpdated = Date()
                    )
                }
                
                // Update local database
                currencyDao.insertRates(newRates)
                
                // Get the requested rate
                rate = currencyDao.getRate(currencyCode)
            }
            
            return rate
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    suspend fun convert(fromCurrency: String, toCurrency: String, amount: Double): Double? {
        try {
            val response = api.convert(fromCurrency, toCurrency, amount)
            if (response.success) {
                return response.result
            }
            return null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    suspend fun insertConversion(conversion: ConversionHistory) {
        currencyDao.insertConversion(conversion)
    }

    suspend fun insertRates(rates: List<CurrencyRate>) {
        currencyDao.insertRates(rates)
    }

    suspend fun cleanupOldHistory() {
        val oneMonthAgo = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000)
        currencyDao.deleteOldHistory(oneMonthAgo)
    }

    suspend fun getHistoricalRates(fromCurrency: String, toCurrency: String): List<Pair<Date, Double>> {
        try {
            val calendar = Calendar.getInstance()
            val endDate = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
            calendar.add(Calendar.MONTH, -1)
            val startDate = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)

            val response = api.getHistoricalRates(startDate, endDate, fromCurrency, toCurrency)
            if (response.success) {
                return response.rates.mapNotNull { (date, rates) ->
                    val rate = rates[toCurrency] ?: return@mapNotNull null
                    Pair(SimpleDateFormat("yyyy-MM-dd").parse(date), rate)
                }.sortedBy { it.first }
            }
            return emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }

    private fun isRateTooOld(date: Date): Boolean {
        val oneHourAgo = System.currentTimeMillis() - (60L * 60 * 1000)
        return date.time < oneHourAgo
    }
} 