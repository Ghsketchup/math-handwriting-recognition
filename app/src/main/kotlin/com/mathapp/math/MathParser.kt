package com.mathapp.math

import kotlin.math.*

class MathParser {
    fun parse(expression: String): Double {
        return evaluateExpression(expression.replace(" ", ""))
    }

    private fun evaluateExpression(expr: String): Double {
        return parseAddition(expr, 0).first
    }

    private fun parseAddition(expr: String, startIndex: Int): Pair<Double, Int> {
        var result = parseMultiplication(expr, startIndex)
        var index = result.second

        while (index < expr.length) {
            when (expr[index]) {
                '+' -> {
                    val next = parseMultiplication(expr, index + 1)
                    result = Pair(result.first + next.first, next.second)
                    index = next.second
                }
                '-' -> {
                    val next = parseMultiplication(expr, index + 1)
                    result = Pair(result.first - next.first, next.second)
                    index = next.second
                }
                else -> break
            }
        }

        return result
    }

    private fun parseMultiplication(expr: String, startIndex: Int): Pair<Double, Int> {
        var result = parsePower(expr, startIndex)
        var index = result.second

        while (index < expr.length) {
            when (expr[index]) {
                '*' -> {
                    val next = parsePower(expr, index + 1)
                    result = Pair(result.first * next.first, next.second)
                    index = next.second
                }
                '/', '÷' -> {
                    val next = parsePower(expr, index + 1)
                    result = Pair(result.first / next.first, next.second)
                    index = next.second
                }
                else -> break
            }
        }

        return result
    }

    private fun parsePower(expr: String, startIndex: Int): Pair<Double, Int> {
        var result = parseFactor(expr, startIndex)
        var index = result.second

        if (index < expr.length && expr[index] == '^') {
            val next = parseFactor(expr, index + 1)
            result = Pair(result.first.pow(next.first), next.second)
            index = next.second
        }

        return result
    }

    private fun parseFactor(expr: String, startIndex: Int): Pair<Double, Int> {
        if (startIndex >= expr.length) return Pair(0.0, startIndex)

        // Handle parentheses
        if (expr[startIndex] == '(') {
            val closeIndex = findMatchingParen(expr, startIndex)
            val result = parseAddition(expr, startIndex + 1)
            return Pair(result.first, closeIndex + 1)
        }

        // Handle functions
        val funcMatch = matchFunction(expr, startIndex)
        if (funcMatch != null) {
            val (func, endIndex) = funcMatch
            val (value, nextIndex) = parseFactor(expr, endIndex)
            val result = when (func) {
                "sin" -> sin(value)
                "cos" -> cos(value)
                "tan" -> tan(value)
                "sqrt" -> sqrt(value)
                "abs" -> abs(value)
                "log" -> log10(value)
                "ln" -> ln(value)
                "exp" -> exp(value)
                else -> value
            }
            return Pair(result, nextIndex)
        }

        // Handle numbers and variables
        val numberMatch = matchNumber(expr, startIndex)
        if (numberMatch != null) {
            val (value, endIndex) = numberMatch
            return Pair(value, endIndex)
        }

        return Pair(0.0, startIndex)
    }

    private fun matchFunction(expr: String, startIndex: Int): Pair<String, Int>? {
        val functions = listOf("sin", "cos", "tan", "sqrt", "abs", "log", "ln", "exp")
        for (func in functions) {
            if (expr.startsWith(func, startIndex)) {
                return Pair(func, startIndex + func.length)
            }
        }
        return null
    }

    private fun matchNumber(expr: String, startIndex: Int): Pair<Double, Int>? {
        var index = startIndex
        val sb = StringBuilder()

        // Handle negative numbers
        if (expr[index] == '-') {
            sb.append('-')
            index++
        }

        // Handle variables and constants
        if (index < expr.length && expr[index].isLetter()) {
            val variable = expr[index].toString()
            return when (variable) {
                "x" -> Pair(1.0, index + 1)
                "pi" -> Pair(PI, index + 2)
                "e" -> Pair(E, index + 1)
                else -> null
            }
        }

        // Parse numeric value
        while (index < expr.length && (expr[index].isDigit() || expr[index] == '.')) {
            sb.append(expr[index])
            index++
        }

        return if (sb.isNotEmpty()) {
            try {
                Pair(sb.toString().toDouble(), index)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    private fun findMatchingParen(expr: String, openIndex: Int): Int {
        var count = 1
        var index = openIndex + 1
        while (index < expr.length && count > 0) {
            if (expr[index] == '(') count++
            else if (expr[index] == ')') count--
            index++
        }
        return index - 1
    }
}
