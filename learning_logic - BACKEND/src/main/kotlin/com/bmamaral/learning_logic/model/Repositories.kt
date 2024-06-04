package com.bmamaral.learning_logic.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UsersRepository : JpaRepository<UserDAO, Long> {
    fun findByUsername(username: String): Optional<UserDAO>
}

@Repository
interface ExerciseRepository : JpaRepository<ExerciseDAO, Long> {
    @Query(value = "select e from ExerciseDAO e where e.type=:#{#type}")
    fun findAllByType(@Param("type") type: String): Optional<List<ExerciseDAO>>
}

@Repository
interface ScenarioRepository : JpaRepository<ScenarioDAO, Long> {
    fun findByName(scenarioName: String): Optional<ScenarioDAO>

    @Query("select s from ScenarioDAO s left join fetch s.exercises where s.id=:scenario ")
    fun findWithExercises(@Param("scenario") scenarioId: Long): Optional<ScenarioDAO>
}

@Repository
interface SubmissionsRepository : JpaRepository<SubmissionDAO, Long> {
    fun findByUser(user: UserDAO): Optional<List<SubmissionDAO>>
}