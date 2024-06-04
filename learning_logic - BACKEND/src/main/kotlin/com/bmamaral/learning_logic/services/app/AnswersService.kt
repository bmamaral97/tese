package com.bmamaral.learning_logic.services.app

import com.bmamaral.learning_logic.model.*
import com.bmamaral.learning_logic.services.app.utils.FormulaConverter.simplifyFormula
import com.bmamaral.learning_logic.services.app.utils.normalform.NFChecker
import com.bmamaral.learning_logic.services.app.utils.parser.PLParser
import com.bmamaral.learning_logic.services.app.utils.truthtable.LogicalValueGenerator
import org.antlr.v4.runtime.misc.ParseCancellationException
import org.springframework.stereotype.Service

@Service
class AnswersService(val exercises: ExercisesService, val users: UsersService) {

    // Grade functions for PL exercises

    fun gradeExercise(exId: Long, answer: AnswerDTO): GradeResponseDTO {
        try {
            val exercise = exercises.getExerciseById(exId)
            return when (exercise.type) {
                "PL1" -> {
                    val user = users.getOneUserByUsername(answer.student)
                    val gradeResponse = gradePL1(exercise as PL1DAO, answer)
                    if (gradeResponse.grade == 100) {
                        users.addCompletedExercise(user, exercise)
                    }
                    gradeResponse
                }
                "PL2" -> {
                    val user = users.getOneUserByUsername(answer.student)
                    val gradeResponse = gradePL2(exercise as PL2DAO, answer)
                    if (gradeResponse.grade == 100) {
                        users.addCompletedExercise(user, exercise)
                    }
                    gradeResponse
                }
                "PL3" -> {
                    val user = users.getOneUserByUsername(answer.student)
                    val gradeResponse = gradePL3(exercise as PL3DAO, answer)
                    if (gradeResponse.grade == 100) {
                        users.addCompletedExercise(user, exercise)
                    }
                    gradeResponse
                }
                //"PL4" -> gradePL4(exercise as PL4DAO, answer)
                else -> throw UnknownExerciseTypeException("Unknown exercise type: ${exercise.type}")
            }
        } catch (nfe: NotFoundException) {
            nfe.printStackTrace()
            throw NotFoundException("Username not found: ${answer.student}")
        } catch (pce: ParseCancellationException) {
            pce.printStackTrace()
            return GradeResponseDTO(0, listOf(true), "The submitted formula is invalid!")
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw Error("Something went wrong.")
        }
    }

    private fun gradePL1(exercise: PL1DAO, dto: AnswerDTO): GradeResponseDTO {
        val solution = exercise.formula
        val submittedExpression = dto.answer as String
        val correct = checkEquivalence(simplifyFormula(submittedExpression), solution)

        val response: GradeResponseDTO = if (correct) {
            GradeResponseDTO(100, listOf(false), "The answer is correct!")
        } else {
            GradeResponseDTO(0, listOf(true), "The answer submitted is not correct!")
        }

        return response
    }

    @Suppress("UNCHECKED_CAST")
    private fun gradePL2(exercise: PL2DAO, dto: AnswerDTO): GradeResponseDTO {
        val truthTable = createTruthTable(exercise.id)
        val correctValues: Map<String, List<Boolean>> = truthTable.values

        val values: List<Any> = dto.answer as List<Any>
        val submittedValues: MutableMap<String, List<Boolean>> = mutableMapOf()

        for (p in values) {
            val map: LinkedHashMap<*, *> = p as LinkedHashMap<*, *>
            val key = map["first"] as String
            val value = map["second"] as List<Boolean>
            val pair = Pair(key, value)
            submittedValues[pair.first] = pair.second
        }

        if (submittedValues.isEmpty())
            throw Error("No values submitted")

        // Check if subformulas submitted are correct , if the complexity is ~1
        // TODO() = remove whitespaces from subformulas to prevent errors (trim)
        if (!correctValues.keys.containsAll(submittedValues.keys)) {
            return GradeResponseDTO(0, listOf(false), "Submitted subformulas aren't correct!")
        }

        val size = truthTable.values.keys.size
        val errors = BooleanArray(size)

        submittedValues.asIterable().forEachIndexed { index, entry ->
            val correct = correctValues[entry.key] == entry.value
            errors[index] = !correct
        }

        val numErrors = errors.count { it }

        val response = if (numErrors == 0) {
            GradeResponseDTO(100, errors.toList(), "Exercise is correct!")
        } else {
            val totalNum = truthTable.values.keys.size
            val grade = ((totalNum - numErrors) / totalNum) * 100
            GradeResponseDTO(grade, errors.toList(), "Exercise is incorrect!")
        }

        return response
    }

    private fun gradePL3(exercise: PL3DAO, dto: AnswerDTO): GradeResponseDTO {
        val expression = dto.answer as String
        val normalForm = exercise.normalForm
        val correct = checkNormalForm(expression, normalForm)

        return if (correct) {
            GradeResponseDTO(100, listOf(false), "Exercise is correct!")
        } else {
            GradeResponseDTO(0, listOf(true), "Exercise is incorrect!")
        }
    }

    //private fun gradePL4(exercise: PL4DAO, dto: AnswerDTO): GradeResponseDTO = TODO()


    // Function to create truth-table from an expression

    fun createTruthTable(exId: Long): TruthTableDTO {
        val exercise = exercises.getExerciseById(exId)

        val expression = exercise.formula
        val generator = LogicalValueGenerator(expression)
        val expTruthvalues = generator.truthValuesGenerator().toList()
        val variables: List<Char> = generator.variables.keys.toList()
        val subformulas: List<String> = PLParser.getSubFormulas(expression)
        val grid: List<List<String>> = generator.binaryGrid

        val values: MutableMap<String, List<Boolean>> = mutableMapOf()
        for (formula in subformulas) {
            val value = generator.truthValuesGeneratorForSubFormula(formula)
            values[formula] = value.toList()
        }
        values[expression] = expTruthvalues

        return TruthTableDTO(variables, grid, subformulas, values)
    }


    // Logic and helper functions

    private fun checkEquivalence(expression1: String, expression2: String): Boolean {
        checkExpression(expression1)
        checkExpression(expression2)

        val generator1 = LogicalValueGenerator(expression1)
        val truthValues1: BooleanArray = generator1.truthValuesGenerator()

        val generator2 = LogicalValueGenerator(expression2)
        val truthValues2: BooleanArray = generator2.truthValuesGenerator()

        return (truthValues1.contentEquals(truthValues2) && generator1.variables == generator2.variables)
    }

    private fun checkNormalForm(expression: String, normalForm: String): Boolean {
        return when (normalForm) {
            "CNF" -> NFChecker.checkCNF(expression)
            "DNF" -> NFChecker.checkDNF(expression)
            "NNF" -> NFChecker.checkNNF(expression)
            else -> {
                throw PreconditionFailedException("No normal form of that type!: $normalForm")
            }
        }
    }

    private fun checkExpression(expression: String) {
        if (!PLParser.parse(expression)) throw ParseCancellationException("The expression submitted is invalid!: $expression")
    }

    fun validateExpression(exId: Long, expression: String): Boolean {
        val exercise = exercises.getExerciseById(exId)
        return checkEquivalence(exercise.formula, expression)
    }

}