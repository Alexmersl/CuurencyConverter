package com.example.myfirstapponkotlin.presentation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstapponkotlin.MainActivity2
import com.example.myfirstapponkotlin.R
import com.example.myfirstapponkotlin.presentation.history.HistoryActivity

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: MainViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        sharedPreferences = getSharedPreferences("CurrencyHistoryPrefs", Context.MODE_PRIVATE)
        
        setupObservers()
        setupClickListeners()
        viewModel.loadCurrencies()
    }
    
    private fun setupObservers() {
        viewModel.currencies.observe(this) { currencies ->
            setupCurrencyDropdowns(currencies)
        }
        
        viewModel.conversionResult.observe(this) { result ->
            findViewById<TextView>(R.id.textView2).text = result
        }
        
        viewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun setupCurrencyDropdowns(currencies: List<String>) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            currencies
        )
        
        val autoCompleteTextView1 = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView1)
        val autoCompleteTextView2 = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2)
        
        autoCompleteTextView1.setAdapter(adapter)
        autoCompleteTextView2.setAdapter(adapter)
        
        // Показываем список сразу при фокусе
        autoCompleteTextView1.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) autoCompleteTextView1.showDropDown()
        }
        autoCompleteTextView2.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) autoCompleteTextView2.showDropDown()
        }
    }
    
    private fun setupClickListeners() {
        findViewById<Button>(R.id.button4).setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putStringArrayListExtra("history", ArrayList(viewModel.getHistory()))
            startActivity(intent)
        }
        
        findViewById<Button>(R.id.button3).setOnLongClickListener {
            Toast.makeText(this, "Переход в offline", Toast.LENGTH_SHORT).show()
            true
        }
    }
    
    fun onClick(view: View) {
        val editText: EditText = findViewById(R.id.editTextNumber3)
        val inputCurrency: AutoCompleteTextView = findViewById(R.id.autoCompleteTextView1)
        val outputCurrency: AutoCompleteTextView = findViewById(R.id.autoCompleteTextView2)
        
        val amount = editText.text.toString()
        val from = inputCurrency.text.toString()
        val to = outputCurrency.text.toString()
        
        if (amount.isBlank() || from.isBlank() || to.isBlank()) {
            Toast.makeText(this, "Заполните поля", Toast.LENGTH_SHORT).show()
            return
        }
        
        viewModel.convertCurrency(from, to, amount)
    }
    
    fun toOfflineMode(view: View) {
        val offlineBtn = Intent(this, MainActivity2::class.java)
        startActivity(offlineBtn)
    }
    
    fun toHistoryScreen(view: View) {
        startActivity(Intent(this, HistoryActivity::class.java))
    }
} 