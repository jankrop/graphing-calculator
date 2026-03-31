package com.example.graphingcalculator

/**
 * A data class representing a mathematical expression together with its values for different values of x
 */
data class MathEntry(
    val expression: String,
    val data: List<Float?>
)
