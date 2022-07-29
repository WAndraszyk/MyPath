package com.example.mypath

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate


class DetailActivity : AppCompatActivity(), SensorEventListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val id: Int = intent.getIntExtra("routeId", 0)
        setRoute(id)

        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        if(sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null){
            val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
            sensorManager.registerListener(this,lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }else Toast.makeText(this,"Light sensor not detected!", Toast.LENGTH_SHORT).show()
    }

    private fun setRoute(id: Int){
        val sharedScore = this.getSharedPreferences("com.example.mypath.shared",0)
        val edit = sharedScore?.edit()
        edit?.putInt("id", id)
        edit?.apply()
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