package com.example.fragmenty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val id: Int = intent.getIntExtra("routeId", 0)
        setRoute(id)
    }

    private fun setRoute(id: Int){
        val sharedScore = this.getSharedPreferences("com.example.fragmenty.shared",0)
        val edit = sharedScore?.edit()
        edit?.putInt("id", id)
        edit?.apply()
    }
}