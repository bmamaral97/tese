package com.bmamaral.learning_logic.services.app

import com.bmamaral.learning_logic.model.SubmissionDAO
import com.bmamaral.learning_logic.model.SubmissionsRepository
import org.springframework.stereotype.Service

@Service
class SubmissionsService(
    private val submissionsRepository: SubmissionsRepository,
    private val users: UsersService
) {
    // get a single submission (by submissionId)
    fun getOneSubmission(submissionId: Long): SubmissionDAO =
        submissionsRepository.findById(submissionId)
            .orElseThrow { NotFoundException("There is no submission with that id: $submissionId") }

    // get all submissions of a given user (by username)
    fun getUserSubmissions(username: String): List<SubmissionDAO> {
        val user = users.getOneUserByUsername(username)
        val submissions = submissionsRepository.findByUser(user)
        if (submissions.isPresent) {
            return submissions.get()
        } else {
            throw NotFoundException("This user has no submissions registered: $username")
        }
    }

    // get all submissions of a given user on a particular exercise
    fun getUserSubmissionsByExercise(username: String, exerciseId: Long): List<SubmissionDAO> {
        val userSubmissions = getUserSubmissions(username)
        // filter collection to only include elements where it.exercise.id == exerciseId
        userSubmissions.filter { it.exercise.id == exerciseId }
        return userSubmissions
    }

    // get the first submission of a certain user on a given exercise (by username and exerciseId)
    fun getFirstSubmissionByUser(username: String, exerciseId: Long): SubmissionDAO {
        val user = users.getOneUserByUsername(username) //if user doesn't exist, throws exception
        val userSubmissions = getUserSubmissions(user.username)

        if (userSubmissions.isEmpty()) {
            throw NotFoundException("Given user has no submissions in the system yet!")
        }
        // filter the collection to only have exercises with the requested id
        userSubmissions.filter { it.exercise.id == exerciseId }
        // sort submissions by submittedAt property, and return the oldest entry
        userSubmissions.sortedBy { it.submittedAt }
        return userSubmissions.first()
    }


}