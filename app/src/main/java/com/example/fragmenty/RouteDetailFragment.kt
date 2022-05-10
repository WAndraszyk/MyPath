package com.example.fragmenty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class RouteDetailFragment : Fragment() {
    private var routeId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState != null){
            routeId = savedInstanceState.getInt("routeId")
        }
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
        val db = DBHelper(requireContext())

        val routes = db.loadRoutes()

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
}