package com.example.fragmenty

import android.annotation.SuppressLint
import android.content.Intent
import android.hardware.SensorManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import java.sql.DriverManager

class AnimatedActivity : AppCompatActivity() {
    private lateinit var fragment: AnimationFragment
    private var isLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animated)
        val fm = supportFragmentManager
        fragment = AnimationFragment()
        fm.beginTransaction().add(R.id.fragment_container,fragment).commit()
        val db = DBHelper()
        db.execute()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if(hasFocus) {
            fragment.startAnimation()

            Handler(Looper.getMainLooper()).postDelayed({
                if(isLoggedIn) {
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

    @SuppressLint("StaticFieldLeak")
    inner class DBHelper : AsyncTask<Void, Void, Void>() {
        var error = ""

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: Void?): Void? {
            val sharedScore = getSharedPreferences("com.example.fragmenty.shared",0)

            try{
                Class.forName("com.mysql.jdbc.Driver").newInstance()
                val url= "jdbc:mysql://10.0.2.2:3306/fragmenty"
                val connection = DriverManager.getConnection(url, "root","haslo")
                val statement = connection.createStatement()
                val resultSet = statement.executeQuery("select nick, is_logged_in from users;")
                while(resultSet.next()){
                    val nick = resultSet.getString(1)
                    val loggedIn = resultSet.getInt(2)
                    if(loggedIn == 1){
                        val edit = sharedScore.edit()
                        edit.putString("username", nick)
                        edit.apply()
                        isLoggedIn = true
                        break
                    }
                }
            }catch (e: Exception){
                error = e.toString()
                println("----------------DATABASE ERROR: $error--------------")
            }

            return null
        }
    }
}