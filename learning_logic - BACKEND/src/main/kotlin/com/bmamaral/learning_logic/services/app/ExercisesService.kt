package com.bmamaral.learning_logic.services.app

import com.bmamaral.learning_logic.model.*
import com.bmamaral.learning_logic.services.app.utils.parser.PLParser
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ExercisesService(
    private val scenariosRepository: ScenarioRepository,
    private val exercisesRepository: ExerciseRepository,
    private val usersRepository: UsersRepository
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Transactional
    fun addExerciseByCode(exType: String, code: String, instructor: String) {
        when (exType) {
            "PL1" -> addPL1Code(code, instructor)
            "PL2" -> addPL2Code(code, instructor)
            "PL3" -> addPL3Code(code, instructor)
            //"PL4" -> addPL4ByCode(code, instructor)
            else -> {
                throw UnknownExerciseTypeException("Unknown exercise type: $exType")
            }
        }
    }

    private fun addPL1Code(code: String, instructorName: String) {
        //code: scenarioName/psymbols/phrases (phrases and symbols separated by comma)
        val split = code.split("/")
        val scenarioName = split[0]
        val phrase = split[1]
        val formula = split[2]
        val creator = getUser(instructorName)
        val scenario = getScenario(scenarioName)
        addPL1(PL1DAO(creator, scenario, formula, phrase))
    }

    private fun addPL1(exercise: PL1DAO) {
        // CHECK IF ID IS 0 WHEN INSERTED
        if (exercise.id != 0L) {
            throw PreconditionFailedException("Id must be 0 in insertion")
        }
        // CHECK IF SUBMITTED FORMULA IS VALID
        else if (!PLParser.parse(exercise.formula)) {
            throw PreconditionFailedException("Invalid formula")
        }
        // CHECK IF INSTRUCTOR ID IS VALID
        else if (!usersRepository.findByUsername(exercise.creator.username).isPresent) {
            throw NotFoundException("No instructor with the given id")
        }
        // CHECK IF SCENARIO ID IS VALID
        else if (!scenariosRepository.findByName(exercise.scenario.name).isPresent) {
            throw NotFoundException("No scenario with that id found")
            // ADD EXERCISE TO CORRESPONDING SCENARIO AND SAVE TO DB
        } else {
            val ex = exercisesRepository.save(exercise)
            val scenario = scenariosRepository.findWithExercises(ex.scenario.id)
            if (scenario.isPresent) {
                val sc = scenario.get()
                val exercises = sc.exercises
                exercises.add(ex)
                sc.updateExercises(exercises)
                scenariosRepository.save(sc)

                val user = ex.creator
                if (user is InstructorDAO) {
                    user.addCreatedExercise(ex)
                    usersRepository.save(user)
                }
            }

            logger.info("Added PL1 exercise: ${ex.id}")
        }
    }

    private fun addPL2Code(code: String, instructorName: String) {
        val split = code.split("/")
        val formula = split[0]
        val complexity = split[1].toInt()
        val creator = getUser(instructorName)
        val exercise = PL2DAO(creator, formula, complexity)
        addPL2(exercise)
    }

    private fun addPL2(exercise: PL2DAO) {
        // CHECK IF EXERCISE ID IS 0 AT INSERTIONS
        if (exercise.id != 0L) {
            throw PreconditionFailedException("Id must be 0 at insertion")
        }
        // CHECK IF SUBMITTED FORMULA IS VALID
        else if (!PLParser.parse(exercise.formula)) {
            throw PreconditionFailedException("Invalid formula")
        }
        // CHECK IF COMPLEXITY IS EITHER 0 OR 1
        else if (exercise.complexity != 0 && exercise.complexity != 1) throw PreconditionFailedException("Complexity must be either 0 or 1 at insertion")
        // ADD EXERCISE TO DB
        else {
            val ex = exercisesRepository.save(exercise) as ExerciseDAO
            logger.info("Added PL2 exercise: ${ex.id}")
        }
    }

    private fun addPL3Code(code: String, instructorName: String) {
        val split = code.split("/")
        val formula = split[0]
        val normalForm = split[1]
        val complexity = split[2].toInt()
        val creator = getUser(instructorName)

        val exercise: PL3DAO = if (complexity == 0) {
            val goalFormula = split[3]
            PL3DAO(creator, formula, normalForm, goalFormula)
        } else {
            PL3DAO(creator, formula, normalForm)
        }

        addPL3(exercise)
    }

    private fun addPL3(exercise: PL3DAO) {
        // CHECK IF EXERCISE ID IS 0 AT INSERTION
        if (exercise.id != 0L) {
            throw PreconditionFailedException("Id must be 0 in insertion")
        }
        // CHECK IF EXERCISE COMPLEXITY IS EITHER 0 OR 1
        else if (exercise.complexity != 0 && exercise.complexity != 1) {
            throw PreconditionFailedException("Complexity must be either 0 or 1 at insertion")
        }
        // CHECK IF EXERCISE FORMULA IS VALID
        else if (!PLParser.parse(exercise.formula)) {
            throw PreconditionFailedException("Invalid formula")
        }
        // CHECK IF EXERCISE HAS END FORMULA AND IF IT IS VALID
        else if (exercise.complexity == 1 && !PLParser.parse(exercise.endFormula)) {
            throw PreconditionFailedException("Invalid goal formula")
        }
        // ADD EXERCISE TO DB
        else {
            val ex = exercisesRepository.save(exercise)
            logger.info("Added PL3 exercise: ${ex.id}")
        }
    }

    fun getExercisesByType(exType: String): List<ExerciseDAO> {
        validateExerciseType(exType)
        return exercisesRepository.findAllByType(exType).get().toList()
    }

    fun getExerciseById(exerciseId: Long): ExerciseDAO = exercisesRepository.findById(exerciseId).orElseThrow {
        NotFoundException("There is no exercise with the given id in the system!: $exerciseId")
    }

    fun getExerciseOptional(exerciseId: Long): Optional<ExerciseDAO> =
        exercisesRepository.findById(exerciseId)

    fun deleteExercise(exerciseId: Long) {
        val exercise = getExerciseById(exerciseId)
        exercisesRepository.delete(exercise)
        logger.info("Successfully deleted the exercise: ${exercise.id}")
    }

    private fun getUser(username: String): UserDAO {
        return usersRepository.findByUsername(username)
            .orElseThrow { NotFoundException("There is no user with that username!: $username") }
    }

    private fun getScenario(scenarioName: String): ScenarioDAO =
        scenariosRepository.findByName(scenarioName).orElseThrow {
            NotFoundException("There is no scenario with the given name!: $scenarioName")
        }

    private fun validateExerciseType(exType: String) {
        val validTypes = listOf("PL1", "PL2", "PL3", "PL4")
        if (!validTypes.contains(exType))
            throw UnknownExerciseTypeException("Unknown exercise type: $exType")
    }

}