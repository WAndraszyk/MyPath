package com.example.fragmenty

class Route (){
    private val name: String = ""
    private val way: String = ""

    constructor(name: String, way: String) : this()

    val routes = arrayListOf(
        Route("Circle","Just walk around."),
        Route("Square","Turn around 90 degrees a few times.")
    )

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