package com.example.fragmenty

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.sql.DriverManager

class RouteDetailFragment : Fragment() {
    private var routeId = -1
    val routes = mutableListOf<Route>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState != null){
            routeId = savedInstanceState.getInt("routeId")
        }
        val db = DBHelper()
        val exe = db.execute().get()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_route_detail, container, false)
    }

    fun setRoute(id: Int){
        routeId = id
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = view.findViewById<TextView>(R.id.textTitle)

        if (routeId >= 0) {
            val route = routes[routeId]
            title.text = route.getName()
            val description = view.findViewById<TextView>(R.id.textDescription)
            description.text = route.getWay()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("routeId", routeId)
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
                val resultSet = statement.executeQuery("select * from routes;")
                while(resultSet.next()){
                    val name = resultSet.getString(1)
                    val way = resultSet.getString(2)
                    routes.add(Route(name, way))
                }
            }catch (e: Exception){
                error = e.toString()
                println("----------------DATABASE ERROR: $error--------------")
            }

            return null
        }
    }
}