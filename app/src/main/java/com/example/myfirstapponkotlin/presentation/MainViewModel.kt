package com.example.myfirstapponkotlin.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirstapponkotlin.api.CurrencyRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = CurrencyRepository()
    
    private val _currencies = MutableLiveData<List<String>>()
    val currencies: LiveData<List<String>> = _currencies
    
    private val _conversionResult = MutableLiveData<String>()
    val conversionResult: LiveData<String> = _conversionResult
    
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    
    private val _history = MutableLiveData<List<String>>(emptyList())
    val history: LiveData<List<String>> = _history
    
    fun loadCurrencies() {
        viewModelScope.launch {
            try {
                Log.d("CurrencyConverter", "Loading currencies...")
                val currencySet = repository.getCurrencySet()
                _currencies.value = currencySet.toList()
                Log.d("CurrencyConverter", "Currencies loaded: ${currencySet.size}")
            } catch (e: Exception) {
                Log.e("CurrencyConverter", "Error loading currencies", e)
                _error.value = "Ошибка загрузки валют: ${e.message}"
            }
        }
    }
    
    fun convertCurrency(from: String, to: String, amount: String) {
        viewModelScope.launch {
            try {
                Log.d("CurrencyConverter", "Converting: from=$from, to=$to, amount=$amount")
                val result = repository.getAmountResult(from, to, amount)
                _conversionResult.value = result
                addToHistory("$amount $from → $result $to")
            } catch (e: Exception) {
                Log.e("CurrencyConverter", "Error during conversion", e)
                _error.value = "Ошибка конвертации: ${e.message}"
            }
        }
    }
    
    private fun addToHistory(conversion: String) {
        val currentHistory = _history.value?.toMutableList() ?: mutableListOf()
        currentHistory.add(conversion)
        _history.value = currentHistory
    }
    
    fun getHistory(): List<String> = _history.value ?: emptyList()
} 