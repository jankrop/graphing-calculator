package com.example.graphingcalculator

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

/**
 * The app's main activity, containing the graph
 */
class MainActivity : AppCompatActivity(), MathAdapter.OnEntryDeletedListener {
    private lateinit var adapter: MathAdapter
    private val expressions = arrayListOf<String>()
    private lateinit var graph: Graph

    override fun onEntryDeleted() {
        graph.setExpressions(expressions)
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
        val settingsButton = findViewById<ImageButton>(R.id.settingsButton)

        adapter = MathAdapter(this, expressions, this)
        expressionList.adapter = adapter

        findViewById<ImageButton>(R.id.leftButton).setOnClickListener { graph.moveLeft() }
        findViewById<ImageButton>(R.id.rightButton).setOnClickListener { graph.moveRight() }
        findViewById<ImageButton>(R.id.upButton).setOnClickListener { graph.moveUp() }
        findViewById<ImageButton>(R.id.downButton).setOnClickListener { graph.moveDown() }
        findViewById<ImageButton>(R.id.zoomInButton).setOnClickListener { graph.zoomIn() }
        findViewById<ImageButton>(R.id.zoomOutButton).setOnClickListener { graph.zoomOut() }

        plotButton.setOnClickListener {
            val expression = expressionInput.text.toString()

            try {
                expressions.add(expression)
                expressionInput.setText("")
                adapter.notifyDataSetChanged()
                graph.setExpressions(expressions)
            } catch (e: Exception) {
                Snackbar.make(findViewById(android.R.id.content), "Malformed expression", Snackbar.LENGTH_SHORT).show()
            }

        }

        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}