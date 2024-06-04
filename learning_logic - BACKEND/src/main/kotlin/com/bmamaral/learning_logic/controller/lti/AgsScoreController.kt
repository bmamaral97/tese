package com.bmamaral.learning_logic.controller.lti

import com.bmamaral.learning_logic.controller.Paths
import com.bmamaral.learning_logic.services.lti.LTIService
import com.bmamaral.learning_logic.services.lti.utils.ags.ScoreVisitor
import com.bmamaral.learning_logic.services.lti.utils.factory.ScoreFactory
import edu.uoc.elc.spring.lti.tool.ToolProvider
import edu.uoc.lti.ags.Score
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(Paths.AGS_LINEITEM_SCORE)
class AgsScoreController(
    val ltiService: LTIService
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping(Paths.DEEPLINK)
    fun score(@RequestParam("id") id: String, @RequestBody dto: ScoreDTO): Score {
        val toolProvider = ltiService.toolProvider!!
        val scoreObject: Score = createScoreObject(dto.userId, dto.score, dto.comment)
        logger.info(scoreObject.toString())
        val scored: Boolean = saveScoreInPlatform(id, scoreObject, toolProvider)
        if (!scored) {
            throw RuntimeException("Scored failed!!!")
        }
        return scoreObject
    }

    private fun createScoreObject(userId: String, score: Double, comment: String): Score {
        val scoreFactory = ScoreFactory()
        return scoreFactory.from(userId, score, comment)
    }

    private fun saveScoreInPlatform(id: String, score: Score, toolProvider: ToolProvider): Boolean {
        val scoreVisitor = ScoreVisitor(toolProvider)
        return scoreVisitor.score(id, score)
    }

}

data class ScoreDTO(
    val userId: String, // launch request sub
    val score: Double,
    val comment: String,
)