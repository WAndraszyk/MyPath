package com.example.fragmenty

class Route (private val name: String, private val way: String, private val image: String){

    fun getName(): String {
        return name
    }

    fun getWay(): String {
        return way
    }

    fun getImage(): String {
        return image
    }

    override fun toString(): String {
        return name
    }
}