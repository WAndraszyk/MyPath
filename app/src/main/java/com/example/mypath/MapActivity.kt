@file:Suppress("DEPRECATION")

package com.example.mypath

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.mypath.databinding.ActivityMapBinding
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
import java.sql.DriverManager


class MapActivity : AppCompatActivity(), OnMapReadyCallback, SensorEventListener {   // , LocationListener

    private lateinit var mCompass: ImageView
    private lateinit var mMapFragment: SupportMapFragment
    private lateinit var mLoading: ProgressBar
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding
    private var mPath = mutableListOf<LatLng>()
    private var mNames = mutableListOf<String>()

    private lateinit var sensorManager: SensorManager
    private lateinit var gsensor: Sensor
    private lateinit var msensor: Sensor

    private val mGravity = FloatArray(3)
    private val mGeomagnetic = FloatArray(3)
    private val arrR = FloatArray(9)
    private val arrI = FloatArray(9)

    private var azimuth = 0f
//    private val azimuthFix = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = DBHelper()
        val exe = db.execute().get()

         binding = ActivityMapBinding.inflate(layoutInflater)
         setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mLoading = this.findViewById(R.id.MapLoading)
        mCompass = this.findViewById(R.id.compassImageView)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        sensorManager.registerListener(this, gsensor,
            SensorManager.SENSOR_DELAY_GAME)
        sensorManager.registerListener(this, msensor,
            SensorManager.SENSOR_DELAY_GAME)

        mMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mMapFragment.view?.visibility = View.GONE
        mCompass.visibility = View.GONE

        val origin: LatLng = mPath[0]
        val dest: LatLng = mPath.last()
        val url = getDirectionsUrl(origin, dest)
        val downloadTask = DownloadTask()
        downloadTask.execute(url)

        var avgLat = .0
        var avgLong = .0
        for(i in 0 until mPath.size)
        {
            avgLat += mPath[i].latitude
            avgLong += mPath[i].longitude
            if(mNames[i] != "")
                mMap.addMarker(MarkerOptions().position(mPath[i]).title(mNames[i]))
        }
        avgLat /= mPath.size
        avgLong /= mPath.size

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
            LatLng(avgLat, avgLong), 10F))

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
    }

    private fun getDirectionsUrl(origin: LatLng, dest: LatLng): String {
        val strOrigin = "origin=" + origin.latitude + "," + origin.longitude
        val strDest = "destination=" + dest.latitude + "," + dest.longitude
        val sensor = "sensor=false"
        var waypoints = ""
        for (i in 1 until mPath.size) {
            val point = mPath[i]
            if (point == dest) break

            if (i == 1) waypoints = "waypoints="

            waypoints += point.latitude.toString() + "," + point.longitude
            if (i < mPath.size-2) waypoints += "|"
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

    @SuppressLint("StaticFieldLeak")
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
                val path = result[0]
                for (j in path.indices) {
                    val point = path[j]
                    val lat = point["lat"]!!.toDouble()
                    val lng = point["lng"]!!.toDouble()
                    val position = LatLng(lat, lng)
                    points.add(position)
                }
            }
            lineOptions.addAll(points)
            lineOptions.width(12f)
            lineOptions.color(Color.BLUE)

            // Drawing polyline in the Google Map for the entire route
            mMap.addPolyline(lineOptions)
            mLoading.visibility = View.GONE
            mCompass.visibility = View.VISIBLE
            mMapFragment.view?.visibility = View.VISIBLE
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

    @SuppressLint("StaticFieldLeak")
    inner class DBHelper : AsyncTask<Void, Void, Void>() {
        private var error = ""

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: Void?): Void? {
            try{
                val sharedScore = getSharedPreferences("com.example.mypath.shared",0)
                val routeId = sharedScore?.getInt("route_id", -1)

                Class.forName("com.mysql.jdbc.Driver").newInstance()
                val url= "jdbc:mysql://10.0.2.2:3306/mypath"
                val connection = DriverManager.getConnection(url, "root","haslo")
                val statement = connection.createStatement()
                val resultSet = statement.executeQuery("select * from points where route_id = $routeId order by point_no;")
                while(resultSet.next()){
                    val name = resultSet.getString(3)
                    val latitude = resultSet.getDouble(4)
                    val longitude = resultSet.getDouble(5)
                    mPath.add(LatLng(latitude, longitude))
                    mNames.add(name)
                }

            }catch (e: Exception){
                error = e.toString()
                println("----------------DATABASE ERROR: $error--------------")
            }

            return null
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        val alpha = 0.97f
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            mGravity[0] = alpha * mGravity[0] + (1 - alpha) * event.values[0]
            mGravity[1] = alpha * mGravity[1] + (1 - alpha) * event.values[1]
            mGravity[2] = alpha * mGravity[2] + (1 - alpha) * event.values[2]

            val success = SensorManager.getRotationMatrix(arrR, arrI, mGravity, mGeomagnetic)
            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(arrR, orientation)
                azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat() // orientation
                azimuth = (azimuth + 360) % 360
                mCompass.rotation = -azimuth
            }
        }
        if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha) * event.values[0]
            mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha) * event.values[1]
            mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha) * event.values[2]

            val success = SensorManager.getRotationMatrix(arrR, arrI, mGravity, mGeomagnetic)
            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(arrR, orientation)
                azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat() // orientation
                azimuth = (azimuth + 360) % 360
                mCompass.rotation = -azimuth
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }
}