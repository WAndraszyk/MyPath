package com.example.fragmenty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class AnimatedActivity : AppCompatActivity() {
    private lateinit var fragment: AnimationFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animated)
        val fm = supportFragmentManager
        fragment = AnimationFragment()
        fm.beginTransaction().add(R.id.fragment_container,fragment).commit()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if(hasFocus) {
            fragment.startAnimation()

            Handler(Looper.getMainLooper()).postDelayed({
                if(isLoggedIn()) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else{
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }, 7500)
        }
    }

    private fun isLoggedIn() : Boolean{
        val sharedScore = getSharedPreferences("com.example.fragmenty.shared",0)
        val user = sharedScore.getString("username","")
        return user != ""
    }
}