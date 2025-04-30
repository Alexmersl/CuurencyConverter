package com.example.myfirstapponkotlin.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myfirstapponkotlin.api.CurrencyApiService
import com.example.myfirstapponkotlin.data.CurrencyDatabase
import com.example.myfirstapponkotlin.data.CurrencyRate
import com.example.myfirstapponkotlin.data.CurrencyRepository
import java.util.Date

class CurrencyUpdateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val repository = CurrencyRepository(
        CurrencyDatabase.getDatabase(context).currencyDao()
    )
    
    private val apiService = CurrencyApiService()

    override suspend fun doWork(): Result {
        return try {
            // Fetch latest rates from API
            val rates = apiService.getLatestRates()
            
            if (rates.isNotEmpty()) {
                // Convert to CurrencyRate objects
                val currencyRates = rates.map { (currencyCode, rate) ->
                    CurrencyRate(currencyCode, rate, Date())
                }
                
                // Store in database
                repository.insertRates(currencyRates)
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "currency_update_worker"
    }
} 