package com.example.graphingcalculator

import kotlin.math.*

private val functions = setOf("sqrt", "sin", "cos", "tan", "log", "ln")

private val constants = mapOf(
    "pi" to PI.toFloat(),
    "e" to E.toFloat()
)

/**
 * Parses a mathematical expression and evaluates it for every x in the range
 * [minX, maxX] inclusive using the given precision as a step.
 *
 * Returns a list of Float? where null represents that the function is undefined
 * at a given x (e.g., division by zero).
 */
fun parseMath(
    expression: String,
    minX: Float,
    maxX: Float,
    precision: Float
): List<Float?> {
    val tokens = tokenize(expression.replace(" ", ""))
    val rpn = toRPN(tokens)

    val results = mutableListOf<Float?>()
    var x = minX
    while (x <= maxX + 1e-6f) {
        results.add(evalRPN(rpn, x))
        x += precision
    }
    return results
}

/**
 * Splits the expression string into tokens (numbers, operators, parentheses, and variable x).
 * Also inserts implicit multiplication where needed (e.g., 3x -> 3*x, 2(x+1) -> 2*(x+1)).
 */
private fun tokenize(expr: String): List<String> {
    val tokens = mutableListOf<String>()
    var i = 0

    while (i < expr.length) {
        val c = expr[i]

        when {
            c.isDigit() || c == '.' -> {
                val start = i
                while (i < expr.length && (expr[i].isDigit() || expr[i] == '.')) i++
                tokens.add(expr.substring(start, i))
                continue
            }

            c.isLetter() -> {
                val start = i
                while (i < expr.length && expr[i].isLetter()) i++
                tokens.add(expr.substring(start, i).lowercase())
                continue
            }

            c in listOf('+', '-', '*', '/', '^', '(', ')') -> {
                tokens.add(c.toString())
                i++
            }

            else -> throw IllegalArgumentException("Invalid character: $c")
        }
    }

    // Insert implicit multiplication (e.g., 2pi, 3sin(x), x(x+1))
    val output = mutableListOf<String>()
    for (j in tokens.indices) {
        output.add(tokens[j])
        if (j < tokens.lastIndex) {
            val a = tokens[j]
            val b = tokens[j + 1]

            val aIsValue = a == "x" || a.toFloatOrNull() != null || a == ")" || a in constants
            val bStartsValue = b == "x" || b == "(" || b in functions || b in constants

            if (aIsValue && bStartsValue) {
                output.add("*")
            }
        }
    }

    return output
}

/**
 * Operator precedence table used by the Shunting Yard algorithm.
 */
private val precedence = mapOf(
    "+" to 1,
    "-" to 1,
    "*" to 2,
    "/" to 2,
    "^" to 3
)

/**
 * Converts a list of tokens from infix notation to Reverse Polish Notation (RPN)
 * using the Shunting Yard algorithm to correctly handle operator precedence
 * and parentheses.
 */
private fun toRPN(tokens: List<String>): List<String> {
    val output = mutableListOf<String>()
    val stack = ArrayDeque<String>()

    for (token in tokens) {
        when {
            token.toFloatOrNull() != null || token == "x" || token in constants -> output.add(token)

            token in functions -> stack.addLast(token)

            token in precedence -> {
                while (stack.isNotEmpty() && stack.last() in precedence &&
                    (((precedence[token]!! <= precedence[stack.last()]!!) && token != "^") ||
                            ((precedence[token]!! < precedence[stack.last()]!!) && token == "^"))
                ) {
                    output.add(stack.removeLast())
                }
                stack.addLast(token)
            }

            token == "(" -> stack.addLast(token)

            token == ")" -> {
                while (stack.isNotEmpty() && stack.last() != "(") {
                    output.add(stack.removeLast())
                }

                if (stack.isEmpty()) throw IllegalArgumentException("Mismatched parentheses")
                stack.removeLast()

                // If top is a function, pop it too
                if (stack.isNotEmpty() && stack.last() in functions) {
                    output.add(stack.removeLast())
                }
            }
        }
    }

    while (stack.isNotEmpty()) {
        val op = stack.removeLast()
        if (op == "(") throw IllegalArgumentException("Mismatched parentheses")
        output.add(op)
    }

    return output
}

/**
 * Evaluates a Reverse Polish Notation expression for a given x value.
 * Returns null if the expression is undefined at this x (e.g., division by zero).
 */
private fun evalRPN(rpn: List<String>, xValue: Float): Float? {
    val stack = ArrayDeque<Float>()

    for (token in rpn) {
        when {
            token.toFloatOrNull() != null -> stack.addLast(token.toFloat())

            token == "x" -> stack.addLast(xValue)

            token in constants -> stack.addLast(constants[token]!!)

            token in listOf("+", "-", "*", "/", "^") -> {
                if (stack.size < 2) return null
                val b = stack.removeLast()
                val a = stack.removeLast()

                val result = when (token) {
                    "+" -> a + b
                    "-" -> a - b
                    "*" -> a * b
                    "/" -> if (b == 0f) return null else a / b
                    "^" -> a.pow(b)
                    else -> return null
                }

                if (!result.isFinite()) return null
                stack.addLast(result)
            }

            token in functions -> {
                if (stack.isEmpty()) return null
                val v = stack.removeLast()

                val result = when (token) {
                    "sqrt" -> if (v < 0f) return null else sqrt(v)
                    "sin" -> sin(v)
                    "cos" -> cos(v)
                    "tan" -> tan(v)
                    "log" -> if (v <= 0f) return null else log10(v)
                    "ln" -> if (v <= 0f) return null else ln(v)
                    else -> return null
                }

                if (!result.isFinite()) return null
                stack.addLast(result)
            }

            else -> return null
        }
    }

    return if (stack.size == 1) stack.removeLast() else null
}
