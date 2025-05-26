package com.example.myfirstapponkotlin

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
import com.example.myfirstapponkotlin.api.CurrencyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val history = mutableListOf<String>()
    private lateinit var sharedPreferences: SharedPreferences
    private val currencyRepository = CurrencyRepository()

    companion object {
        var inAmount: String? = ""
        var inCurrency: String? = ""
        var outCurrency: String? = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("CurrencyHistoryPrefs", Context.MODE_PRIVATE)


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val currencies = currencyRepository.getCurrencySet()
                withContext(Dispatchers.Main) {
                    android.util.Log.d("API_TEST", "Currencies: $currencies")
                    Toast.makeText(this@MainActivity, "Currencies: $currencies", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    android.util.Log.e("API_TEST", "Error: "+e.message)
                    Toast.makeText(this@MainActivity, "API error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        addCurrencyToDropDownList()
        setUpLongClickListener()

        findViewById<Button>(R.id.button4).setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            intent.putStringArrayListExtra("history", ArrayList(history))
            startActivity(intent)
        }
    }

    private fun addCurrencyToDropDownList() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val itemsList = ArrayList(currencyRepository.getCurrencySet())
                Log.d("CurrencyConverter", "Loaded currencies: $itemsList")
                withContext(Dispatchers.Main) {
                    if (itemsList.isEmpty()) {
                        Toast.makeText(this@MainActivity, "Список валют пуст", Toast.LENGTH_SHORT).show()
                        return@withContext
                    }
                    val adapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        itemsList
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
            } catch (e: Exception) {
                Log.e("CurrencyConverter", "Error loading currencies", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Ошибка загрузки валют: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun onClick(view: View) {
        val editText: EditText = findViewById(R.id.editTextNumber3)
        val inputCurrency: AutoCompleteTextView = findViewById(R.id.autoCompleteTextView1)
        val outputCurrency: AutoCompleteTextView = findViewById(R.id.autoCompleteTextView2)

        inAmount = FieldInput.inputAmount(editText)
        inCurrency = FieldInput.inCurrencyToString(inputCurrency)
        outCurrency = FieldInput.outCurrencyToString(outputCurrency)

        if (inAmount.isNullOrEmpty() || inCurrency.isNullOrEmpty() || outCurrency.isNullOrEmpty()) {
            Toast.makeText(this, "Заполните поля", Toast.LENGTH_SHORT).show()
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val result = currencyRepository.getAmountResult(
                        inCurrency!!,
                        outCurrency!!,
                        inAmount!!
                    )
                    withContext(Dispatchers.Main) {
                        val counter: TextView = findViewById(R.id.textView2)
                        counter.text = result
                        history.add("$inAmount --> $result")
                        Toast.makeText(this@MainActivity, "Расчет окончен", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Ошибка конвертации", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun setUpLongClickListener() {
        findViewById<Button>(R.id.button3).setOnLongClickListener {
            Toast.makeText(this, "Переход в offline", Toast.LENGTH_SHORT).show()
            true
        }
    }

    fun toOfflineMode(view: View) {
        val offlineBtn = Intent(this, MainActivity2::class.java)
        startActivity(offlineBtn)
    }

    fun toHistoryScreen(view : View){
        startActivity(Intent(this, MainActivity3::class.java))
    }
}
