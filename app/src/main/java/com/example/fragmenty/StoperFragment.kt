package com.example.fragmenty

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.sql.DriverManager
import java.sql.Time
import java.util.*

class StoperFragment : Fragment() {
    private var seconds = 0
    private var running = false
    private var wasRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null){
            seconds = savedInstanceState.getInt("seconds")
            running = savedInstanceState.getBoolean("running")
            wasRunning = savedInstanceState.getBoolean("wasRunning")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_stoper, container, false)
        runStoper(layout)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val startButton = view.findViewById<Button>(R.id.start_button)
        val stopButton = view.findViewById<Button>(R.id.stop_button)
        val resetButton = view.findViewById<Button>(R.id.reset_button)
        val saveButton = view.findViewById<Button>(R.id.save_button)

        startButton.setOnClickListener(){
            onClickStart()
        }
        stopButton.setOnClickListener(){
            onClickStop()
        }
        resetButton.setOnClickListener(){
            onClickReset()
        }
        saveButton.setOnClickListener(){
            onClickSave()
        }
    }

    override fun onPause() {
        super.onPause()
        wasRunning = running
        running = false
    }

    override fun onResume() {
        super.onResume()
        if(wasRunning){
            running = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("seconds", seconds)
        outState.putBoolean("running", running)
        outState.putBoolean("wasRunning", wasRunning)
    }

    private fun onClickStart() {
        running = true
    }

    private fun onClickStop() {
        running = false
    }

    private fun onClickReset() {
        running = false
        seconds = 0
    }

    private fun onClickSave() {
        if (!running) {
            val saver = DBSaver()
            saver.execute()
        }else{
            Toast.makeText(activity,"Counting still running!",Toast.LENGTH_SHORT).show()
        }
    }

    private fun runStoper(view: View){
        val timeView = view.findViewById<TextView>(R.id.time_view)
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                val hours = seconds / 3600
                val minutes = seconds % 3600 / 60
                val secs = seconds % 60
                val time = String.format("%d:%02d:%02d", hours, minutes, secs)
                timeView.text = time
                if (running) {
                    seconds++
                }
                handler.postDelayed(this, 1000)
            }
        })
    }

    @SuppressLint("StaticFieldLeak")
    inner class DBSaver : AsyncTask<Void, Void, Void>() {
        var error = ""

        private fun insert(time: Time, date: String) {
            val sharedScore = activity?.getSharedPreferences("com.example.fragmenty.shared",0)
            val routeId = sharedScore?.getInt("route_id", -1)
            val user = sharedScore?.getString("username", "")
            try{
                Class.forName("com.mysql.jdbc.Driver").newInstance()
                val url= "jdbc:mysql://10.0.2.2:3306/fragmenty"
                val connection = DriverManager.getConnection(url, "root","haslo")
                val statement = connection.createStatement()
                statement.executeUpdate("insert into routes_times values('$date', '$time', '$routeId', '$user');")
                activity!!.runOnUiThread {
                    Toast.makeText(activity,"Saved to database!",Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                error = e.toString()
                println("----------------DATABASE ERROR: $error--------------")
            }
        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: Void?): Void? {
            val hours = seconds / 3600
            val minutes = seconds % 3600 / 60
            val secs = seconds % 60
            val time = Time(hours, minutes, secs)
            val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
            val date = sdf.format(System.currentTimeMillis());
            insert(time, date)

            return null
        }
    }
}