package com.bmamaral.learning_logic.services.lti.utils.factory

import com.bmamaral.learning_logic.services.lti.utils.beans.DeepLinkForm
import com.bmamaral.learning_logic.services.lti.utils.factory.ItemFactory
import edu.uoc.lti.deeplink.content.*

class LtiResourceItemFactory : ItemFactory {
    override fun itemFor(deepLinkForm: DeepLinkForm): Item {
        var item = LtiResourceItem.builder()
            .title(deepLinkForm.getTitle())
            .url(deepLinkForm.getUrl())
            .build()
        item = setDocumentTarget(deepLinkForm.getDocumentTarget(), item)
        return item
    }

    private fun setDocumentTarget(documentTarget: String, item: LtiResourceItem): LtiResourceItem {
        val documentTargetEnum = DocumentTargetEnum.valueOf(documentTarget)
        if (documentTargetEnum == DocumentTargetEnum.iframe) {
            item.iFrame = IFrame.builder()
                .url(item.url)
                .build()
        }
        if (documentTargetEnum == DocumentTargetEnum.window) {
            item.window = Window.builder().build()
        }
        return item
    }
}