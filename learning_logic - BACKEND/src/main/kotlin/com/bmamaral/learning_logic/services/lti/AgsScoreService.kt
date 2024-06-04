package com.bmamaral.learning_logic.services.lti

import com.bmamaral.learning_logic.services.lti.utils.factory.ScoreFactory

import edu.uoc.lti.ags.Score
import org.springframework.stereotype.Service

@Service
class AgsScoreService {

    public fun score() {

    }

    private fun createScoreObject(userId: String, score: Double, comment: String) {
        val scoreFactory: ScoreFactory = ScoreFactory()
    }

}