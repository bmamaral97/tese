package com.bmamaral.learning_logic.config

import com.bmamaral.learning_logic.model.UserDAO
import com.bmamaral.learning_logic.model.UserPasswordDTO
import com.bmamaral.learning_logic.model.UserSecurityDAO
import com.bmamaral.learning_logic.services.app.UsersService
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.web.filter.GenericFilterBean
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

object JWTSecret {
    private const val SECRET = "fTjWnZr4u7x!A%D*G-KaNdRgUkXp2s5v"
    val KEY: String = Base64.getEncoder().encodeToString(SECRET.toByteArray())
    const val SUBJECT = "JSON Web Token for Learning Logic 22/23"
    const val VALIDITY = 3600000  // 1 hour in milliseconds
}

private fun addResponseToken(authentication: Authentication, response: HttpServletResponse) {
    val claims = HashMap<String, Any?>()
    claims["username"] = authentication.name
    claims["roles"] = authentication.authorities

    val token =
        Jwts.builder().setClaims(claims).setSubject(JWTSecret.SUBJECT).setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + JWTSecret.VALIDITY))
            .signWith(SignatureAlgorithm.HS256, JWTSecret.KEY).compact()

    response.addHeader("Authorization", "Bearer $token")
}

class UserPasswordAuthenticationFilterToJWT(
    defaultFilterProcessesUrl: String?,
    private val anAuthenticationManager: AuthenticationManager,
    val users: UsersService
) : AbstractAuthenticationProcessingFilter(defaultFilterProcessesUrl) {

    override fun attemptAuthentication(
        request: HttpServletRequest?, response: HttpServletResponse?
    ): Authentication? {
        //getting user from request body
        val user = ObjectMapper().readValue(request!!.inputStream, UserSecurityDAO::class.java)
        val authorities = users.getAuthorities(user.username)
        // perform the "normal" authentication
        val auth = anAuthenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                user.username, user.password, authorities
            )
        )
        return if (auth.isAuthenticated) {
            // Proceed with an authenticated user
            SecurityContextHolder.getContext().authentication = auth
            auth
        } else null
    }

    override fun successfulAuthentication(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain?, auth: Authentication
    ) {
        // When returning from the Filter loop, add the token to the response
        addResponseToken(auth, response)
    }

}

class UserAuthToken(private var login: String, private var authorities: Collection<GrantedAuthority>) :
    Authentication {

    override fun getAuthorities() = authorities

    override fun setAuthenticated(isAuthenticated: Boolean) {}

    override fun getName() = login

    override fun getCredentials() = null

    override fun getPrincipal() = this

    override fun isAuthenticated() = true

    override fun getDetails() = login
}

class JWTAuthenticationFilter() : GenericFilterBean() {

    override fun doFilter(
        request: ServletRequest?, response: ServletResponse?, chain: FilterChain?
    ) {
        val authHeader = (request as HttpServletRequest).getHeader("Authorization")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7) // Skip 7 characters for "Bearer
            val claims = Jwts.parser().setSigningKey(JWTSecret.KEY).parseClaimsJws(token).body
            // should check for token validity here (e.g. expiration date, session in db, etc.)
            val exp = (claims["exp"] as Int).toLong()
            if (exp < System.currentTimeMillis() / 1000) { // in seconds
                (response as HttpServletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED) // RFC 6750 3.1
            } else {
                val authentication = UserAuthToken(claims["username"] as String, getAuthorities(claims))
                // Can go to the database to get the actual user information (e.g. authorities)
                SecurityContextHolder.getContext().authentication = authentication
                // Renew token with extended time here. (before doFilter)
                addResponseToken(authentication, response as HttpServletResponse)
                //println(response.getHeader("Authorization"))
                chain!!.doFilter(request, response)
            }
        } else {
            chain!!.doFilter(request, response)
        }
    }

    private fun getAuthorities(map: Claims): MutableCollection<GrantedAuthority> {
        val roles = map["roles"]
        val list: MutableCollection<GrantedAuthority> = mutableListOf()

        if (roles is List<*>) {
            for (role in roles) {
                var aux: String = role.toString()
                aux = aux.drop(1)
                aux = aux.dropLast(1)
                aux = aux.substring(10)
                list.add(SimpleGrantedAuthority(aux))
            }
        }
        return list
    }

}

class UserPasswordSignUpFilterToJWT(
    defaultFilterProcessesUrl: String?, private val users: UsersService
) : AbstractAuthenticationProcessingFilter(defaultFilterProcessesUrl) {

    override fun attemptAuthentication(
        request: HttpServletRequest?, response: HttpServletResponse?
    ): Authentication {
        val user = ObjectMapper().readValue(request!!.inputStream, UserDAO::class.java)
        return Optional.of(users.createUser(UserPasswordDTO(user.username, user.password), "STUDENT")).orElse(null)
            .let {
                val auth = UserAuthToken(user.username, users.getAuthorities(user.username))
                SecurityContextHolder.getContext().authentication = auth
                auth
            }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain?, auth: Authentication
    ) {
        addResponseToken(auth, response)
    }

}