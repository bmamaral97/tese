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
    value = "LEARNING LOGIC - Users Controller",
    description = "OPERATIONS RELATED TO THE USERS OF THE SYSTEM"
)

@RequestMapping("api/users")
interface UsersAPI {

    @Operation(summary = "Get all registered users")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200", description = "Found all users", content = [(Content(
                mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = UserDTO::class)))
            ))]
        ), ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])]
    )
    @GetMapping("")
    fun getRegisteredUsers(): Collection<UserDTO>


    @Operation(summary = "Get one user given the Id")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Found the user",
            content = [(Content(mediaType = "application/json", schema = Schema(implementation = UserDTO::class)))]
        ), ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]), ApiResponse(
            responseCode = "404",
            description = "Did not find the sought user",
            content = [Content()]
        )]
    )
    @GetMapping("{userId}")
    fun getOneUserById(@PathVariable userId: Long): UserDTO


    @Operation(summary = "Post a new user")
    @ApiResponses(
        value = [ApiResponse(responseCode = "201", description = "Added the user", content = [Content()]), ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = [Content()]
        )]
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("")
    fun postUser(@RequestBody dto: PostUserDTO)


    @Operation(summary = "Delete a user")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "202",
            description = "Deleted the user",
            content = [Content()]
        ), ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]), ApiResponse(
            responseCode = "404",
            description = "Did not find any user",
            content = [Content()]
        )]
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{userId}")
    fun deleteUser(@PathVariable userId: Long)


    @Operation(summary = "Get exercises created by a given instructor")
    @GetMapping("{userId}/exercises")
    fun getExercisesByInstructor(@PathVariable userId: Long): Collection<ExerciseDTO>


    @Operation(summary = "Get scenarios created by a given instructor")
    @GetMapping("{userId}/scenarios")
    fun getScenariosByInstructor(@PathVariable userId: Long): Collection<ScenarioListDTO>
}