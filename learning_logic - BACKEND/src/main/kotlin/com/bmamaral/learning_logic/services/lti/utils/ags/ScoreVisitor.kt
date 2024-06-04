package com.bmamaral.learning_logic.services.lti.utils.ags

import edu.uoc.elc.lti.platform.ags.GenericScoreServiceClient
import edu.uoc.elc.spring.lti.tool.AgsServiceProvider
import edu.uoc.elc.spring.lti.tool.ToolProvider
import edu.uoc.lti.ags.Score

class ScoreVisitor(
    private val toolProvider: ToolProvider
) {
    fun score(id: String, score: Score): Boolean {
        val serviceClient: GenericScoreServiceClient = getServiceClient()
        return serviceClient.score(id, score)
    }

    private fun getServiceClient(): GenericScoreServiceClient {
        val agsServiceProvider: AgsServiceProvider = toolProvider.agsServiceProvider
        return agsServiceProvider.scoreServiceClient
    }
}