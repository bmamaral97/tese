package com.bmamaral.learning_logic.services.lti.utils.ags

import edu.uoc.elc.lti.platform.ags.GenericResultServiceClient
import edu.uoc.elc.spring.lti.tool.AgsServiceProvider
import edu.uoc.elc.spring.lti.tool.ToolProvider
import edu.uoc.lti.ags.Result

class ResultsVisitor(
    val toolProvider: ToolProvider
) {

    fun getAll(id: String?): List<Result> {
        return getAll(id, null, null, null)
    }

    fun getAll(id: String?, limit: Int?, page: Int?, userId: String?): List<Result> {
        val serviceClient = getServiceClient()
        return serviceClient.getLineItemResults(id, limit, page, userId)
    }

    private fun getServiceClient(): GenericResultServiceClient {
        val agsServiceProvider: AgsServiceProvider = toolProvider.agsServiceProvider
        return agsServiceProvider.resultsServiceClient
    }
}