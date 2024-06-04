package com.bmamaral.learning_logic.config

import edu.uoc.elc.lti.tool.Registration
import edu.uoc.elc.spring.lti.ags.RestTemplateFactory
import edu.uoc.elc.spring.lti.tool.builders.ClaimAccessorService
import edu.uoc.elc.spring.lti.tool.builders.ClientCredentialsTokenBuilderService
import edu.uoc.elc.spring.lti.tool.builders.DeepLinkingTokenBuilderService
import edu.uoc.lti.accesstoken.AccessTokenRequestBuilder
import edu.uoc.lti.accesstoken.JSONAccessTokenRequestBuilderImpl
import edu.uoc.lti.accesstoken.UrlEncodedFormAccessTokenRequestBuilderImpl
import edu.uoc.lti.jwt.claims.JWSClaimAccessor
import edu.uoc.lti.jwt.client.JWSClientCredentialsTokenBuilder
import edu.uoc.lti.jwt.deeplink.JWSTokenBuilder

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration


@Configuration
@ComponentScan(value = ["edu.uoc.elc.spring.lti.tool.builders"])
class LTIConfig {

    @Bean
    fun claimAccessorService(): ClaimAccessorService {
        return ClaimAccessorService { registration: Registration -> JWSClaimAccessor(registration.keySetUrl) }
    }

    @Bean
    fun deepLinkingTokenBuilderService(): DeepLinkingTokenBuilderService {
        return DeepLinkingTokenBuilderService { registration: Registration, kid: String ->
            val key = registration.keySet.keys[0]
            JWSTokenBuilder(key.publicKey, key.privateKey, key.algorithm)
        }
    }

    @Bean
    fun clientCredentialsTokenBuilderService(): ClientCredentialsTokenBuilderService {
        return ClientCredentialsTokenBuilderService { registration: Registration, kid: String ->
            val key = registration.keySet.keys[0]
            CustomJWSClientCredentialsTokenBuilder(key.publicKey, key.privateKey, key.algorithm)
        }
    }

    @Bean
    fun accessTokenRequestBuilder(): AccessTokenRequestBuilder {
        return UrlEncodedFormAccessTokenRequestBuilderImpl()
//        return JSONAccessTokenRequestBuilderImpl()
    }

    @Bean
    fun restTemplateFactory(): RestTemplateFactory {
        return RestTemplateFactory()
    }


}