package com.bmamaral.learning_logic.services.lti.utils.beans

import edu.uoc.elc.lti.tool.deeplinking.Settings
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

class DeepLinkBean(
    private var settings: Settings?,
    private var deepLinks: List<DeepLinkForm>?
) {
    constructor() : this(Settings(), listOf(DeepLinkForm()))

    fun setSettings(settings: Settings) {
        this.settings = settings
    }

    fun setDeepLinks(deepLinks: List<DeepLinkForm>) {
        this.deepLinks = deepLinks
    }

    fun getSettings(): Settings {
        return settings!!
    }

    fun getDeepLinks(): List<DeepLinkForm> {
        return deepLinks!!
    }
}