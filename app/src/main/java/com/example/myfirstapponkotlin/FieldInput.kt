package com.example.myfirstapponkotlin

import android.widget.AutoCompleteTextView
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText

class FieldInput {

    companion object {
        fun inputAmount(editText: EditText): String? = editText.text.toString()
        fun inCurrencyToString(autoCompleteTextView: AutoCompleteTextView): String? = autoCompleteTextView.text.toString()
        fun outCurrencyToString(autoCompleteTextView: AutoCompleteTextView): String? = autoCompleteTextView.text.toString()
    }

}