package com.example.fragmenty

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.util.*

class Tab1Fragment : Fragment(R.layout.fragment_tab1) {

    private lateinit var loading: ProgressBar
    private lateinit var home: RelativeLayout
    private lateinit var cityNameTV: TextView
    private lateinit var temperatureTV: TextView
    private lateinit var conditionTV: TextView
    private lateinit var cityEdt: TextInputEditText
    private lateinit var iconIV: ImageView
    private lateinit var search: ImageView
    private lateinit var locationManager: LocationManager
    private val PERMISSION_CODE = 1
    private lateinit var cityName: String
    private lateinit var icon: String
    private var notFound = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading = view.findViewById(R.id.Loading)
        cityNameTV = view.findViewById(R.id.CityName)
        temperatureTV = view.findViewById(R.id.Temperature)
        conditionTV = view.findViewById(R.id.Condition)
        cityEdt = view.findViewById(R.id.EdtCity)
        iconIV = view.findViewById(R.id.Icon)
        search = view.findViewById(R.id.IVSearch)
        home = view.findViewById(R.id.Home)

        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),PERMISSION_CODE)
        }

        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location != null) {
            cityName = getCityName(location.latitude, location.longitude)
            cityName = cityName.normalize()
            if(cityName != "Not found"){
                getWeatherInfo(cityName)
            }
        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1F)
            { p0 ->
                cityName = getCityName(p0.latitude, p0.longitude)
                cityName = cityName.normalize()
                if(cityName != "Not found"){
                    getWeatherInfo(cityName)
                }
            }
        }

        search.setOnClickListener{
            val city = cityEdt.text.toString()
            if(city.isEmpty()){
                Toast.makeText(activity,"Please enter city!",Toast.LENGTH_SHORT).show()
            }else{
                getWeatherInfo(city)
            }
        }


    }

    fun String.normalize(): String {
        val original = arrayOf("Ą", "ą", "Ć", "ć", "Ę", "ę", "Ł", "ł", "Ń", "ń", "Ó", "ó", "Ś", "ś", "Ź", "ź", "Ż", "ż")
        val normalized = arrayOf("A", "a", "C", "c", "E", "e", "L", "l", "N", "n", "O", "o", "S", "s", "Z", "z", "Z", "z")

        return this.map { char ->
            val index = original.indexOf(char.toString())
            if (index >= 0) normalized[index] else char
        }.joinToString("")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(activity,"Permission granted...",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(activity,"Please provide the permissions!",Toast.LENGTH_SHORT).show()
                activity?.finish()
            }
        }
    }

    private fun getCityName(longitude: Double, latitude: Double) : String{
        var cityName = "Not found"
        val gcd = Geocoder(requireContext(), Locale.getDefault())
        try{
            val addresses = gcd.getFromLocation(longitude, latitude, 10)
            for(adr in addresses){
                if(adr!=null){
                    val city = adr.locality
                    if(city!=null && !city.equals("")){
                        cityName = city
                    }
                }
            }
        }catch (e: IOException){
            e.printStackTrace()
        }
        if(cityName == "Not found"){
            Log.d("TAG","CITY NOT FOUND")
            Toast.makeText(activity,"User's city not found!",Toast.LENGTH_SHORT).show()
        }
        return cityName
    }

    private fun getWeatherInfo(cityName: String) {
        val url = URL("https://api.weatherapi.com/v1/current.json?key=9093389d3dfd43c4b8e121340221007&q=$cityName&aqi=no")
        val thread = Thread(){
            run{
                val mapper = ObjectMapper()

                val json = getJson(url)

                if (!notFound) {
                    cityNameTV.text = cityName

                    val current = mapper.readTree(json).get("current")

                    val temperature = current.get("temp_c").asText()
                    temperatureTV.text = "$temperature°C"

                    val condition = current.get("condition")

                    val conditionText = condition.get("text").asText()
                    conditionTV.text = conditionText
                    icon = condition.get("icon").asText()
                }
            }
            activity?.runOnUiThread(){
                if (!notFound) {
                    Picasso.get().load("https:$icon").into(iconIV)
                    loading.visibility = View.GONE
                    home.visibility = View.VISIBLE
                }else{
                    Toast.makeText(activity,"No matching location found.",Toast.LENGTH_SHORT).show()
                }
            }
        }
        thread.start()
    }

    private fun getJson(url: URL): String? {
        return try {
            val input = BufferedReader(InputStreamReader(url.openStream()))
            val json = input.readLine()
            input.close()

            notFound = false
            json
        }catch (e: Exception){
            notFound = true
            ""
        }
    }
}
