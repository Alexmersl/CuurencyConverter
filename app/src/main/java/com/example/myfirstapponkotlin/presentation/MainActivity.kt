package com.example.myfirstapponkotlin.presentation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstapponkotlin.MainActivity2
import com.example.myfirstapponkotlin.R
import com.example.myfirstapponkotlin.api.CurrencyRepository
import com.example.myfirstapponkotlin.api.RetrofitClient
import com.example.myfirstapponkotlin.database.AppDatabase
import com.example.myfirstapponkotlin.database.DataBaseInit
import com.example.myfirstapponkotlin.presentation.history.HistoryActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var databaseInit: DataBaseInit
    private val TAG = "MainActivity"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        sharedPreferences = getSharedPreferences("CurrencyPrefs", Context.MODE_PRIVATE)
        val database = AppDatabase.getDatabase(this)
        databaseInit = DataBaseInit(RetrofitClient.currencyApi, database)
        
        val repository = CurrencyRepository(RetrofitClient.currencyApi)
        viewModel = MainViewModel(repository)
        
        setupObservers()
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                databaseInit.saveCurrenciesToDb()
                val currenciesFromDb = databaseInit.getCurrenciesFromDb()
                
                withContext(Dispatchers.Main) {
                    val currencyList = currenciesFromDb.map { it.currencyName }
                    setupCurrencyDropdowns(currencyList)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Database error", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error updating currency data", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        setupClickListeners()
    }
    
    private fun setupObservers() {
        viewModel.currencies.observe(this) { currencies ->
            setupCurrencyDropdowns(currencies)
        }
        
        viewModel.conversionResult.observe(this) { result ->
            if (result != null) {
                findViewById<TextView>(R.id.textView2).text = result
                val history = sharedPreferences.getString("conversion_history", "") ?: ""
                val newHistory = "$history\n${result}"
                sharedPreferences.edit().putString("conversion_history", newHistory).apply()
                Log.d(TAG, "Result updated: $result")
            }
        }
        
        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun setupCurrencyDropdowns(currencies: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, currencies)
        
        findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView1).apply {
            setAdapter(adapter)
            setText(currencies.firstOrNull() ?: "", false)
        }
        
        findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2).apply {
            setAdapter(adapter)
            setText(currencies.getOrNull(1) ?: "", false)
        }
    }
    
    private fun setupClickListeners() {
        findViewById<Button>(R.id.button4).setOnClickListener {
            val history = sharedPreferences.getString("conversion_history", "") ?: ""
            val historyList = history.split("\n").filter { it.isNotEmpty() }
            
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putStringArrayListExtra("history", ArrayList(historyList))
            startActivity(intent)
        }
        
        findViewById<Button>(R.id.button3).setOnLongClickListener {
            Toast.makeText(this, "Переход в offline", Toast.LENGTH_SHORT).show()
            true
        }
    }
    
    fun onClick(view: View) {
        val amountFrom = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView1).text.toString()
        val amountTo = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2).text.toString()
        val amount = findViewById<EditText>(R.id.editTextNumber3).text.toString()
        
        Log.d(TAG, "Convert button clicked - From: $amountFrom, To: $amountTo, Amount: $amount")
        
        if (amountFrom.isNotEmpty() && amountTo.isNotEmpty() && amount.isNotEmpty()) {
            viewModel.convertCurrency(amountFrom, amountTo, amount)
            Log.d(TAG, "Conversion started")
        } else {
            Log.w(TAG, "Conversion attempted with empty fields")
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }
    
    fun toOfflineMode(view: View) {
        val offlineBtn = Intent(this, MainActivity2::class.java)
        startActivity(offlineBtn)
    }
    
    fun toHistoryScreen(view: View) {
        startActivity(Intent(this, HistoryActivity::class.java))
    }
} 