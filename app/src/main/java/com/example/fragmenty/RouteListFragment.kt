package com.example.fragmenty

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class RouteListFragment() : Fragment(R.layout.fragment_route_list) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val tableLayout = view?.findViewById<TabLayout>(R.id.tabs)
        val pager = view?.findViewById<ViewPager2>(R.id.pager)

        val adapter = ViewPageAdapter(this)
        pager?.adapter = adapter

        if (pager != null && tableLayout != null) {
            TabLayoutMediator(tableLayout, pager) { tab, position ->
                when (position){
                    0 -> tab.text = "Home"
                    1 -> tab.text = "Bicycle"
                    2 -> tab.text = "Walking"
                }
            }.attach()
        }

        val logOut = view?.findViewById<ImageView>(R.id.logOutIcon)
        logOut?.setOnClickListener{
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent)
        }

        return view
    }
}


