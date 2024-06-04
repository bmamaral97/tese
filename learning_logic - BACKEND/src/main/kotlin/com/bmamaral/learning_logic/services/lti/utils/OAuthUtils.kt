package com.bmamaral.learning_logic.services.lti.utils

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

object OAuthUtils {
    private const val PEM_PUBLIC_START = "-----BEGIN PUBLIC KEY-----"
    private const val PEM_PUBLIC_END = "-----END PUBLIC KEY-----"
    private const val PEM_PRIVATE_START = "-----BEGIN PRIVATE KEY-----"
    private const val PEM_PRIVATE_END = "-----END PRIVATE KEY-----"

    @Throws(GeneralSecurityException::class)
    fun loadPublicKey(key: String): RSAPublicKey {
        val publicKeyContent = key.replace("\\n", "").replace("\n", "").replace(PEM_PUBLIC_START, "")
            .replace(PEM_PUBLIC_END, "")
        val kf: KeyFactory = KeyFactory.getInstance("RSA")
        val keySpecX509 = X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent))
        return kf.generatePublic(keySpecX509) as RSAPublicKey
    }

    @Throws(GeneralSecurityException::class)
    fun loadPrivateKey(key: String): PrivateKey? {
        // PKCS#8 format
        var privateKeyPem = key

        if (privateKeyPem.contains(PEM_PRIVATE_START)) { // PKCS#8 format
            privateKeyPem = privateKeyPem.replace(PEM_PRIVATE_START, "").replace(PEM_PRIVATE_END, "")
            privateKeyPem = privateKeyPem.replace("\\s".toRegex(), "")
            val pkcs8EncodedKey: ByteArray = Base64.getDecoder().decode(privateKeyPem)
            val kf: KeyFactory = KeyFactory.getInstance("RSA")
            return kf.generatePrivate(PKCS8EncodedKeySpec(pkcs8EncodedKey))
        }
        throw GeneralSecurityException("Not supported format of a private key")
    }
}