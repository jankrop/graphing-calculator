package com.example.graphingcalculator

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View

/**
 * A graph view capable of displaying multiple mathematical functions
 */
class Graph(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private fun createPaint(index: Int): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Colors.palette[index]
            strokeWidth = 8f
            style = Paint.Style.STROKE
        }
    }
    private val paints = arrayOf(
        createPaint(0), createPaint(1), createPaint(2), createPaint(3)
    )
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

    private var expressions: ArrayList<String> = arrayListOf()
    private var xAxis = 0f
    private var yAxis = 0f
    private var scale: Float = 1f

    fun setExpressions(values: ArrayList<String>) {
        expressions = values
        invalidate()
    }

    fun moveLeft() {
        xAxis += 1
        invalidate()
    }

    fun moveRight() {
        xAxis -= 1
        invalidate()
    }

    fun moveUp() {
        yAxis += 1
        invalidate()
    }

    fun moveDown() {
        yAxis -= 1
        invalidate()
    }

    fun zoomIn() {
        scale /= 2
        xAxis *= 2
        yAxis *= 2
        invalidate()
    }

    fun zoomOut() {
        scale *= 2
        xAxis /= 2
        yAxis /= 2
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val sharedPref = context.getSharedPreferences("prefs", MODE_PRIVATE)

        val graphWidth = width.toFloat()
        val graphHeight = height.toFloat()

        for (x in 0..20) {
            if (x == 10 + xAxis.toInt()) continue
            canvas.drawLine(
                (x.toFloat() + xAxis % 1) / 20 * graphWidth, 0f,
                (x.toFloat() + xAxis % 1) / 20 * graphWidth, graphHeight, gridPaint
            )
        }

        for (y in 0..20) {
            if (y == 10 + yAxis.toInt()) continue
            canvas.drawLine(
                0f, (y.toFloat() + yAxis % 1) / 20 * graphHeight,
                graphWidth, (y.toFloat() + yAxis % 1) / 20 * graphHeight, gridPaint
            )
        }

        val xAxisPosition = graphHeight / 2 + yAxis / 20 * graphHeight
        val yAxisPosition = graphWidth / 2 + xAxis / 20 * graphWidth

        canvas.drawLine(0f, xAxisPosition, graphWidth, xAxisPosition, axisPaint)
        canvas.drawLine(yAxisPosition, 0f, yAxisPosition, graphHeight, axisPaint)

        expressions.forEachIndexed { exprIndex, expr ->
            val data = parseMath(expr, (-10f - xAxis) * scale, (10f - xAxis) * scale, 0.05f * scale, sharedPref.getBoolean("isRadians", true))
            val points = data.mapIndexed { i, value ->
                if (value == null) {
                    null
                } else {
                    PointF(
                        i.toFloat() / 400 * graphWidth,
                        graphHeight - (value + 10 * scale - yAxis) / 20 * graphHeight / scale
                    )
                }
            }

            val path = Path()

            var wasPreviousStepDefined = false
            points.forEach { point ->
                if (point == null) {
                    wasPreviousStepDefined = false
                } else {
                    if (wasPreviousStepDefined) {
                        path.lineTo(point.x, point.y)
                    } else {
                        path.moveTo(point.x, point.y)
                    }
                    wasPreviousStepDefined = true
                }
            }

            canvas.drawPath(path, paints[exprIndex % 4])
        }
    }
}