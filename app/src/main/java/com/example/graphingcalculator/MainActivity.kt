package com.example.graphingcalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sin

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = parseMath("1/x", -10f, 10f, 0.1f)

        println("$data")

        val graph = findViewById<Graph>(R.id.graph)
        graph.setData(data, 0.1f)
    }
}