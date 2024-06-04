package com.bmamaral.learning_logic.controller.lti

import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.KeyUse
import com.nimbusds.jose.jwk.RSAKey
import edu.uoc.elc.lti.tool.Registration
import edu.uoc.elc.spring.lti.tool.registration.RegistrationService
import org.bouncycastle.asn1.*
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.*
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

@RestController
@RequestMapping("lti")
class JWKSController(
    @Qualifier("registrationService") val registrationService: RegistrationService
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("well-known/jwks.json")
    fun shareKeys(): String {
        try {
            val keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
            val registration: Registration = registrationService.getRegistration("1")
            val registrationKeys = registration.keySet.keys[0]

            val publicKey = getPublicKey(
                keyFactory,
                registrationKeys.publicKey
            )!!

            logger.info("RSA Public key: $publicKey")

            val privateKey = getPrivateKey(
                keyFactory,
                registrationKeys.privateKey
            )!!

            logger.info("RSA Private key: $privateKey")

            val keys = KeyPair(
                publicKey, privateKey
            )

            val jwk = convertToJWK(keys)

            val set = JWKSet(jwk)

            return set.toString(true)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    private fun getPublicKey(keyFactory: KeyFactory, key: String): PublicKey? {
        val publicKey: PublicKey? = null

        try {
            val keyBytes: ByteArray = Base64.getDecoder().decode(key)
            val publicKeySpec = X509EncodedKeySpec(keyBytes)
            return keyFactory.generatePublic(publicKeySpec)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return publicKey

    }

    private fun getPrivateKey(keyFactory: KeyFactory, key: String): PrivateKey? {
        val privateKey: PrivateKey? = null

        try {
            val keyBytes: ByteArray = Base64.getDecoder().decode(key)

            /* Add PKCS#8 formatting */
            val v = ASN1EncodableVector()
            v.add(ASN1Integer(0))
            val v2 = ASN1EncodableVector()
            v2.add(ASN1ObjectIdentifier(PKCSObjectIdentifiers.rsaEncryption.id))
            v2.add(DERNull.INSTANCE)
            v.add(DERSequence(v2))
            v.add(DEROctetString(keyBytes))
            val seq: ASN1Sequence = DERSequence(v)
            val privKey: ByteArray = seq.getEncoded("DER")

            val privateKeySpec = PKCS8EncodedKeySpec(privKey)
            return keyFactory.generatePrivate(privateKeySpec)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return privateKey
    }

    private fun convertToJWK(keyPair: KeyPair): JWK {
        return RSAKey.Builder(keyPair.public as RSAPublicKey)
            .privateKey(keyPair.private as RSAPrivateKey)
            .keyUse(KeyUse.SIGNATURE)
            .keyID(UUID.randomUUID().toString())
            .issueTime(Date())
            .build()
    }

}