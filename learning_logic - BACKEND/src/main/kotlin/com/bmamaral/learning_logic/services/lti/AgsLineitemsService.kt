package com.bmamaral.learning_logic.services.lti

import com.bmamaral.learning_logic.services.lti.utils.ags.LineItemVisitor
import com.bmamaral.learning_logic.services.lti.utils.beans.AgsBean
import com.bmamaral.learning_logic.services.lti.utils.factory.LineItemFactory
import edu.uoc.elc.spring.lti.tool.ToolProvider

import edu.uoc.lti.ags.LineItem
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AgsLineitemsService {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun list(toolProvider: ToolProvider, tag: String): AgsBean {
        val lineItems: List<LineItem> = getExistingLineItemsForTool(toolProvider, tag)
        return AgsBean(true, lineItems)
    }

    private fun getExistingLineItemsForTool(toolProvider: ToolProvider, tag: String): List<LineItem> {
        val lineItemVisitor = LineItemVisitor(toolProvider)
        return lineItemVisitor.getAll(null, null, tag, null)
    }

    fun createLineItem(
        label: String,
        maxScore: Double,
        resourceLinkId: String,
        toolProvider: ToolProvider,
        tag: String
    ): LineItem {
        val lineItem: LineItem = createObject(label, maxScore, tag, resourceLinkId)
        logger.info("Created new line item: $lineItem")
        saveInPlatform(lineItem, toolProvider)
        return lineItem
    }

    private fun createObject(label: String, maxScore: Double, tag: String, resourceLinkId: String): LineItem {
        val lineItemFactory = LineItemFactory()
        return lineItemFactory.newLineItem(label, maxScore, tag, resourceLinkId)
    }

    private fun saveInPlatform(lineItem: LineItem, toolProvider: ToolProvider): LineItem {
        val lineItemVisitor = LineItemVisitor(toolProvider)
        return lineItemVisitor.create(lineItem)
    }

}