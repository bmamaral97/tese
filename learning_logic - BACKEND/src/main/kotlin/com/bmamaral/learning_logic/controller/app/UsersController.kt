package com.bmamaral.learning_logic.controller.app

import com.bmamaral.learning_logic.controller.app.api.UsersAPI
import com.bmamaral.learning_logic.model.*
import com.bmamaral.learning_logic.services.app.UsersService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UsersController(val users: UsersService) : UsersAPI {

    override fun getRegisteredUsers(): Collection<UserDTO> =
        handle4xx { users.getUsers().map { UserDTO(it) } }

    override fun getOneUserById(@PathVariable userId: Long): UserDTO =
        handle4xx { getDto(userId) }

    private fun getDto(userId: Long): UserDTO {
        val user = users.getOneUserById(userId)
        return if (user is InstructorDAO) {
            InstructorDTO(user)
        } else {
            UserDTO(user)
        }
    }

    override fun postUser(@RequestBody dto: PostUserDTO) =
        handle4xx { users.createUser(dto.userPassword, dto.userType) }

    override fun deleteUser(@PathVariable userId: Long) =
        handle4xx { users.deleteUser(userId) }

    override fun getExercisesByInstructor(userId: Long): List<ExerciseDTO> =
        handle4xx { users.getExercisesByInstructor(userId).map { ExerciseDTO(it) } }

    override fun getScenariosByInstructor(userId: Long): List<ScenarioListDTO> =
        handle4xx { users.getScenariosByInstructor(userId).map { ScenarioListDTO(it) } }

}