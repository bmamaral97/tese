package com.bmamaral.learning_logic.services.lti.utils.beans

import java.net.URL

class DeepLinkCreationResponseBean(
    private var JWT: String,
    private var url: URL
) {

    fun setJWT(JWT: String) {
        this.JWT = JWT
    }

    fun setUrl(url: URL) {
        this.url = url
    }

    fun getJWT(): String {
        return JWT
    }

    fun getUrl(): URL {
        return url
    }
}