package com.example.myfirstapponkotlin.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currency_rates")
    fun getAllRates(): Flow<List<CurrencyRate>>

    @Query("SELECT * FROM currency_rates WHERE currencyCode = :currencyCode")
    suspend fun getRate(currencyCode: String): CurrencyRate?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRate(rate: CurrencyRate)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<CurrencyRate>)

    @Query("SELECT * FROM conversion_history ORDER BY timestamp DESC")
    fun getConversionHistory(): Flow<List<ConversionHistory>>

    @Insert
    suspend fun insertConversion(conversion: ConversionHistory)

    @Query("DELETE FROM conversion_history WHERE timestamp < :timestamp")
    suspend fun deleteOldHistory(timestamp: Long)
} 