package com.example.myfirstapponkotlin.data

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET("latest")
    suspend fun getLatestRates(
        @Query("base") baseCurrency: String = "USD"
    ): ExchangeRatesResponse

    @GET("convert")
    suspend fun convert(
        @Query("from") fromCurrency: String,
        @Query("to") toCurrency: String,
        @Query("amount") amount: Double
    ): ConversionResponse

    @GET("timeseries")
    suspend fun getHistoricalRates(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("base") baseCurrency: String,
        @Query("symbols") targetCurrency: String
    ): HistoricalRatesResponse
}

data class ExchangeRatesResponse(
    val base: String,
    val rates: Map<String, Double>,
    val date: String
)

data class ConversionResponse(
    val success: Boolean,
    val query: ConversionQuery,
    val info: ConversionInfo,
    val result: Double
)

data class ConversionQuery(
    val from: String,
    val to: String,
    val amount: Double
)

data class ConversionInfo(
    val rate: Double
)

data class HistoricalRatesResponse(
    val success: Boolean,
    val timeseries: Boolean,
    val start_date: String,
    val end_date: String,
    val base: String,
    val rates: Map<String, Map<String, Double>>
) 