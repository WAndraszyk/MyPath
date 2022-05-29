package com.example.fragmenty

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TableLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.sql.DriverManager
import java.text.FieldPosition


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
                tab.text = "Tab " + (position + 1)
            }.attach()
        }

        return view
    }
}


