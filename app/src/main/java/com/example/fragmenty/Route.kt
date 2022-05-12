package com.example.fragmenty

class Route (private val name: String, private val way: String){

    fun getName(): String {
        return name
    }

    fun getWay(): String {
        return way
    }

    override fun toString(): String {
        return name
    }
}