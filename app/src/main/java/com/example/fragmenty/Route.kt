package com.example.fragmenty

class Route (private val name: String, private val way: String, private val image: String, private val type: String){

    fun getName(): String {
        return name
    }

    fun getWay(): String {
        return way
    }

    fun getImage(): String {
        return image
    }

    fun getType(): String {
        return type
    }

    override fun toString(): String {
        return name
    }
}