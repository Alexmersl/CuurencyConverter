package com.example.myfirstapponkotlin.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirstapponkotlin.data.ConversionHistory
import com.example.myfirstapponkotlin.data.CurrencyDatabase
import com.example.myfirstapponkotlin.data.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class CurrencyViewModel : ViewModel() {
    private lateinit var repository: CurrencyRepository
    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()

    fun initialize(context: android.content.Context) {
        repository = CurrencyRepository(CurrencyDatabase.getDatabase(context).currencyDao())
        loadRates()
        loadHistory()
    }

    private fun loadRates() {
        viewModelScope.launch {
            repository.allRates.collect { rates ->
                _uiState.value = _uiState.value.copy(rates = rates)
            }
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            repository.conversionHistory.collect { history ->
                _uiState.value = _uiState.value.copy(history = history)
            }
        }
    }

    fun loadHistoricalRates(fromCurrency: String, toCurrency: String) {
        viewModelScope.launch {
            try {
                val historicalRates = repository.getHistoricalRates(fromCurrency, toCurrency)
                _uiState.value = _uiState.value.copy(
                    historicalRates = historicalRates,
                    error = null
                )
            } catch (e: Exception) {
                setError("Error loading historical rates: ${e.message}")
            }
        }
    }

    fun convert(fromCurrency: String, toCurrency: String, amount: Double) {
        viewModelScope.launch {
            try {
                val result = repository.convert(fromCurrency, toCurrency, amount)
                
                if (result == null) {
                    setError("Failed to convert currencies")
                    return@launch
                }
                
                val conversion = ConversionHistory(
                    fromCurrency = fromCurrency,
                    toCurrency = toCurrency,
                    amount = amount,
                    result = result,
                    timestamp = Date()
                )
                
                repository.insertConversion(conversion)
                
                _uiState.value = _uiState.value.copy(
                    conversionResult = result,
                    lastConversion = conversion,
                    error = null
                )

                // Load historical rates after successful conversion
                loadHistoricalRates(fromCurrency, toCurrency)
            } catch (e: Exception) {
                setError("Error during conversion: ${e.message}")
            }
        }
    }

    fun setError(message: String) {
        _uiState.value = _uiState.value.copy(
            error = message,
            conversionResult = null
        )
    }

    fun cleanupHistory() {
        viewModelScope.launch {
            repository.cleanupOldHistory()
        }
    }
}

data class CurrencyUiState(
    val rates: List<com.example.myfirstapponkotlin.data.CurrencyRate> = emptyList(),
    val history: List<ConversionHistory> = emptyList(),
    val conversionResult: Double? = null,
    val lastConversion: ConversionHistory? = null,
    val historicalRates: List<Pair<Date, Double>> = emptyList(),
    val error: String? = null
) 