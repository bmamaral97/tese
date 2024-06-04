package com.bmamaral.learning_logic.services.lti.utils.factory

import com.bmamaral.learning_logic.services.lti.utils.beans.DeepLinkForm
import edu.uoc.lti.deeplink.content.HtmlItem
import edu.uoc.lti.deeplink.content.Item

class HtmlItemFactory : ItemFactory {
    override fun itemFor(deepLinkForm: DeepLinkForm): Item {
        return HtmlItem.builder()
            .html(deepLinkForm.getUrl())
            .text(deepLinkForm.getText())
            .title(deepLinkForm.getTitle())
            .build()
    }
}