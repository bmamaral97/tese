package com.bmamaral.learning_logic.services.lti

import com.bmamaral.learning_logic.services.lti.utils.ags.LineItemVisitor
import com.bmamaral.learning_logic.services.lti.utils.ags.MemberVisitor
import com.bmamaral.learning_logic.services.lti.utils.ags.ResultsVisitor
import com.bmamaral.learning_logic.services.lti.utils.beans.LineItemBean
import edu.uoc.elc.lti.platform.Member
import edu.uoc.elc.spring.lti.tool.ToolProvider
import edu.uoc.lti.ags.LineItem
import edu.uoc.lti.ags.Result
import org.springframework.stereotype.Service


@Service
class AgsLineitemsDetailsService {
    fun get(id: String, toolProvider: ToolProvider): LineItemBean {
        val lineItem: LineItem = getLineItem(id, toolProvider)
        val results: List<Result> = getResultsOfLineItem(id, toolProvider)
        val members: List<Member> = getMembers(toolProvider)
        return LineItemBean(lineItem, members, results)
    }

    private fun getLineItem(id: String, toolProvider: ToolProvider): LineItem {
        val lineItemVisitor = LineItemVisitor(toolProvider)
        return lineItemVisitor.get(id)
    }

    private fun getResultsOfLineItem(id: String, toolProvider: ToolProvider): List<Result> {
        val resultsVisitor = ResultsVisitor(toolProvider)
        return resultsVisitor.getAll(id)
    }

    private fun getMembers(toolProvider: ToolProvider): List<Member> {
        val memberVisitor = MemberVisitor(toolProvider)
        return memberVisitor.getAll()
    }
}