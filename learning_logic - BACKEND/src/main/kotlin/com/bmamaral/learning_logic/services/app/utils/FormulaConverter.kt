package com.bmamaral.learning_logic.services.app.utils

object FormulaConverter {
    fun simplifyFormula(formula: String): String {
        val sb = StringBuilder()
        for (i in formula.indices) {
            when {
                formula[i] == '(' -> {
                    sb.append("(")
                }
                formula[i] == ')' -> {
                    sb.append(")")
                }
                formula[i] == '∧' -> {
                    sb.append("&")
                }
                formula[i] == '∨' -> {
                    sb.append("|")
                }
                formula[i] == '¬' -> {
                    sb.append("~")
                }
                formula[i] == '⇒' -> {
                    sb.append("=>")
                }
                formula[i] == '⇔' -> {
                    sb.append("<=>")
                }
                else -> {
                    sb.append(formula[i])
                }
            }
        }
        return sb.toString()
    }

    fun prettifyFormula(formula: String): String {
        val sb = StringBuilder()
        var i = 0
        while (i < formula.length) {
            when {
                formula[i] == ' ' -> {
                    i++
                }
                formula[i] == '(' -> {
                    sb.append("(")
                    i++
                }
                formula[i] == ')' -> {
                    sb.append(")")
                    i++
                }
                formula[i] == '&' -> {
                    sb.append("∧")
                    i++
                }
                formula[i] == '|' -> {
                    sb.append("∨")
                    i++
                }
                formula[i] == '~' -> {
                    sb.append("¬")
                    i++
                }
                formula[i] == '=' -> {
                    sb.append("⇒")
                    i += 2
                }
                formula[i] == '<' -> {
                    sb.append("⇔")
                    i += 3
                }
                else -> {
                    sb.append(formula[i])
                    i++
                }
            }
        }
        return sb.toString()
    }

}