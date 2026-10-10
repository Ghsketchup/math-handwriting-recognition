package com.mathapp.math

class LaTeXGenerator {
    fun generate(equation: String): String {
        return convertToLatex(equation)
    }

    private fun convertToLatex(equation: String): String {
        var latex = equation
        
        // Replace common operators
        latex = latex.replace("^", "^")
        latex = latex.replace("sqrt", "\\sqrt")
        latex = latex.replace("pi", "\\pi")
        latex = latex.replace("sin", "\\sin")
        latex = latex.replace("cos", "\\cos")
        latex = latex.replace("tan", "\\tan")
        latex = latex.replace("log", "\\log")
        latex = latex.replace("ln", "\\ln")
        latex = latex.replace("x", "x")
        latex = latex.replace("y", "y")
        
        return latex
    }
}
