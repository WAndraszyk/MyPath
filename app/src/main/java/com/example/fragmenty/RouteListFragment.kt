package com.example.fragmenty

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment

class RouteListFragment : Fragment(R.layout.fragment_route_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val namesListView = view.findViewById(R.id.name_list) as ListView
        val names = mutableListOf<String>()

        val routes = arrayListOf(
            Route("Trasa1", "Szczegóły trasy 1"),
            Route("Trasa2", "Szczegóły trasy 2")
        )

        for (i in routes) {
            names.add(i.getName())
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), android.R.layout.simple_list_item_1, names)
        namesListView.adapter = adapter
        namesListView.setOnItemClickListener { parent, _, position, _ ->
            val element = parent.getItemAtPosition(position) // The item that was clicked
            val intent = Intent(requireContext(), DetailActivity()::class.java)
            intent.putExtra("routeId", position)
            startActivity(intent)
        }
    }
}