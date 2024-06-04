package com.bmamaral.learning_logic.controller.lti

import com.bmamaral.learning_logic.services.lti.LTIService
import edu.uoc.elc.lti.platform.accesstoken.AccessTokenResponse
import edu.uoc.elc.lti.tool.Context
import edu.uoc.elc.lti.tool.ResourceLink
import edu.uoc.elc.lti.tool.Tool
import edu.uoc.elc.spring.lti.security.User
import edu.uoc.elc.spring.lti.tool.ToolProvider
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("lti")
class LTIController(
    val ltiService: LTIService
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping("launch")
    fun handleLTILaunch(
        request: HttpServletRequest,
        user: User,
        toolProvider: ToolProvider
    ): ModelAndView {
        ltiService.toolProvider = toolProvider
        val tool: Tool = user.tool
        val resourceLink: ResourceLink = tool.resourceLink
        val token: AccessTokenResponse = tool.accessToken
        val model = ModelAndView("redirect:${getUrlFromResourceLink(resourceLink)}")
        model.addObject("access-token", token.accessToken)
        model.addObject("user", user.email)
        return model
    }

    private fun getUrlFromResourceLink(resourceLink: ResourceLink): String {
        val baseUrl = "http://localhost:3000/"
        val parts = resourceLink.title.split("-")
        val exType = parts[0]
        val exId = parts[1]

        return when (exType) {
            "PL1" -> baseUrl + "scenarios/${exId}"
            "PL2" -> baseUrl + "exercises/PL2/${exId}"
            "PL3" -> baseUrl + "exercises/PL3/${exId}"
            else -> throw Exception("Wrong exType")
        }

    }

}
