package com.example.fragmenty

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            onShowDetail()
        }
    }

    private fun onShowDetail() {
        val intent = Intent(this, DetailActivity::class.java)
        startActivity(intent)
    }
}