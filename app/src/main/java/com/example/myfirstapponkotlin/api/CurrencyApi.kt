package com.example.myfirstapponkotlin.api

import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApi {
    @GET("latest/USD")
    suspend fun getCurrencies(): CurrencyData

    @GET("pair/{from}/{to}/{amount}")
    suspend fun convertCurrency(
        @Path("from") from: String,
        @Path("to") to: String,
        @Path("amount") amount: String
    ): ConversionResult
} 