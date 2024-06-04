package com.bmamaral.learning_logic.controller.app.api

import com.bmamaral.learning_logic.model.*
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
    value = "LEARNING LOGIC - Exercises Controller", description = "OPERATIONS RELATED TO THE EXERCISES IN THE SYSTEM"
)

@RequestMapping("api/exercises")
interface ExercisesAPI {

    @Operation(summary = "Get exercises list by type")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200", description = "Found all exercises by type", content = [(Content(
                mediaType = "application/json",
                array = (ArraySchema(schema = Schema(implementation = ExerciseDAO::class)))
            ))]
        ), ApiResponse(
            responseCode = "400", description = "Bad request", content = [Content()]
        )]
    )
    @GetMapping("")
    fun getExercisesByType(@RequestParam exType: String): Collection<ExerciseDTO>


    @Operation(summary = "Get one exercise by Id")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Found the exercise",
            content = [(Content(mediaType = "application/json", schema = Schema(implementation = ExerciseDAO::class)))]
        ), ApiResponse(
            responseCode = "400", description = "Bad request", content = [Content()]
        ), ApiResponse(
            responseCode = "404", description = "Did not find the sought exercise", content = [Content()]
        )]
    )
    @GetMapping("{exId}")
    fun getOneExerciseById(@PathVariable exId: Long): ExerciseDTO


    @Operation(summary = "Post a new exercise")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "201", description = "Added the exercise", content = [Content()]
        ), ApiResponse(
            responseCode = "400", description = "Bad request", content = [Content()]
        )]
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'INSTRUCTOR')")
    @PostMapping("")
    fun postExercise(@RequestParam exType: String, @RequestBody dto: CodeAndInstructorDTO)


    @Operation(summary = "Delete an exercise")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "202", description = "Deleted the exercise", content = [Content()]
        ), ApiResponse(
            responseCode = "400", description = "Bad request", content = [Content()]
        ), ApiResponse(
            responseCode = "404", description = "Did not find any exercise", content = [Content()]
        )]
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'INSTRUCTOR')")
    @DeleteMapping("{exId}")
    fun deleteExercise(@PathVariable exId: Long)

    @Operation(summary = "Grade student answer")
    @PostMapping("{exId}/grades")
    fun gradeExercise(@PathVariable exId: Long, @RequestBody dto: AnswerDTO): GradeResponseDTO

    @Operation(summary = "Generate truth table for exercise expression")
    @GetMapping("{exId}/truth-table")
    fun createTruthTable(@PathVariable exId: Long): TruthTableDTO

    @Operation(summary = "Validate an expression")
    @PostMapping("{exId}/validator")
    fun validateExpression(@PathVariable exId: Long, @RequestBody dto: ExpressionDTO): Boolean

}