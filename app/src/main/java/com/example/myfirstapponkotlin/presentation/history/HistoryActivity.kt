package com.example.myfirstapponkotlin.presentation.history

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstapponkotlin.R

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        val currencyHistoryView: ListView = findViewById(R.id.currencyHistory)
        val history = intent.getStringArrayListExtra("history") ?: arrayListOf()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            history
        )

        currencyHistoryView.adapter = adapter
    }
} 