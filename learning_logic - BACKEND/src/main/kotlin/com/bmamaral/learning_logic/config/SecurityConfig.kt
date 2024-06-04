package com.bmamaral.learning_logic.config

import com.bmamaral.learning_logic.services.app.UsersService
import edu.uoc.elc.spring.lti.security.LTIApplicationSecurity
import edu.uoc.elc.spring.lti.tool.ToolDefinitionBean
import edu.uoc.elc.spring.lti.tool.registration.RegistrationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

val whitelistedUrls: Array<String> = arrayOf(
    //APP URLS
    "/api/**",
    "/lti/**",
    "/lti/well-known/jwks.json",
    //H2 CONSOLE URLS
    "/h2-console",
    "/h2-console/**",

    //SWAGGER URLS
    "/v2/api-docs",
    "/swagger-resources",
    "/swagger-resources/**",
    "/configuration/ui",
    "/configuration/security",
    "/swagger-ui.html",
    "/webjars/**",
    "/v3/api-docs/**",
    "/swagger-ui/**"
)

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties
@EnableAutoConfiguration
@ComponentScan("edu.uoc.elc.spring.lti.tool", "edu.uoc.elc.spring.lti.security.openid")
@PropertySource("classpath:application.properties")
class SecurityConfig(
    val users: UsersService,
    @Qualifier("registrationService") registrationService: RegistrationService,
    toolDefinitionBean: ToolDefinitionBean
) : LTIApplicationSecurity(registrationService, toolDefinitionBean) {

    lateinit var auth: AuthenticationManagerBuilder

    @Override
    @Order(20)
    override fun configure(http: HttpSecurity) {
        // TODO: this method applies LTIProcessingFilter to all whitelisted urls, when it should only apply to /lti urls
        super.configure(http)

        http.csrf().disable()
            .servletApi()
            .and()
            .cors()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .httpBasic()
            .and()
            .headers()
            .frameOptions().sameOrigin() // X-Frame-Options to allow any request from same domain
            //.disable() // If necessary, you can disable all the HTTP Security response headers
            .and()
            .authorizeRequests()
            .antMatchers(*whitelistedUrls)
            .permitAll()
            .and()
            .addFilterBefore(
                UserPasswordAuthenticationFilterToJWT("/api/login", this.auth.orBuild, users),
                BasicAuthenticationFilter::class.java
            ).authorizeRequests().antMatchers("/api/login").permitAll()
            .and()
            .addFilterBefore(
                UserPasswordSignUpFilterToJWT("/api/signup", users),
                BasicAuthenticationFilter::class.java
            ).authorizeRequests().antMatchers("/api/signup").permitAll()
            .and()
            .addFilterBefore(
                JWTAuthenticationFilter(),
                BasicAuthenticationFilter::class.java
            ).authorizeRequests().antMatchers("/api/**").permitAll()

            .anyRequest().authenticated()
    }


    @Autowired
    @Override
    @Order(10)
    override fun configureGlobal(
        auth: AuthenticationManagerBuilder
    ) {
        val authenticationProvider = PreAuthenticatedAuthenticationProvider()
        authenticationProvider.setPreAuthenticatedUserDetailsService(
            LTIAuthUserDetailsService(
                users
            )
        )
        auth.authenticationProvider(authenticationProvider)
        auth.authenticationProvider(authProvider())
        this.auth = auth
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? = BCryptPasswordEncoder()

    fun authProvider(): DaoAuthenticationProvider? {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setPasswordEncoder(BCryptPasswordEncoder())
        authenticationProvider.setUserDetailsService(CustomUserDetailsService(users))
        return authenticationProvider
    }

}