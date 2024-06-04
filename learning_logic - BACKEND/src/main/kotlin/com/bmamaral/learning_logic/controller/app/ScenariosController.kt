package com.bmamaral.learning_logic.controller.app

import com.bmamaral.learning_logic.controller.app.api.ScenariosAPI
import com.bmamaral.learning_logic.model.CodeAndInstructorDTO
import com.bmamaral.learning_logic.model.ExerciseDAO
import com.bmamaral.learning_logic.model.ScenarioDTO
import com.bmamaral.learning_logic.model.ScenarioListDTO
import com.bmamaral.learning_logic.services.app.ScenariosService
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ScenariosController(val scenarios: ScenariosService) : ScenariosAPI {

    override fun getAllScenarios(): Collection<ScenarioListDTO> =
        handle4xx { scenarios.getScenarios().map { ScenarioListDTO(it) } }

    override fun getScenarioById(scenarioId: Long): ScenarioDTO =
        handle4xx { ScenarioDTO(scenarios.getScenarioById(scenarioId)) }

    override fun getScenarioExercises(scenarioId: Long): Collection<ExerciseDAO> =
        handle4xx { scenarios.getScenarioExercises(scenarioId) }

    override fun addScenario(@RequestBody dto: CodeAndInstructorDTO) =
        handle4xx { scenarios.addScenarioCode(dto.code, dto.instructor) }

    override fun deleteScenario(scenarioId: Long) =
        handle4xx { scenarios.deleteScenario(scenarioId) }

}