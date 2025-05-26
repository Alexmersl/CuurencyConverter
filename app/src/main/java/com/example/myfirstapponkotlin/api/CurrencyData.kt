package com.example.myfirstapponkotlin.api

import com.google.gson.annotations.SerializedName

data class CurrencyData(
    @SerializedName("conversion_rates")
    val conversionRates: Map<String, Double>
)

