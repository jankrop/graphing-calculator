package com.example.graphingcalculator

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView

/**
 * An adapter for the list of expressions
 */
class MathAdapter(
    private val context: Context,
    private val data: MutableList<MathEntry>,
    private val listener: OnEntryDeletedListener
) : BaseAdapter() {
    interface OnEntryDeletedListener {
        fun onEntryDeleted()
    }

    override fun getCount(): Int = data.size

    override fun getItem(position: Int): Any = data[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_math, parent, false)

        val entry = data[position]

        val functionColor = view.findViewById<View>(R.id.functionColor)
        val expressionText = view.findViewById<TextView>(R.id.expressionText)
        val resultText = view.findViewById<TextView>(R.id.resultText)
        val deleteButton = view.findViewById<ImageButton>(R.id.deleteButton)

        functionColor.setBackgroundColor(Colors.palette[position % 4])
        expressionText.text = "y = ${entry.expression}"
        if (entry.data.all { it == entry.data[0] } && entry.data[0] != null) {
            val resultString = if (entry.data[0]!! % 1 == 0f) { entry.data[0]!!.toInt().toString() } else { entry.data[0].toString() }
            resultText.text = "= $resultString"
        }

        deleteButton.setOnClickListener {
            data.removeAt(position)
            notifyDataSetChanged()

            listener.onEntryDeleted()
        }

        return view
    }
}