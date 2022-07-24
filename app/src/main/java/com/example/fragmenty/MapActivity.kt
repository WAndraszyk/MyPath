@file:Suppress("DEPRECATION")

package com.example.fragmenty

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.example.fragmenty.databinding.ActivityMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MapActivity : AppCompatActivity(), OnMapReadyCallback {   // , LocationListener

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding
    private lateinit var path: List<LatLng>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        path =
            listOf(
                LatLng(41.385064,2.173403),
                LatLng(41.648823,-0.889085),
                LatLng(40.416775,-3.70379))

     binding = ActivityMapBinding.inflate(layoutInflater)
     setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
//        val path: List<LatLng> = ArrayList()

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)

        for(point in path)
        {
            val pointName = "test"
            mMap.addMarker(MarkerOptions().position(point).title(pointName))
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(path[0]))

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        }

        val origin: LatLng = path[0]
        val dest: LatLng = path.last()
        val url = getDirectionsUrl(origin, dest)
        val downloadTask = DownloadTask()
        downloadTask.execute(url)
    }

    private fun getDirectionsUrl(origin: LatLng, dest: LatLng): String {
        val strOrigin = "origin=" + origin.latitude + "," + origin.longitude
        val strDest = "destination=" + dest.latitude + "," + dest.longitude
        val sensor = "sensor=false"
        var waypoints = ""
        for (i in 1 until path.size) {
            val point = path[i]
            if (point == dest) break

            if (i == 1) waypoints = "waypoints="

            waypoints += point.latitude.toString() + "," + point.longitude
            if (i < path.size-1) waypoints += "|"
        }
        val key = "key=AIzaSyAbEFjkjnSmgej_CqAasJkH_YCmgufFORs"
        val parameters = "$strOrigin&$strDest&$sensor&$waypoints&$key"
        val output = "json"
        Log.d("URL",parameters)
        return "https://maps.googleapis.com/maps/api/directions/${output}?${parameters}"
    }

    @SuppressLint("LongLogTag")
    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connect()
            iStream = urlConnection.inputStream
            val br = BufferedReader(InputStreamReader(iStream))
            val sb = StringBuffer()
            var line: String?
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }
            data = sb.toString()
            br.close()
        } catch (e: Exception) {
            Log.d("Exception while downloading url", e.toString())
        } finally {
            iStream?.close()
            urlConnection?.disconnect()
        }
        return data
    }

    inner class DownloadTask : AsyncTask<String?, Void?, String>() {

        override fun doInBackground(vararg url: String?): String {
            var data = ""
            try {
                data = url[0]?.let { downloadUrl(it) }.toString()
            } catch (e: java.lang.Exception) {
                Log.d("Background Task", e.toString())
            }
            Log.d("data", data)
            return data
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            val parserTask = ParserTask()
            parserTask.execute(result)
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class ParserTask : AsyncTask<String, Int, List<List<HashMap<String, String>>>>() {

        override fun doInBackground(vararg jsonData: String?): List<List<HashMap<String, String>>> {

            val jObject : JSONObject
            val routes : List<List<HashMap<String, String>>>

            try {
                jObject = JSONObject(jsonData[0]!!)
                val parser = DirectionsJSONParser()

                routes = parser.parse(jObject)
                return routes
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return emptyList()
        }

        override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
            val points: ArrayList<LatLng> = ArrayList()
            val lineOptions = PolylineOptions()
            if (result != null) {
                for (i in result.indices) {
                    val path = result[i]
                    for (j in path.indices) {
                        val point = path[j]
                        val lat = point["lat"]!!.toDouble()
                        val lng = point["lng"]!!.toDouble()
                        val position = LatLng(lat, lng)
                        points.add(position)
                    }
                }
            }
            lineOptions.addAll(points)
            lineOptions.width(12f)
            lineOptions.color(Color.RED)

            // Drawing polyline in the Google Map for the entire route
            mMap.addPolyline(lineOptions)
        }

        inner class DirectionsJSONParser {
            fun parse(jObject: JSONObject): List<List<HashMap<String, String>>> {
                val routes: MutableList<List<HashMap<String, String>>> = ArrayList()
                val jRoutes: JSONArray
                var jLegs: JSONArray
                var jSteps: JSONArray
                try {
                    jRoutes = jObject.getJSONArray("routes")
                    for (i in 0 until jRoutes.length()) {
                        jLegs = (jRoutes[i] as JSONObject).getJSONArray("legs")
                        val path: MutableList<HashMap<String, String>> = ArrayList()
                        for (j in 0 until jLegs.length()) {
                            jSteps = (jLegs[j] as JSONObject).getJSONArray("steps")
                            for (k in 0 until jSteps.length()) {
                                val polyline: String = ((jSteps[k] as JSONObject)["polyline"] as JSONObject)["points"] as String
                                val list = decodePoly(polyline)
                                for (l in list.indices) {
                                    val hm: HashMap<String, String> = HashMap()
                                    hm["lat"] = list[l].latitude.toString()
                                    hm["lng"] = list[l].longitude.toString()
                                    path.add(hm)
                                }
                            }
                            routes.add(path)
                        }
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
                return routes
            }

            private fun decodePoly(encoded: String): List<LatLng> {
                val poly: MutableList<LatLng> = ArrayList()
                var index = 0
                val len = encoded.length
                var lat = 0
                var lng = 0
                while (index < len) {
                    var b: Int
                    var shift = 0
                    var result = 0
                    do {
                        b = encoded[index++].code - 63
                        result = result or (b and 0x1f shl shift)
                        shift += 5
                    } while (b >= 0x20)
                    val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
                    lat += dlat
                    shift = 0
                    result = 0
                    do {
                        b = encoded[index++].code - 63
                        result = result or (b and 0x1f shl shift)
                        shift += 5
                    } while (b >= 0x20)
                    val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
                    lng += dlng
                    val p = LatLng(
                        lat.toDouble() / 1E5,
                        lng.toDouble() / 1E5
                    )
                    poly.add(p)
                }
                return poly
            }
        }
    }
}