package com.bmamaral.learning_logic.config

import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import edu.uoc.lti.clientcredentials.ClientCredentialsRequest
import edu.uoc.lti.clientcredentials.ClientCredentialsTokenBuilder
import edu.uoc.lti.jwt.AlgorithmFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*

class CustomJWSClientCredentialsTokenBuilder(
    private val publicKey: String,
    private val privateKey: String,
    private val algorithm: String,
) : ClientCredentialsTokenBuilder {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun build(request: ClientCredentialsRequest): String {
        // Set JWT headers
        val headers: JWSHeader = JWSHeader.Builder(JWSAlgorithm.RS256)
            .type(JOSEObjectType.JWT)
            .keyID(request.kid)
            .build()

        // Set JWT claims
        val claimsSet: JWTClaimsSet = JWTClaimsSet.Builder()
            .issuer(request.clientId)
            .subject(request.clientId)
            .audience(request.oauth2Url)
            .issueTime(Date.from(Instant.now()))
            .jwtID(UUID.randomUUID().toString())
            .expirationTime(Date.from(Instant.now().plusSeconds(300))) //5 minutes
            .build()

        val algorithmFactory = AlgorithmFactory(publicKey, privateKey, algorithm)
        val signer = RSASSASigner(algorithmFactory.privateKey)

        // Create the JWT with headers and claims
        val signedJWT = SignedJWT(headers, claimsSet)

        signedJWT.sign(signer) // Sign JWT with private key
        val token = signedJWT.serialize()

        log.info("Created signed token: {}", token)
        return token
    }
}