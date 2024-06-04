package com.bmamaral.learning_logic.model

import java.util.*

data class ScenarioDTO(
    val id: Long, val name: String, val creator: String, val map: Map<String, String>, var exercises: List<PL1DTO>
) {
    constructor(scenarioDAO: ScenarioDAO) : this(scenarioDAO.id,
        scenarioDAO.name,
        scenarioDAO.creator.username,
        scenarioDAO.map,
        scenarioDAO.exercises.map { PL1DTO(it) })
}

data class ScenarioListDTO(
    val id: Long, val name: String, val creator: String, val map: Map<String, String>, var exercises: Map<Long, String>
) {
    constructor(scenarioDAO: ScenarioDAO) : this(scenarioDAO.id,
        scenarioDAO.name,
        scenarioDAO.creator.username,
        scenarioDAO.map,
        buildMap<Long, String> { scenarioDAO.exercises.map { put(it.id, it.formula) } })
}

open class ExerciseDTO(
    open val id: Long, open val type: String, open val creator: String, open val formula: String
) {
    constructor(exercise: ExerciseDAO) : this(
        exercise.id,
        exercise.type,
        exercise.creator.username,
        exercise.formula,
    )
}

data class PL1DTO(
    override val id: Long,
    override val type: String,
    override val creator: String,
    override val formula: String,
    val phrase: String,
    val scenarioId: Long
) : ExerciseDTO(id, type, creator, formula) {
    constructor(exercise: PL1DAO) : this(
        exercise.id,
        exercise.type,
        exercise.creator.username,
        exercise.formula,
        exercise.phrase,
        exercise.scenario.id,
    )
}

data class PL2DTO(
    override val id: Long,
    override val type: String,
    override val creator: String,
    override val formula: String,
    val complexity: Int
) : ExerciseDTO(id, type, creator, formula) {
    constructor(exercise: PL2DAO) : this(
        exercise.id, exercise.type, exercise.creator.username, exercise.formula, exercise.complexity
    )
}

data class PL3DTO(
    override val id: Long,
    override val type: String,
    override val creator: String,
    override val formula: String,
    val normalForm: String,
    val complexity: Int,
    val endFormula: String,
) : ExerciseDTO(id, type, creator, formula) {
    constructor(exercise: PL3DAO) : this(
        exercise.id,
        exercise.type,
        exercise.creator.username,
        exercise.formula,
        exercise.normalForm,
        exercise.complexity,
        exercise.endFormula
    )
}

data class PostUserDTO(val userPassword: UserPasswordDTO, val userType: String)

data class UserPasswordDTO(val username: String, var password: String)

open class UserDTO(open val id: Long, open val username: String, open val authorities: Set<String>) {
    constructor(user: UserDAO) : this(
        user.id,
        user.username,
        user.authorities
    )
}

data class InstructorDTO(
    override val id: Long,
    override val username: String,
    override val authorities: Set<String>,
    val createdExercises: List<Long>,
    val createdScenarios: List<Long>
) : UserDTO(id, username, authorities) {
    constructor(instructor: InstructorDAO) : this(
        instructor.id,
        instructor.username,
        instructor.authorities,
        instructor.createdExercises.map { it.id },
        instructor.createdScenarios.map { it.id }
    )
}

//data class FileUploadDTO(val file: MultipartFile, val instructor: String)

data class CodeAndInstructorDTO(val code: String, val instructor: String)

// data class DoubleExpressionDTO(val expression1: String, val expression2: String)

data class ExpressionDTO(val expression: String)

// data class ExpressionAndNormalFormDTO(val expression: String, val normalForm: String)

data class TruthTableDTO(
    val variables: List<Char>,
    val grid: List<List<String>>,
    val subformulas: List<String>,
    val values: Map<String, List<Boolean>>
)

data class GradeResponseDTO(
    var grade: Int, var errors: List<Boolean>, var feedback: String
)

data class AnswerDTO(
    val student: String,
    val answer: Any
)

data class Pair(
    val first: String,
    val second: List<Boolean>
)

data class SubmissionDTO(
    val id: Long,
    val student: String,
    val exercise: Long,
    val grade: Int,
    val submittedAt: Date
) {
    constructor(s: SubmissionDAO) : this(s.id, s.user.username, s.exercise.id, s.grade, s.submittedAt)
}

