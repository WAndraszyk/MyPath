package com.example.fragmenty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class RouteDetailFragment : Fragment() {
    private var routeId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_route_detail, container, false)
    }

    fun setRoute(id: Int){
        routeId = id
    }

    override fun onStart() {
        super.onStart()
        val title = requireView().findViewById<TextView>(R.id.textTitle)

        val routes = arrayListOf<Route>(
            Route("Trasa1", "Szczegóły trasy 1"),
            Route("Trasa2", "Szczegóły trasy 2")
        )

        val route = routes[routeId]
        title.text = route.getName()
        val description = requireView().findViewById<TextView>(R.id.textDescription)
        description.text = route.getWay()
    }
}