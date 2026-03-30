package com.example.graphingcalculator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View

class Graph(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        strokeWidth = 8f
        style = Paint.Style.STROKE
    }

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }

    private val axisPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        strokeWidth = 6f
        style = Paint.Style.STROKE
    }

    private var data: List<Float> = emptyList()
    private var precision: Float = 1f

    fun setData(values: List<Float>, precisionValue: Float) {
        data = values
        precision = precisionValue
        invalidate()
    }

    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (data.isEmpty()) return

        val graphWidth = width.toFloat()
        val graphHeight = height.toFloat()

        val points = data.mapIndexed { index, value ->
            val x = index.toFloat() / 20 * graphWidth * precision
            val y = graphHeight - (value + 10) / 20 * graphHeight
            PointF(x, y)
        }

        for (x in 0..20) {
            if (x == 10) continue
            canvas.drawLine(
                x.toFloat() / 20 * graphWidth, 0f,
                x.toFloat() / 20 * graphWidth, graphHeight, gridPaint
            )
        }

        for (y in 0..20) {
            if (y == 10) continue
            canvas.drawLine(
                0f, y.toFloat() / 20 * graphHeight,
                graphWidth, y.toFloat() / 20 * graphHeight, gridPaint
            )
        }

        canvas.drawLine(graphWidth / 2, 0f, graphWidth / 2, graphHeight, axisPaint)
        canvas.drawLine(0f, graphHeight / 2, graphWidth, graphHeight / 2, axisPaint)

        path.reset()

        points.forEachIndexed { index, point ->
            if (index == 0) {
                path.moveTo(point.x, point.y)
            } else {
                path.lineTo(point.x, point.y)
            }
        }

        canvas.drawPath(path, paint)
    }
}