package com.bmamaral.learning_logic.controller.lti

import com.bmamaral.learning_logic.controller.Paths
import com.bmamaral.learning_logic.services.lti.AgsLineitemsDetailsService
import com.bmamaral.learning_logic.services.lti.LTIService
import com.bmamaral.learning_logic.services.lti.utils.beans.LineItemBean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(Paths.AGS_LINEITEM)
class AgsLineitemsDetailsController(
    val agsLineitemsDetailsService: AgsLineitemsDetailsService,
    val ltiService: LTIService
) {

    @GetMapping("")
    fun index(@RequestParam("id") id: String): LineItemBean {
        val toolProvider = ltiService.toolProvider!!
        return agsLineitemsDetailsService.get(id, toolProvider)
    }
}