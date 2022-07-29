package com.example.mypath

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources.Theme
import android.graphics.drawable.ColorDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar


class MainActivity : AppCompatActivity(), SensorEventListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        if(sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null){
            val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
            sensorManager.registerListener(this,lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }else Toast.makeText(this,"Light sensor not detected!", Toast.LENGTH_SHORT).show()

        if((resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) < Configuration.SCREENLAYOUT_SIZE_LARGE) {
            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)
            val typedValue = TypedValue()
            val theme: Theme = this.theme
            theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true)
            @ColorInt val color = typedValue.data
            supportActionBar?.setBackgroundDrawable(ColorDrawable(color))

            val logOut = findViewById<ImageView>(R.id.logOutIcon)
            logOut.setOnClickListener{
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent)
                this.finish()
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor?.type == Sensor.TYPE_LIGHT) {
            if (event.values[0] < 40) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }
}