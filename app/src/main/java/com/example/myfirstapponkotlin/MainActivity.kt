package com.example.myfirstapponkotlin

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
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private val history = mutableListOf<String>()
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        var inAmount: String? = ""
        var inCurrency: String? = ""
        var outCurrency: String? = ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("CurrencyHistoryPrefs", Context.MODE_PRIVATE)

        addCurrencyToDropDownList()
        setUpLongClickListener()

        findViewById<Button>(R.id.button4).setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            intent.putStringArrayListExtra("history", ArrayList(history))
            startActivity(intent)

        }
    }

        private fun addCurrencyToDropDownList() {
            GlobalScope.launch(Dispatchers.IO) {
                val itemsList = ArrayList(HttpConnectionClass.getCurrencySet())
                runOnUiThread {
                    val adapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        itemsList
                    )
                    val autoCompleteTextView1 =
                        findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView1)
                    val autoCompleteTextView2 =
                        findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2)
                    autoCompleteTextView1.setAdapter(adapter)
                    autoCompleteTextView2.setAdapter(adapter)
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
                val toastErr = Toast.makeText(this, "Заполните поля", Toast.LENGTH_SHORT)
                toastErr.show()
            } else {
                HttpConnectionClass.updateUrl(
                    FieldInput.getInCurrency(),
                    FieldInput.getOutCurrency(),
                    FieldInput.getAmount()
                )
                GlobalScope.launch(Dispatchers.IO) {
                    val result = HttpConnectionClass.getAmountResult()
                    var stringHistory = inAmount
                    withContext(Dispatchers.Main) {
                        val counter: TextView = findViewById(R.id.textView2)
                        counter.text = result

                        history.add("$stringHistory --> $result")

                        Toast.makeText(this@MainActivity, "Расчет окончен", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

    fun setUpLongClickListener(){
        findViewById<Button>(R.id.button3).setOnLongClickListener {
            Toast.makeText(this, "Переход в offline", Toast.LENGTH_SHORT).show()
            true
        }
    }

    fun toOfflineMode(view: View){
        val offlineBtn = Intent(this, MainActivity2::class.java)
        startActivity(offlineBtn)
    }

    fun toHistoryScreen(view : View){
        startActivity(Intent(this, MainActivity3::class.java))
    }

}
