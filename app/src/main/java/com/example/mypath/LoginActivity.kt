package com.example.mypath

import android.annotation.SuppressLint
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import java.sql.DriverManager

class LoginActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var nick: String
    private lateinit var password: String
    private var success = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        success = false
        val dbl = DBLogIn()
        val dbs = DBSignUp()

        val loginButton = findViewById<Button>(R.id.logIn)
        val signUpButton = findViewById<Button>(R.id.signUp)
        val nickField = findViewById<EditText>(R.id.nick)
        val passwordField = findViewById<EditText>(R.id.password)

        val sharedScore = getSharedPreferences("com.example.mypath.shared",0)

        loginButton.setOnClickListener {
            nick = nickField.text.toString()
            password = passwordField.text.toString()
            if(nick.isNotEmpty() && password.isNotEmpty()) {
                val wait = dbl.execute().get()
                if (success) {
                    val edit = sharedScore.edit()
                    edit.putString("username", nick)
                    edit.apply()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "Błędne dane logowania!", Toast.LENGTH_SHORT)
                        .show()
                }
            }else{
                Toast.makeText(applicationContext, "Wpisz prawidłowy nick i hasło!", Toast.LENGTH_SHORT).show()
            }
        }

        signUpButton.setOnClickListener {
            nick = nickField.text.toString()
            password = passwordField.text.toString()
            if(nick.isNotEmpty() && password.isNotEmpty()){
                val wait = dbs.execute().get()
                if(success){
                    val edit = sharedScore.edit()
                    edit.putString("username", nick)
                    edit.apply()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(applicationContext, "Użytkownik o takim nicku już istnieje!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(applicationContext, "Wpisz prawidłowy nick i hasło!", Toast.LENGTH_SHORT).show()
            }
        }

        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        if(sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null){
            val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
            sensorManager.registerListener(this,lightSensor,SensorManager.SENSOR_DELAY_NORMAL)
        }else Toast.makeText(this,"Light sensor not detected!",Toast.LENGTH_SHORT).show()
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor?.type == Sensor.TYPE_LIGHT) {
            if (event.values[0] < 40) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }

    override fun onStart() {
        super.onStart()
        success = false
        val nickField = findViewById<EditText>(R.id.nick)
        val passwordField = findViewById<EditText>(R.id.password)
        nickField.text.clear()
        passwordField.text.clear()
        val db = DBLogOut()
        val wait = db.execute().get()
    }

    override fun onStop() {
        super.onStop()
    }

    @SuppressLint("StaticFieldLeak")
    inner class DBLogIn : AsyncTask<Void, Void, Void>() {
        var error = ""

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: Void?): Void? {
            try{
                Class.forName("com.mysql.jdbc.Driver").newInstance()
                val url= "jdbc:mysql://10.0.2.2:3306/mypath"
                val connection = DriverManager.getConnection(url, "root","haslo")
                val statement = connection.createStatement()
                val resultSet = statement.executeQuery("select * from users where nick = '$nick' and password = '$password';")
                while(resultSet.next()){
                    success = true
                }
            }catch (e: Exception){
                error = e.toString()
                println("----------------DATABASE ERROR: $error--------------")
            }

            return null
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class DBSignUp : AsyncTask<Void, Void, Void>() {
        var error = ""

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: Void?): Void? {
            try{
                Class.forName("com.mysql.jdbc.Driver").newInstance()
                val url= "jdbc:mysql://10.0.2.2:3306/mypath"
                val connection = DriverManager.getConnection(url, "root","haslo")
                val statement = connection.createStatement()
                statement.executeUpdate("insert into users values('$nick', '$password');")
                success = true
            }catch (e: Exception){
                error = e.toString()
                println("----------------DATABASE ERROR: $error--------------")
            }

            return null
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class DBLogOut : AsyncTask<Void, Void, Void>() {
        var error = ""

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: Void?): Void? {
            val sharedScore = getSharedPreferences("com.example.mypath.shared",0)
            val username = sharedScore.getString("username", "")

            try{
                Class.forName("com.mysql.jdbc.Driver").newInstance()
                val url= "jdbc:mysql://10.0.2.2:3306/mypath"
                val connection = DriverManager.getConnection(url, "root","haslo")
                val statement = connection.createStatement()
                val resultSet = statement.executeQuery("select * from users where nick = '$username';")
                while(resultSet.next()){
                    val edit = sharedScore.edit()
                    edit.putString("username", "")
                    edit.apply()
                }
            }catch (e: Exception){
                error = e.toString()
                println("----------------DATABASE ERROR: $error--------------")
            }

            return null
        }
    }

}