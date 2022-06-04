package com.example.fragmenty

import android.content.Intent
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat

class AnimatedActivity : AppCompatActivity() {
    private lateinit var fragment: AnimationFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animated)
        val fm = supportFragmentManager
        fragment = AnimationFragment()
        fm.beginTransaction().add(R.id.fragment_container,fragment).commit()
    }

    override fun onStart() {
        super.onStart()
        fragment.startAnimation()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 7500)
    }

}