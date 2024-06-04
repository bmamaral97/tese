package com.bmamaral.learning_logic.services.app

import com.bmamaral.learning_logic.model.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ScenariosService(
    private val scenariosRepository: ScenarioRepository,
    private val usersRepository: UsersRepository
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun getScenarios(): List<ScenarioDAO> = scenariosRepository.findAll().toList()

    fun getScenarioById(scenarioId: Long): ScenarioDAO = scenariosRepository.findById(scenarioId).orElseThrow {
        NotFoundException("There is no scenario with the given id!: $scenarioId")
    }

    fun getScenarioExercises(scenarioId: Long): List<ExerciseDAO> {
        val scenario = getScenarioById(scenarioId)
        val exercises = scenario.exercises
        return exercises.toList()
    }

    @Transactional
    fun addScenarioCode(code: String, instructorName: String) {
        //scenario code: scenarioName/psymbols/phrases[]
        val split = code.split("/")
        val scenarioName: String = split[0]
        val psymbols = split[1].split(",") // TODO: CHECK IF ALL CHARS ARE BETWEEN a..z and A..Z
        val phrases: List<String> = split[2].split(",")
        val creator = getUser(instructorName)
        val map = createMap(psymbols, phrases)
        val scenario = ScenarioDAO(scenarioName, creator, map)
        addScenario(scenario)
    }

    private fun createMap(psymbols: List<String>, phrases: List<String>): Map<String, String> {
        val mutableMap: MutableMap<String, String> = mutableMapOf()
        if (psymbols.size != phrases.size) {
            throw PreconditionFailedException("Must have the same number of psymbols and phrases!")
        }
        for (i in psymbols.indices) {
            val currentSymbol = psymbols[i].trimStart()
            if (!Character.isLetter(currentSymbol[0])) {
                throw PreconditionFailedException("All psymbols must be letters!")
            }
            mutableMap[currentSymbol] = phrases[i].trimStart()
        }
        return mutableMap.toMap()
    }

    private fun addScenario(scenario: ScenarioDAO) {
        // CHECK IF SCENARIO ID IS 0 AT INSERTION
        if (scenario.id != 0L) {
            throw PreconditionFailedException("Scenario Id must be 0 at insertion.")
        }
        // CHECK IF ALREADY EXISTS A SCENARIO WITH GIVEN NAME
        else if (scenariosRepository.findByName(scenario.name).isPresent) {
            throw PreconditionFailedException("There already exists a scenario with that name in the system!: ${scenario.name}")
        }
        // ADD SCENARIO TO DB
        else {
            val newScenario = scenariosRepository.save(scenario)
            val user = newScenario.creator
            if (user is InstructorDAO) {
                user.addCreatedScenario(newScenario)
                usersRepository.save(user)
            }
            logger.info("Added new scenario: ${newScenario.id}")
        }
    }

    fun deleteScenario(scenarioId: Long) {
        val scenario = getScenarioById(scenarioId)
        scenariosRepository.delete(scenario)
        logger.info("Scenario with Id ${scenario.id} was deleted!")
    }

    private fun getUser(username: String): UserDAO {
        return usersRepository.findByUsername(username)
            .orElseThrow { NotFoundException("There is no user with that username!: $username") }
    }

    fun getScenarioOptional(scenarioName: String): Optional<ScenarioDAO> = scenariosRepository.findByName(scenarioName)

}