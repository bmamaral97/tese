package com.bmamaral.learning_logic.services.app.utils.truthtable

enum class LogicalSymbolTypeEnum {
    CONJUNCTION, DISJUNCTION, IMPLICATION, NEGATION, BICONDITIONAL, BRACKET
}

class LogicalSymbol(val type: LogicalSymbolTypeEnum, var precedence: Int, var symbol: String) {

    constructor(type: LogicalSymbolTypeEnum) : this(type, -1, "")

    init {
        when {
            type === LogicalSymbolTypeEnum.NEGATION -> {
                precedence = 5
                symbol = "~"
            }
            type === LogicalSymbolTypeEnum.CONJUNCTION -> {
                precedence = 4
                symbol = "&"
            }
            type === LogicalSymbolTypeEnum.DISJUNCTION -> {
                precedence = 3
                symbol = "|"
            }
            type === LogicalSymbolTypeEnum.IMPLICATION -> {
                precedence = 2
                symbol = "=>"
            }
            type === LogicalSymbolTypeEnum.BICONDITIONAL -> {
                precedence = 1
                symbol = "<=>"
            }
            type === LogicalSymbolTypeEnum.BRACKET -> {
                precedence = 6
                symbol = "("
            }
        }
    }

    fun applyOperation(operand1: Boolean, operand2: Boolean): Boolean {
        when {
            type === LogicalSymbolTypeEnum.NEGATION -> {
                return !operand1
            }
            type === LogicalSymbolTypeEnum.CONJUNCTION -> {
                return operand1 and operand2
            }
            type === LogicalSymbolTypeEnum.DISJUNCTION -> {
                return operand1 or operand2
            }
            type === LogicalSymbolTypeEnum.IMPLICATION -> {
                return operand2 or !operand1
            }
            type === LogicalSymbolTypeEnum.BICONDITIONAL -> {
                return operand1 == operand2
            }
            else -> return false
        }
    }
}

class Token(val type: String, val isLetter: Boolean, var letter: Char) {
    constructor(type: String, isLetter: Boolean) : this(type, isLetter, type[0])
}