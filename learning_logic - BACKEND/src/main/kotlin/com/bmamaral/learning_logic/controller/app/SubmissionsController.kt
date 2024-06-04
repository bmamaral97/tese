package com.bmamaral.learning_logic.controller.app

import com.bmamaral.learning_logic.controller.app.api.SubmissionsAPI
import com.bmamaral.learning_logic.model.SubmissionDTO
import com.bmamaral.learning_logic.services.app.SubmissionsService
import org.springframework.web.bind.annotation.RestController

@RestController
class SubmissionsController(val submissionsService: SubmissionsService) : SubmissionsAPI {
    override fun getUserSubmissions(username: String): Collection<SubmissionDTO> = handle4xx {
        submissionsService.getUserSubmissions(username).map { SubmissionDTO(it) }
    }

    override fun getUserSubmissionsByExercise(username: String, exerciseId: String): Collection<SubmissionDTO> =
        handle4xx {
            submissionsService.getUserSubmissionsByExercise(username, exerciseId.toLong()).map { SubmissionDTO(it) }
        }

    override fun getOneSubmission(submissionId: Long): SubmissionDTO = handle4xx {
        SubmissionDTO(submissionsService.getOneSubmission(submissionId))
    }

    override fun getFirstSubmissionByExercise(username: String, exerciseId: String): SubmissionDTO = handle4xx {
        SubmissionDTO(submissionsService.getFirstSubmissionByUser(username, exerciseId.toLong()))
    }

}