package com.bmamaral.learning_logic.controller.lti

import com.bmamaral.learning_logic.services.lti.utils.beans.DeepLinkBean
import com.bmamaral.learning_logic.services.lti.utils.beans.DeepLinkCreationResponseBean
import com.bmamaral.learning_logic.controller.Paths
import com.bmamaral.learning_logic.services.lti.DeepLinkService
import edu.uoc.elc.spring.lti.tool.ToolProvider
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView
import java.util.*

@RestController
@RequestMapping("")
class DeeplinkController(val deepLinkService: DeepLinkService) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping(Paths.DEEPLINK)
    fun init(toolProvider: ToolProvider): ModelAndView {
        val obj: Any = deepLinkService.getMainInfo(toolProvider)

        return ModelAndView("redirect:http://localhost:3000/lti/deep_links", "object", obj)
    }

    @PostMapping(value = [Paths.DEEPLINK_NEW], params = ["add"])
    fun addItem(deepLinkBean: DeepLinkBean): DeepLinkBean {
        return deepLinkService.addItem(deepLinkBean)
    }

    @PostMapping(value = [Paths.DEEPLINK_NEW], params = ["remove"])
    fun removeItem(@RequestParam(value = "remove") index: Int): DeepLinkBean {
        return deepLinkService.removeItemAt(index)
    }

    @PostMapping(value = [Paths.DEEPLINK_NEW], params = ["save"])
    fun save(): DeepLinkCreationResponseBean {
        return deepLinkService.save()
    }
}