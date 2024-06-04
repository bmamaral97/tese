package com.bmamaral.learning_logic.controller.app

import com.bmamaral.learning_logic.controller.app.api.ExercisesAPI
import com.bmamaral.learning_logic.model.*
import com.bmamaral.learning_logic.services.app.AnswersService
import com.bmamaral.learning_logic.services.app.ExercisesService
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ExercisesController(val exercises: ExercisesService, val answers: AnswersService) : ExercisesAPI {

    override fun getExercisesByType(exType: String): Collection<ExerciseDTO> = handle4xx { getDtoList(exType) }

    private fun getDtoList(exType: String): Collection<ExerciseDTO> {
        val exercises = exercises.getExercisesByType(exType)
        return when (exType) {
            "PL1" -> exercises.map { PL1DTO(it as PL1DAO) }
            "PL2" -> exercises.map { PL2DTO(it as PL2DAO) }
            "PL3" -> exercises.map { PL3DTO(it as PL3DAO) }
            else -> {
                throw java.lang.Exception("Error")
            }
        }
    }

    override fun getOneExerciseById(exId: Long): ExerciseDTO = handle4xx { getDto(exId) }

    private fun getDto(exId: Long): ExerciseDTO {
        val exercise = exercises.getExerciseById(exId)
        return when (exercise.type) {
            "PL1" -> PL1DTO(exercise as PL1DAO)
            "PL2" -> PL2DTO(exercise as PL2DAO)
            "PL3" -> PL3DTO(exercise as PL3DAO)
            else -> {
                throw java.lang.Exception("Error")
            }
        }
    }

    override fun postExercise(exType: String, @RequestBody dto: CodeAndInstructorDTO) =
        handle4xx { exercises.addExerciseByCode(exType, dto.code, dto.instructor) }

    override fun deleteExercise(exId: Long) = handle4xx { exercises.deleteExercise(exId) }

    override fun gradeExercise(exId: Long, @RequestBody dto: AnswerDTO): GradeResponseDTO =
        handle4xx { answers.gradeExercise(exId, dto) }

    override fun createTruthTable(exId: Long): TruthTableDTO = handle4xx {
        answers.createTruthTable(exId)
    }

    override fun validateExpression(exId: Long, dto: ExpressionDTO): Boolean =
        handle4xx { answers.validateExpression(exId, dto.expression) }
}