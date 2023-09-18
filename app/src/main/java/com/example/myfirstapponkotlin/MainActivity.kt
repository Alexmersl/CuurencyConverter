package com.example.myfirstapponkotlin

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstapponkotlin.HttpConnectionClass.getCurrencySet
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    companion object {
        var inAmount: String? = ""
        var inCurrency: String? = ""
        var outCurrency: String? = ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addCurrencyToDropDownList()

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
                    runOnUiThread {
                        val counter: TextView = findViewById(R.id.textView2)
                        counter.text = result
                        val toast =
                            Toast.makeText(this@MainActivity, "Расчет окончен", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }
            }
        }
}
