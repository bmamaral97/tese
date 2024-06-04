package com.bmamaral.learning_logic.services.lti.utils.factory

import com.bmamaral.learning_logic.services.lti.utils.beans.DeepLinkForm
import edu.uoc.lti.deeplink.content.*

class LinkItemFactory : ItemFactory {
    override fun itemFor(deepLinkForm: DeepLinkForm): Item {
        var item = LinkItem.builder()
            .url(deepLinkForm.getUrl())
            .title(deepLinkForm.getTitle())
            .text(deepLinkForm.getText())
            .build()
        item = setDocumentTarget(deepLinkForm.getDocumentTarget(), item)
        return item
    }

    private fun setDocumentTarget(documentTarget: String, item1: LinkItem): LinkItem {
        var item = item1
        try {
            val documentTargetEnum = DocumentTargetEnum.valueOf(documentTarget)
            item = setDocumentTarget(documentTargetEnum, item)
        } catch (e: IllegalArgumentException) {
            // nothing here
        }
        return item
    }

    private fun setDocumentTarget(documentTargetEnum: DocumentTargetEnum, item: LinkItem): LinkItem {
        if (documentTargetEnum == DocumentTargetEnum.iframe) {
            item.iframe = IFrame.builder()
                .url(item.url)
                .build()
        }
        if (documentTargetEnum == DocumentTargetEnum.window) {
            item.window = Window.builder().build()
        }
        return item
    }
}