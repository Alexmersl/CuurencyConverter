package com.example.myfirstapponkotlin.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_of_currencies")
data class ListOfCurrencies(
    @PrimaryKey val currencyName: String,
    @ColumnInfo(name = "value") val value : Double)