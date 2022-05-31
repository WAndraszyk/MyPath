package com.example.fragmenty

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.sql.Date
import java.sql.DriverManager
import java.sql.Time

class RouteStatisticsFragment : Fragment() {
    val routes = mutableListOf<Route>()
    private var record = Time(0,0,0)
    private var recordDate = Date(0,0,0)
    private var lastTime = Time(0,0,0)
    private var lastTimeDate = Date(0,0,0)
    private var name = ""
    private var way = ""
    private var image = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_route_statistics, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageView = view.findViewById<ImageView>(R.id.image)
        val title = view.findViewById<TextView>(R.id.textTitle)
        val db = DBHelper()
        val exe = db.execute().get()
        val id = context?.resources?.getIdentifier("drawable/$image", null, context?.packageName)
        if (id != null) {
            imageView.setImageResource(id)
        }
        title.text = name
        val textRecord = view.findViewById<TextView>(R.id.textRecord)
        textRecord.text = "Record of the route: $record on $recordDate"
        val textLastTime = view.findViewById<TextView>(R.id.textLastTime)
        textLastTime.text = "Most recent time: $lastTime on $lastTimeDate"
    }

    @SuppressLint("StaticFieldLeak")
    inner class DBHelper : AsyncTask<Void, Void, Void>() {
        var error = ""

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: Void?): Void? {
            try{
                Class.forName("com.mysql.jdbc.Driver").newInstance()
                val url= "jdbc:mysql://10.0.2.2:3306/fragmenty"
                val connection = DriverManager.getConnection(url, "root","haslo")
                val statement = connection.createStatement()
                var resultSet = statement.executeQuery("select * from routes;")
                while(resultSet.next()){
                    val name = resultSet.getString(1)
                    val way = resultSet.getString(2)
                    val image = resultSet.getString(3)
                    val type = resultSet.getString(4)
                    routes.add(Route(name, way, image, type))
                }

                val sharedScore = activity?.getSharedPreferences("com.example.fragmenty.shared",0)
                val routeId = sharedScore?.getInt("id", -1)
                val route = routes[routeId!!]
                name = route.getName()
                way = route.getWay()
                image = route.getImage()
                val edit = sharedScore.edit()
                edit.putString("name", name)
                edit.apply()

                resultSet = statement.executeQuery("select date, score, route from routes_times \n" +
                        "where score = (select min(score) from routes_times where route = '$name')\n" +
                        "and route = '$name'")
                while(resultSet.next()){
                    recordDate = resultSet.getDate(1)
                    record = resultSet.getTime(2)
                }
                resultSet = statement.executeQuery("select date, score, route from routes_times \n" +
                        "where date = (select max(date) from routes_times where route = '$name')\n" +
                        "and route = '$name'")
                while(resultSet.next()){
                    lastTimeDate = resultSet.getDate(1)
                    lastTime = resultSet.getTime(2)
                }
            }catch (e: Exception){
                error = e.toString()
                println("----------------DATABASE ERROR: $error--------------")
            }

            return null
        }
    }
}