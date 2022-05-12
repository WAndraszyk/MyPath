package com.example.fragmenty

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentTransaction
import java.sql.DriverManager


class RouteListFragment() : Fragment(R.layout.fragment_route_list) {
    val routes = mutableListOf<Route>()
    private val names = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = DBHelper()
        val exe = db.execute().get()

        for (i in routes) {
            names.add(i.getName())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val namesListView = view.findViewById(R.id.name_list) as ListView

        val r = (view.parent as ViewGroup).parent as LinearLayoutCompat

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), android.R.layout.simple_list_item_1, names)
        namesListView.adapter = adapter
        namesListView.setOnItemClickListener { parent, _, position, _ ->
            parent.getItemAtPosition(position) // The item that was clicked
            val fragmentContainer = r.findViewById<FragmentContainerView>(R.id.fragment_container)
            if(fragmentContainer != null){
                val details = RouteDetailFragment()
                val ft = parentFragmentManager.beginTransaction()
                details.setRoute(position)
                ft.replace(R.id.fragment_container, details)
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                ft.addToBackStack(null)
                ft.commit()
            }else{
                val intent = Intent(requireContext(), DetailActivity()::class.java)
                intent.putExtra("routeId", position)
                startActivity(intent)
            }
        }
    }

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