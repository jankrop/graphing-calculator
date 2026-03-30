package com.example.graphingcalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sin

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = arrayListOf<Float>()
        for (i in -100..100) {
            data.add(sin(i.toFloat() / 10))
        }

        println("$data")

        val graph = findViewById<Graph>(R.id.graph)
        graph.setData(data.toList(), 0.1f)
    }
}