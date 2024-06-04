package com.bmamaral.learning_logic.controller.app.api

import com.bmamaral.learning_logic.model.CodeAndInstructorDTO
import com.bmamaral.learning_logic.model.ExerciseDAO
import com.bmamaral.learning_logic.model.ScenarioDTO
import com.bmamaral.learning_logic.model.ScenarioListDTO
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@Api(
    value = "LEARNING LOGIC - Scenarios Controller",
    description = "OPERATIONS RELATED TO THE SCENARIOS IN THE SYSTEM"
)

@RequestMapping("api/scenarios")
interface ScenariosAPI {

    @Operation(summary = "Get all scenarios")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200", description = "Found all scenarios", content = [(Content(
                mediaType = "application/json",
                array = (ArraySchema(schema = Schema(implementation = ScenarioDTO::class)))
            ))]
        ), ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])]
    )
    @GetMapping("")
    fun getAllScenarios(): Collection<ScenarioListDTO>


    @Operation(summary = "Get scenario with the given Id")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200", description = "Found the scenario", content = [(Content(
                mediaType = "application/json", schema = Schema(implementation = ScenarioDTO::class)
            ))]
        ), ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]), ApiResponse(
            responseCode = "404", description = "Did not find the sought scenario", content = [Content()]
        )]
    )
    @GetMapping("{scenarioId}")
    fun getScenarioById(@PathVariable scenarioId: Long): ScenarioDTO


    @Operation(summary = "Get the exercises list of a given scenario")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200", description = "Found the exercises list", content = [(Content(
                mediaType = "application/json",
                array = (ArraySchema(schema = Schema(implementation = ScenarioDTO::class)))
            ))]
        ), ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]), ApiResponse(
            responseCode = "404", description = "Did not find the sought exercise list", content = [Content()]
        )]
    )
    @GetMapping("{scenarioId}/exercises")
    fun getScenarioExercises(@PathVariable scenarioId: Long): Collection<ExerciseDAO>


    @Operation(summary = "Post a new scenario")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "201", description = "Added the scenario", content = [Content()]
        ), ApiResponse(
            responseCode = "400", description = "Bad request", content = [Content()]
        )]
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'INSTRUCTOR')")
    @PostMapping("")
    fun addScenario(@RequestBody dto: CodeAndInstructorDTO)


    @Operation(summary = "Delete a scenario")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "202", description = "Deleted the scenario", content = [Content()]
        ), ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]), ApiResponse(
            responseCode = "404", description = "Did not find any scenario", content = [Content()]
        )]
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'INSTRUCTOR')")
    @DeleteMapping("{scenarioId}")
    fun deleteScenario(@PathVariable scenarioId: Long)

}