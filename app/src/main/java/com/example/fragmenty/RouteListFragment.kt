package com.example.fragmenty

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentTransaction


class RouteListFragment() : Fragment(R.layout.fragment_route_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val namesListView = view.findViewById(R.id.name_list) as ListView
        val names = mutableListOf<String>()

        val db = DBHelper(requireContext())

        val routes = db.loadRoutes()

        for (i in routes) {
            names.add(i.getName())
        }

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
}