package com.example.myfirstapponkotlin

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity


class MainActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        val currencyHistoryView: ListView = findViewById(R.id.currencyHistory)

        val history = intent.getStringArrayListExtra("history") ?: arrayListOf()

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1, history
        )

        currencyHistoryView.adapter = adapter
    }



}