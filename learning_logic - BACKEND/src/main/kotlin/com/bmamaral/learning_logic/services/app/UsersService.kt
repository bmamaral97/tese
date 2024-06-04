package com.bmamaral.learning_logic.services.app

import com.bmamaral.learning_logic.model.*
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
class UsersService(var users: UsersRepository) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun getUsers(): List<UserDAO> {
        return this.users.findAll().toList()
    }

    fun getOneUserById(userId: Long): UserDAO =
        users.findById(userId).orElseThrow { NotFoundException("There is no user with that Id: $userId") }

    fun getOneUserByUsername(username: String): UserDAO =
        users.findByUsername(username)
            .orElseThrow { NotFoundException("There is no user with that username: $username") }

    fun getUserOptional(username: String): Optional<UserDAO> = users.findByUsername(username)

    fun createUser(newUser: UserPasswordDTO, userType: String) {
        val user = users.findByUsername(newUser.username)
        if (user.isPresent) {
            logger.error("A user with the given username already exists!: ${newUser.username}")
            throw PreconditionFailedException("A user with the given username already exists!: ${newUser.username}")
        } else {
            newUser.password = BCryptPasswordEncoder().encode(newUser.password)
            when (userType) {
                "STUDENT" -> {
                    val student = users.save(StudentDAO(newUser))
                    logger.info("Added new student to the system!: ${student.id}")
                }
                "INSTRUCTOR" -> {
                    val instructor = users.save(InstructorDAO(newUser))
                    logger.info("Added new instructor to the system!: ${instructor.id}")
                }
                "ADMIN" -> {
                    val admin = users.save(AdminDAO(newUser))
                    logger.info("Added new admin to the system!: ${admin.id}")
                }
                else -> {
                    logger.error("No user of that type exists!: $userType")
                    throw PreconditionFailedException("No user of that type exists!: $userType")
                }
            }
        }
    }

    fun createUser(email: String, authorities: Collection<GrantedAuthority>): UserDAO {
        val user = users.findByUsername(email)
        if (user.isPresent) {
            logger.error("A user with the given username already exists!: $email")
            throw PreconditionFailedException("A user with the given username already exists!: $email")
        } else {
            val data = UserPasswordDTO(email, BCryptPasswordEncoder().encode("password"))
            val student = users.save(StudentDAO(data, authorities))
            logger.info("Added new student to the system!: ${student.id}")
            return student
        }
    }

    fun deleteUser(userId: Long) {
        val user = getOneUserById(userId)
        users.delete(user)
        logger.info("The user was deleted!: $userId")
    }

    @Transactional
    fun getAuthorities(username: String): List<GrantedAuthority> {
        val user = getOneUserByUsername(username)
        val authorities: MutableList<GrantedAuthority> = mutableListOf()
        val userAuthorities = user.authorities.ifEmpty { emptyList() }
        userAuthorities.forEach { role ->
            val authority = SimpleGrantedAuthority(role)
            authorities.add(authority)
        }
        return authorities.toList()
    }

    fun addCompletedExercise(user: UserDAO, exercise: ExerciseDAO) {
        if (user is StudentDAO) {
            user.completeExercise(exercise)
            users.save(user)
        }
    }

    fun getExercisesByInstructor(userId: Long): List<ExerciseDAO> {
        val user = getOneUserById(userId)
        if (user is InstructorDAO) {
            return user.createdExercises
        } else {
            throw NotFoundException("No INSTRUCTOR found with that id.")
        }
    }

    fun getScenariosByInstructor(userId: Long): List<ScenarioDAO> {
        val user = getOneUserById(userId)
        if (user is InstructorDAO) {
            return user.createdScenarios
        } else {
            throw NotFoundException("No INSTRUCTOR found with that id.")
        }
    }

}
