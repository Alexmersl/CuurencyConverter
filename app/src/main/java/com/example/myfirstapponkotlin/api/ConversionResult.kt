package com.example.myfirstapponkotlin.api

import com.google.gson.annotations.SerializedName

data class ConversionResult(
    @SerializedName("conversion_result")
    val conversionResult: Double
) 