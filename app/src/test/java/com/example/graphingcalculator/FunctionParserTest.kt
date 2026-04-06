package com.example.graphingcalculator

import org.junit.Assert.*
import org.junit.Test
import kotlin.math.PI

class FunctionParserTest {

    /**
     * Verifies that a simple polynomial is evaluated correctly.
     * f(x) = x^2 + 3x - 2
     */
    @Test
    fun testPolynomial() {
        val result = parseMath("-x^2 + 3x - 2", 0f, 2f, 1f)

        assertEquals(listOf(-2f, 0f, 0f), result)
    }

    /**
     * Ensures division by zero produces null instead of crashing.
     * f(x) = 1/x
     */
    @Test
    fun testDivisionByZero() {
        val result = parseMath("1/x", -1f, 1f, 1f)

        assertEquals(3, result.size)
        assertEquals(-1f, result[0])
        assertNull(result[1])   // x = 0 → undefined
        assertEquals(1f, result[2])
    }

    /**
     * Validates trigonometric function handling in radians.
     * sin(pi/2) = 1
     */
    @Test
    fun testTrigFunctions() {
        val result = parseMath("sin(x)", (PI / 2).toFloat(), (PI / 2).toFloat(), 1f)

        assertEquals(1, result.size)
        assertNotNull(result[0])
        assertEquals(1f, result[0]!!, 1e-4f)
    }

    /**
     * Tests logarithms and mathematical constants.
     * log(100) = 2
     * ln(e) = 1
     */
    @Test
    fun testLogsAndConstants() {
        val result1 = parseMath("log(100)", 0f, 0f, 1f)
        val result2 = parseMath("ln(e)", 0f, 0f, 1f)

        assertEquals(2f, result1[0])
        assertEquals(1f, result2[0]!!, 1e-4f)
    }

    /**
     * Confirms nested functions and precedence are evaluated correctly.
     * sqrt(4 + sin(pi/2)^2) = sqrt(5)
     */
    @Test
    fun testNestedFunctions() {
        val result = parseMath("sqrt(4 + sin(pi/2)^2)", 0f, 0f, 1f)

        assertNotNull(result[0])
        assertEquals(kotlin.math.sqrt(5f), result[0]!!, 1e-4f)
    }

    /**
     * Tests whether an IllegalArgumentException is thrown for unexpected identifiers.
     */
    @Test(expected = IllegalArgumentException::class)
    fun testUnknownIdentifier() {
        parseMath("h", 0f, 1f, 1f)
    }
}