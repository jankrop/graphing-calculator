package com.example.graphingcalculator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.exp
import kotlin.math.sin

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val graph = findViewById<Graph>(R.id.graph)
        val expressionInput = findViewById<EditText>(R.id.expressionInput)
        val plotButton = findViewById<Button>(R.id.plotButton)

        plotButton.setOnClickListener {
            val data = parseMath(expressionInput.text.toString(), -10f, 10f, 0.1f)
            graph.setData(data, 0.1f)
        }
    }
}