package com.example.fragmenty

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.os.AsyncTask
import android.os.StrictMode
import java.sql.DriverManager

//class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
//    companion object {
//        private const val DATABASE_VERSION = 4
//        private const val DATABASE_NAME = "RoutesDB.db"
//        //Table Players
//        private const val TABLE_NAME = "Routes"
//        private const val COL_NAME = "Name"
//        private const val COL_WAY = "Way"
//    }
//
//    override fun onCreate(db: SQLiteDatabase?) {
//        val createTableQuery = ("CREATE TABLE $TABLE_NAME ($COL_NAME VARCHAR INT PRIMARY KEY, $COL_WAY VARCHAR)")
//        db!!.execSQL(createTableQuery)
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        val upgradeTableQuery = ("DROP TABLE IF EXISTS $TABLE_NAME")
//        db!!.execSQL(upgradeTableQuery)
//        onCreate(db)
//    }
//
//    fun loadRoutes(): MutableList<Route> {
//        val routes = mutableListOf<Route>()
//        val selectQuery = ("SELECT $COL_NAME, $COL_WAY FROM $TABLE_NAME")
//        val db = this.writableDatabase
//        val cursor =  db.rawQuery(selectQuery, null)
//        if(cursor.moveToFirst()){
//            do {
//                val name = cursor.getString(0)
//                val way = cursor.getString(1)
//
//                routes.add(Route(name, way))
//            } while (cursor.moveToNext())
//        }
//        cursor.close()
//        db.close()
//        return routes
//    }
//
//    fun addRoute(name: String, way: String){
//        val values = ContentValues()
//        values.put(COL_NAME, name)
//        values.put(COL_WAY, way)
//        val db = this.writableDatabase
//        db.insert(TABLE_NAME, null, values)
//        db.close()
//    }
//}

