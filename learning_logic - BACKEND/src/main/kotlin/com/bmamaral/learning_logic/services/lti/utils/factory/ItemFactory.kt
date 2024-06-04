package com.bmamaral.learning_logic.services.lti.utils.factory

import com.bmamaral.learning_logic.services.lti.utils.beans.DeepLinkForm
import edu.uoc.lti.deeplink.content.Item

interface ItemFactory {
    fun itemFor(deepLinkForm: DeepLinkForm): Item
}