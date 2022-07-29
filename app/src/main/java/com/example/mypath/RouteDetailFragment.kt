package com.example.mypath

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.sql.DriverManager


class RouteDetailFragment : Fragment() {
    val routes = mutableListOf<Route>()
    private var name = ""
    private var way = ""
    private var image = ""
    private var route_id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_route_detail, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        val typedValue = TypedValue()
        val theme: Resources.Theme? = activity?.theme
        theme?.resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true)
        @ColorInt val color = typedValue.data
        (activity as AppCompatActivity?)!!.supportActionBar?.setBackgroundDrawable(ColorDrawable(color))

        val logOut = view.findViewById<ImageView>(R.id.logOutIcon)
        logOut.setOnClickListener{
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent)
            activity?.finish()
        }

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)

        fab.setOnClickListener(){
            val intent = Intent(requireContext(), StatisticsActivity()::class.java)
            startActivity(intent)
        }

        val map = view.findViewById<FloatingActionButton>(R.id.mapfab)

        map.setOnClickListener(){
            val intent = Intent(requireContext(), MapActivity()::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = view.findViewById<TextView>(R.id.textTitle)
        val imageView = view.findViewById<ImageView>(R.id.image)
        val db = DBHelper()
        val exe = db.execute().get()
        val id = context?.resources?.getIdentifier("drawable/$image", null, context?.packageName)
        if (id != null) {
            imageView.setImageResource(id)
        }
        title.text = name
        val description = view.findViewById<TextView>(R.id.textDescription)
        description.text = way
    }

    @SuppressLint("StaticFieldLeak")
    inner class DBHelper : AsyncTask<Void, Void, Void>() {
        var error = ""

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: Void?): Void? {
            try{
                Class.forName("com.mysql.jdbc.Driver").newInstance()
                val url= "jdbc:mysql://10.0.2.2:3306/mypath"
                val connection = DriverManager.getConnection(url, "root","haslo")
                val statement = connection.createStatement()
                val resultSet = statement.executeQuery("select * from routes order by type, name;")
                while(resultSet.next()){
                    val route_id = resultSet.getInt(1)
                    val name = resultSet.getString(2)
                    val way = resultSet.getString(3)
                    val image = resultSet.getString(4)
                    val type = resultSet.getString(5)
                    routes.add(Route(route_id, name, way, image, type))
                }

                val sharedScore = activity?.getSharedPreferences("com.example.mypath.shared",0)
                val routeId = sharedScore?.getInt("id", -1)
                val route = routes[routeId!!]
                route_id = route.getRouteId()
                name = route.getName()
                way = route.getWay()
                image = route.getImage()
                val edit = sharedScore.edit()
                edit.putInt("route_id", route_id)
                edit.putString("name", name)
                edit.apply()
            }catch (e: Exception){
                error = e.toString()
                println("----------------DATABASE ERROR: $error--------------")
            }

            return null
        }
    }
}