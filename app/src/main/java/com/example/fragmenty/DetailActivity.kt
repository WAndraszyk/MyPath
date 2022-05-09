package com.example.fragmenty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.hide()

        val frag: RouteDetailFragment =
            supportFragmentManager.findFragmentById(R.id.detail_frag) as RouteDetailFragment
        frag.setRoute(1)
    }
}