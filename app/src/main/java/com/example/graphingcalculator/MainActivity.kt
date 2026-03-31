package com.example.graphingcalculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import kotlin.math.exp
import kotlin.math.sin

class MainActivity : AppCompatActivity(), MathAdapter.OnEntryDeletedListener {
    private lateinit var adapter: MathAdapter
    private val expressions = arrayListOf<MathEntry>()
    private lateinit var graph: Graph

    override fun onEntryDeleted() {
        graph.setData(expressions)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val root = findViewById<View>(android.R.id.content)

        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            view.setPadding(0, 0, 0, imeInsets.bottom)
            insets
        }

        graph = findViewById(R.id.graph)
        val expressionInput = findViewById<EditText>(R.id.expressionInput)
        val plotButton = findViewById<Button>(R.id.plotButton)
        val expressionList = findViewById<ListView>(R.id.expressionList)

        adapter = MathAdapter(this, expressions, this)
        expressionList.adapter = adapter

        plotButton.setOnClickListener {
            val expression = expressionInput.text.toString()

            try {
                expressions.add(MathEntry(
                    expression,
                    parseMath(expression, -10f, 10f, 0.1f)
                ))
                expressionInput.setText("")
                adapter.notifyDataSetChanged()
                graph.setData(expressions)
            } catch (e: Exception) {
                Snackbar.make(findViewById(android.R.id.content), "Malformed expression", Snackbar.LENGTH_SHORT).show()
            }

        }
    }
}