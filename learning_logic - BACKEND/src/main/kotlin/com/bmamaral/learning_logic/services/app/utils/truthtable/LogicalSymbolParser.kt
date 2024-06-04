package com.bmamaral.learning_logic.services.app.utils.truthtable

import com.bmamaral.learning_logic.services.app.InvalidSymbolException

object LogicalSymbolParser {
    fun getParsedSymbol(symbol: String): LogicalSymbolTypeEnum {
        return when (symbol) {
            "=>" -> {
                LogicalSymbolTypeEnum.IMPLICATION
            }
            "<=>" -> {
                LogicalSymbolTypeEnum.BICONDITIONAL
            }
            "~" -> {
                LogicalSymbolTypeEnum.NEGATION
            }
            "&" -> {
                LogicalSymbolTypeEnum.CONJUNCTION
            }
            "|" -> {
                LogicalSymbolTypeEnum.DISJUNCTION
            }
            "(" -> {
                LogicalSymbolTypeEnum.BRACKET
            }
            else -> {
                throw InvalidSymbolException("At least one of the input symbols is invalid.")
            }
        }
    }
}