package com.bmamaral.learning_logic.services.lti.utils.factory

import edu.uoc.lti.ags.Score
import edu.uoc.lti.ags.ActivityProgressEnum
import edu.uoc.lti.ags.GradingProgressEnum
import java.time.Instant

class ScoreFactory {
    fun from(userId: String, score: Double, comment: String): Score {
        return Score.builder()
            .userId(userId)
            .scoreGiven(score)
            .comment(comment)
            .timeStamp(Instant.now())
            .activityProgress(ActivityProgressEnum.COMPLETED)
            .gradingProgress(GradingProgressEnum.FULLY_GRADED)
            .build()
    }
}