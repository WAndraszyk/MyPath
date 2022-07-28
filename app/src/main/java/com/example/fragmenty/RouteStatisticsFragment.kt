package com.example.fragmenty

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.fragment.app.FragmentTransaction
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
    private var route_id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val stoper = StoperFragment()
            val ft = childFragmentManager.beginTransaction()
            ft.add(R.id.stoper_container, stoper)
            ft.addToBackStack(null)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.commit()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_route_statistics, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)

        val logOut = view.findViewById<ImageView>(R.id.logOutIcon)
        logOut.setOnClickListener{
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent)
        }

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
                var resultSet = statement.executeQuery("select * from routes order by type, name;")
                while(resultSet.next()){
                    val route_id = resultSet.getInt(1)
                    val name = resultSet.getString(2)
                    val way = resultSet.getString(3)
                    val image = resultSet.getString(4)
                    val type = resultSet.getString(5)
                    routes.add(Route(route_id, name, way, image, type))
                }

                val sharedScore = activity?.getSharedPreferences("com.example.fragmenty.shared",0)
                val routeId = sharedScore?.getInt("id", -1)
                val user = sharedScore?.getString("username", "")
                val route = routes[routeId!!]
                route_id = route.getRouteId()
                name = route.getName()
                way = route.getWay()
                image = route.getImage()
                val edit = sharedScore.edit()
                edit.putInt("route_id", route_id)
                edit.putString("name", name)
                edit.apply()

                resultSet = statement.executeQuery("select date, score, route from routes_times \n" +
                        "where score = (select min(score) from routes_times where route = '$name' and user = '$user')\n" +
                        "and route = '$name' and user = '$user';")
                while(resultSet.next()){
                    recordDate = resultSet.getDate(1)
                    record = resultSet.getTime(2)
                }
                resultSet = statement.executeQuery("select date, score, route from routes_times \n" +
                        "where date = (select max(date) from routes_times where route = '$name' and user = '$user')\n" +
                        "and route = '$name' and user = '$user';")
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