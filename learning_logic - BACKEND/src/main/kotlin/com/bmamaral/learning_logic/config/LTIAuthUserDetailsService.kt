package com.bmamaral.learning_logic.config

import com.bmamaral.learning_logic.model.UserDAO
import com.bmamaral.learning_logic.services.app.UsersService
import edu.uoc.elc.lti.tool.Tool
import edu.uoc.elc.spring.lti.security.User
import org.hibernate.LazyInitializationException
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails

class LTIAuthUserDetailsService<T : Authentication>(
    private val usersService: UsersService,
) : AuthenticationUserDetailsService<T> {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun loadUserDetails(token: T): UserDetails {
        val tool: Tool = getTool(token)
        val authorities: Collection<GrantedAuthority> = getAuthorities(token)
        val user: UserDAO = getOrCreateUser(tool.user, authorities)

        return User(user.username, user.password, tool, authorities)
    }

    private fun getTool(token: T): Tool {
        if (token.credentials is Tool) {
            val tool = token.credentials as Tool
            if (tool.isValid) {
                return tool
            }
        }
        logger.error("Error getting tool from token.credentials")
        throw RuntimeException("Error getting tool from token.credentials")
    }

    private fun getAuthorities(token: T): Collection<GrantedAuthority> {
        var authorities: Collection<GrantedAuthority> = token.authorities
        if (token.details is PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails) {
            authorities =
                (token.details as PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails).grantedAuthorities
        }
        return authorities
    }

    private fun getOrCreateUser(user: edu.uoc.elc.lti.tool.User, authorities: Collection<GrantedAuthority>): UserDAO {
        try {
            val userOptional = usersService.getUserOptional(user.email)
            return if (userOptional.isPresent) {
                userOptional.get()
            } else {
                usersService.createUser(user.email, authorities)
            }
        } catch (e: LazyInitializationException) {
            logger.error("Lazy Initialization Exception")
            e.printStackTrace()
        }
        logger.error("Error fetching or creating new user")
        throw RuntimeException("Error fetching or creating new user")
    }

}