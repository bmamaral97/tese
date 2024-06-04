package com.bmamaral.learning_logic.services.lti.utils.ags

import edu.uoc.elc.lti.platform.ags.ToolLineItemServiceClient
import edu.uoc.elc.spring.lti.tool.AgsServiceProvider
import edu.uoc.elc.spring.lti.tool.ToolProvider
import edu.uoc.lti.ags.LineItem

class LineItemVisitor(
    private val toolProvider: ToolProvider
) {

    fun canGet(): Boolean {
        val agsServiceProvider: AgsServiceProvider = toolProvider.agsServiceProvider
        return agsServiceProvider.hasAgsService()
    }

    fun getAll(): List<LineItem> {
        return getAll(null, null, null, null)
    }

    fun getAll(limit: Int?, page: Int?, tag: String?, resourceId: String?): List<LineItem> {
        val lineItemsServiceClient = getServiceClient()
        return lineItemsServiceClient.getLineItems(limit, page, tag, resourceId)
    }

    fun get(id: String): LineItem {
        val lineItemsServiceClient: ToolLineItemServiceClient = getServiceClient()
        return lineItemsServiceClient.getLineItem(id)
    }

    fun create(lineItem: LineItem): LineItem {
        val lineItemsServiceClient = getServiceClient()
        return lineItemsServiceClient.createLineItem(lineItem)
    }

    private fun getServiceClient(): ToolLineItemServiceClient {
        val agsServiceProvider: AgsServiceProvider = toolProvider.agsServiceProvider
        return agsServiceProvider.lineItemsServiceClient
    }
}