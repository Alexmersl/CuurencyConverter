package com.example.myfirstapponkotlin

import android.widget.AutoCompleteTextView
import android.widget.EditText
import com.example.myfirstapponkotlin.MainActivity.Companion.inAmount
import com.example.myfirstapponkotlin.MainActivity.Companion.inCurrency
import com.example.myfirstapponkotlin.MainActivity.Companion.outCurrency
import com.google.android.material.textfield.TextInputEditText

class FieldInput {

    companion object {
        fun inputAmount (editText: EditText) : String? = editText.text.toString()
        fun inCurrencyToString(autoCompleteTextView : AutoCompleteTextView) : String? = autoCompleteTextView.text.toString()
        fun outCurrencyToString(autoCompleteTextView : AutoCompleteTextView) : String? = autoCompleteTextView.text.toString()
        fun getAmount(): String? = inAmount
        fun getInCurrency() : String? = inCurrency
        fun getOutCurrency() : String? = outCurrency
    }

}