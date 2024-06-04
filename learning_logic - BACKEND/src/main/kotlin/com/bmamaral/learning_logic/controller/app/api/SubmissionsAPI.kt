package com.bmamaral.learning_logic.controller.app.api

import com.bmamaral.learning_logic.model.SubmissionDTO
import io.swagger.annotations.Api

import org.springframework.web.bind.annotation.*

@Api(
    value = "LEARNING LOGIC - Submissions Controller",
    description = "OPERATIONS RELATED TO THE SUBMISSIONS IN THE SYSTEM"
)

@RequestMapping("api/submissions")
interface SubmissionsAPI {

    @GetMapping("{username}")
    fun getUserSubmissions(@PathVariable username: String): Collection<SubmissionDTO>

    @GetMapping("{username}/exercise/{exerciseId}")
    fun getUserSubmissionsByExercise(
        @PathVariable username: String,
        @PathVariable exerciseId: String
    ): Collection<SubmissionDTO>

    @GetMapping("{submissionId}")
    fun getOneSubmission(@PathVariable submissionId: Long): SubmissionDTO

    @PostMapping("{username}/first/{exerciseId}")
    fun getFirstSubmissionByExercise(
        @PathVariable username: String,
        @PathVariable exerciseId: String
    ): SubmissionDTO

}