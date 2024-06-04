package com.bmamaral.learning_logic.services.app.utils.truthtable

import com.bmamaral.learning_logic.services.app.InvalidSymbolException
import com.bmamaral.learning_logic.services.app.utils.FormulaConverter
import com.bmamaral.learning_logic.services.app.utils.truthtable.LogicalSymbolParser.getParsedSymbol
import java.util.*
import kotlin.math.pow

class LogicalValueGenerator(
    private val expressionToConvert: String,
    var variables: TreeMap<Char, Boolean>,
    private var subformulaVariables: TreeMap<Char, Boolean>
) {
    lateinit var binaryGrid: List<List<String>>

    constructor(expressionToConvert: String) : this(
        expressionToConvert, TreeMap<Char, Boolean>(), TreeMap<Char, Boolean>()
    )

    fun truthValuesGenerator(): BooleanArray {
        setVariables()
        //transform expression into rpqQueue and initialize variables map
        val rpnQueue = postfixConversionWithTokens(expressionToConvert)
        println("variables: ${variables.keys}")
        //initialize binary grid depending on the number of variables
        binaryGrid = fillGrid(variables.size)
        //initialize array of truth values
        val truthValues = BooleanArray(2.0.pow(variables.size.toDouble()).toInt())
        //loop through binary grid and calculate the truth values
        for (index in binaryGrid.indices) {
            for ((iterate, character) in variables.keys.withIndex()) {
                val value: Boolean = binaryGrid[index][iterate] != "F"
                variables.replace(character, value)
            }
            val queueClone: Queue<Token> = LinkedList(rpnQueue)
            truthValues[index] = evaluateExpression(queueClone, variables)
        }
        //return truth values of the expression to convert
        return truthValues
    }

    fun truthValuesGeneratorForSubFormula(subformula: String): BooleanArray {
        setVariables()
        setVariablesSubformula(subformula)
        val rpnQueueSubformula = postfixConversionWithTokens(subformula)

        binaryGrid = fillGrid(variables.size) // binary grid ready

        val truthValues = BooleanArray(2.0.pow(variables.size.toDouble()).toInt())
        for (index in binaryGrid.indices) {
            for (character in subformulaVariables.keys) {
                var iterate = 0 // HERE IS WHAT I NEED
                var it = 0
                for (char in variables.keys) {
                    if (character == char) {
                        iterate = it
                        break
                    } else {
                        it++
                    }
                }
                val value: Boolean = binaryGrid[index][iterate] != "F"
                subformulaVariables.replace(character, value) // update treemap with new values
            }
            val queueClone: Queue<Token> = LinkedList(rpnQueueSubformula)
            truthValues[index] = evaluateExpression(queueClone, subformulaVariables) // based on treemap values,
        }

        return truthValues
    }

    private fun fillGrid(size: Int): List<List<String>> {
        val listSize = 2.0.pow(size.toDouble()).toInt()
        val list = mutableListOf<List<String>>()
        for (i in 0 until listSize) {
            val row = convertBinary(i, size)
            val aux = row.substring(0, row.length - 1)
            val newList = aux.split(" ".toRegex()).toList()
            list.add(newList)
        }
        return list
    }

    private fun convertBinary(number: Int, size: Int): String {
        val convertedBinary: StringBuilder = StringBuilder()
        var stringAdditions = 0
        var num = number

        while (num > 0) {
            val rem: Int = num % 2
            num /= 2
            if (rem == 0) {
                convertedBinary.insert(0, "F ")
                ++stringAdditions
            } else {
                convertedBinary.insert(0, "T ")
                ++stringAdditions
            }
        }

        //if (stringAdditions < size) {
        while (stringAdditions < size) {
            convertedBinary.insert(0, "F ")
            ++stringAdditions
        }
        //}
        return convertedBinary.toString()
    }

    private fun postfixConversionWithTokens(original: String): Queue<Token> {
        val simpleExpression = FormulaConverter.simplifyFormula(original)
        val tokens = tokenize(simpleExpression)

        val rpnConverted: Queue<Token> = LinkedList() // variable output queue
        val operators: Stack<LogicalSymbol> = Stack() // operator stack
        val bracketValidation: Stack<Token> = Stack() // used to ensure consistent use of brackets in string

        try {
            for (token in tokens) {
                println("current token: ${token.type}")
                if (token.isLetter) { // if token is a LETTER
                    rpnConverted.add(token)
                } else if (token.type == ")") { // if token is a RIGHT PARENTHESIS
                    if (bracketValidation.isEmpty() || bracketValidation.pop().type != token.type) throw InvalidSymbolException(
                        "Brackets are malformed!"
                    )

                    while (!operators.isEmpty() && operators.peek().type != LogicalSymbolTypeEnum.BRACKET) {
                        rpnConverted.add(Token(operators.pop().symbol, false))
                    }
                    operators.pop()
                } else {
                    val parsedType: LogicalSymbolTypeEnum = getParsedSymbol(token.type)
                    val currentSymbol = LogicalSymbol(parsedType)
                    if (currentSymbol.type === LogicalSymbolTypeEnum.BRACKET) { // if token is LEFT PARENTHESIS
                        operators.push(currentSymbol)
                        if (token.type == "(") {
                            bracketValidation.push(
                                Token(
                                    ")", false
                                )
                            ) // add the required closing bracket to match opening one
                        }
                    } else { // if token is an OPERATOR
                        if (!operators.isEmpty() && operators.peek().precedence > currentSymbol.precedence) {
                            while (!operators.isEmpty() && operators.peek().precedence > currentSymbol.precedence && operators.peek().type !== LogicalSymbolTypeEnum.BRACKET) {
                                val poppedSymbol = operators.pop()
                                rpnConverted.add(Token(poppedSymbol.symbol, false))
                            }
                            operators.push(currentSymbol)
                        } else {
                            operators.push(currentSymbol)
                        }
                    }
                }
            }
            while (!operators.isEmpty()) {
                rpnConverted.add(Token(operators.pop().symbol, false)) // add remaining operators in stack
            }
            return rpnConverted

        } catch (e: EmptyStackException) {
            throw InvalidSymbolException("Invalid Expression Syntax")
        }
    }

    private fun evaluateExpression(rpnExpression: Queue<Token>, values: TreeMap<Char, Boolean>): Boolean {
        return try {
            val variables = Stack<Boolean>()
            while (!rpnExpression.isEmpty()) {
                val removedToken = rpnExpression.remove()
                if (removedToken.isLetter) {
                    variables.add(values[removedToken.letter])
                } else {
                    val parsedType: LogicalSymbolTypeEnum = getParsedSymbol(removedToken.type)
                    val currentSymbol = LogicalSymbol(parsedType)
                    if (currentSymbol.type !== LogicalSymbolTypeEnum.NEGATION) {
                        //assigning operandTwo first because for implication, order matters
                        val operandTwo = variables.pop()
                        val operandOne = variables.pop()
                        variables.add(currentSymbol.applyOperation(operandOne, operandTwo))
                    } else {
                        val operandOne = variables.pop()
                        variables.add(currentSymbol.applyOperation(operandOne, false))
                    }
                }
            }
            variables.pop()
        } catch (e: EmptyStackException) {
            throw InvalidSymbolException("Invalid Expression Syntax")
        }
    }

    private fun tokenize(expression: String): List<Token> {
        val list = mutableListOf<Token>()

        var i = 0
        while (i < expression.length) {
            when {
                expression[i] == '&' -> {
                    list.add(Token("&", false))
                    i++
                }
                expression[i] == '|' -> {
                    list.add(Token("|", false))
                    i++
                }
                expression[i] == '~' -> {
                    list.add(Token("~", false))
                    i++
                }
                expression[i] == '(' -> {
                    list.add(Token("(", false))
                    i++
                }
                expression[i] == ')' -> {
                    list.add(Token(")", false))
                    i++
                }
                expression[i] == '=' -> {
                    list.add(Token("=>", false))
                    i += 2
                }
                expression[i] == '<' -> {
                    list.add(Token("<=>", false))
                    i += 3
                }
                else -> {
                    list.add(Token(expression[i].toString(), true))
                    i++
                }
            }

        }
        return list
    }

    private fun setVariables() {
        for (i in expressionToConvert.indices) {
            val currentChar = expressionToConvert[i]
            if (Character.isLetter(expressionToConvert[i])) {
                variables[currentChar] = false
            }
        }
    }

    private fun setVariablesSubformula(subformula: String) {
        for (i in subformula.indices) {
            val currentChar = subformula[i]
            if (Character.isLetter(subformula[i])) {
                subformulaVariables[currentChar] = false
            }
        }
    }

}