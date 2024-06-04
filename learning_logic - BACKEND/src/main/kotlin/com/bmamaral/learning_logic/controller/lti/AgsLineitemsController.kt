package com.bmamaral.learning_logic.controller.lti

import com.bmamaral.learning_logic.controller.Paths
import com.bmamaral.learning_logic.services.lti.AgsLineitemsService
import com.bmamaral.learning_logic.services.lti.LTIService
import com.bmamaral.learning_logic.services.lti.utils.beans.AgsBean
import edu.uoc.lti.ags.LineItem
import edu.uoc.lti.ags.Submission
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

const val TAG: String = "grade"

@RestController
@RequestMapping(Paths.AGS)
class AgsLineitemsController(
    val agsLineitemsService: AgsLineitemsService,
    val ltiService: LTIService
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("/")
    fun list(request: HttpServletRequest): AgsBean {
        val toolProvider = ltiService.toolProvider!!
        return agsLineitemsService.list(toolProvider, TAG)
    }

    @PostMapping("/")
    fun createLineItem(@RequestBody dto: LineItemDTO): LineItem {
        val toolProvider = ltiService.toolProvider!!
        return agsLineitemsService.createLineItem(dto.label, dto.maxScore, dto.resourceLinkId, toolProvider, TAG)
    }
}

data class LineItemDTO(
    val label: String,
    val maxScore: Double,
    val resourceLinkId: String
)
