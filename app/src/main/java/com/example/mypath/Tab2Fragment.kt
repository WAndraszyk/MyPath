package com.example.mypath

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.sql.DriverManager


class Tab2Fragment : Fragment(R.layout.fragment_tab2) {

    val routes = mutableListOf<Route>()
    private val names = mutableListOf<String>()
    private val images = mutableListOf<Int>()
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = DBHelper()
        val exe = db.execute().get()

        for (i in routes) {
            names.add(i.getName())
            val image = i.getImage()
            val id = context?.resources?.getIdentifier("drawable/$image", null, context?.packageName)
            if (id != null){
                images.add(id)
            }
        }

        layoutManager = GridLayoutManager(requireContext(),2)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = layoutManager

        adapter = RecyclerAdapter(names, images) { position -> onItemClick(position) }
        recyclerView.adapter = adapter
    }

    private fun onItemClick(position: Int) {
        val frag = this.parentFragment?.view?.parent as ViewGroup

        val r = if((resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE){
            frag.parent as LinearLayoutCompat
        }else{
            frag.parent as RelativeLayout
        }

        val fragmentContainer = r.findViewById<FragmentContainerView>(R.id.fragment_container)
        if(fragmentContainer != null){
            val details = RouteDetailFragment()
            val ft = activity?.supportFragmentManager?.beginTransaction()
            setRoute(position)
            ft?.replace(R.id.fragment_container, details)
            ft?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft?.addToBackStack(null)
            ft?.commit()
        }else{
            val intent = Intent(requireContext(), DetailActivity()::class.java)
            intent.putExtra("routeId", position)
            startActivity(intent)
        }
    }

    private fun setRoute(id: Int){
        val sharedScore = activity?.getSharedPreferences("com.example.mypath.shared",0)
        val edit = sharedScore?.edit()
        edit?.putInt("id", id)
        edit?.apply()
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
                val resultSet = statement.executeQuery("select * from routes where type = 'bicycle';")
                while(resultSet.next()){
                    val route_id = resultSet.getInt(1)
                    val name = resultSet.getString(2)
                    val way = resultSet.getString(3)
                    val image = resultSet.getString(4)
                    val type = resultSet.getString(5)
                    routes.add(Route(route_id, name, way, image, type))
                }
            }catch (e: Exception){
                error = e.toString()
                println("----------------DATABASE ERROR: $error--------------")
            }

            return null
        }
    }
}