package com.example.myfirstapponkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.myfirstapponkotlin.presentation.MainActivity

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
    }

    fun clickBack(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}