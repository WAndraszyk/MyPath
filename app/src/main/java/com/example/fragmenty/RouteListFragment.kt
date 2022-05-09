package com.example.fragmenty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.ListFragment

class RouteListFragment : ListFragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val names = arrayListOf<String>()
            for (i in Route().routes) {
                names.add(i.getName())
            }
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(inflater.context, android.R.layout.simple_list_item_1, names)
            listAdapter = adapter
            // Inflate the layout for this fragment
            return super.onCreateView(inflater, container, savedInstanceState)
        }
}