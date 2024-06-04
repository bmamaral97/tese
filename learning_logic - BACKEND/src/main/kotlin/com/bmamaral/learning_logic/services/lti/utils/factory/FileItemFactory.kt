package com.bmamaral.learning_logic.services.lti.utils.factory

import com.bmamaral.learning_logic.services.lti.utils.beans.DeepLinkForm
import edu.uoc.lti.deeplink.content.FileItem
import edu.uoc.lti.deeplink.content.Item

class FileItemFactory : ItemFactory {
    override fun itemFor(deepLinkForm: DeepLinkForm): Item {
        val item = FileItem.builder()
            .title(deepLinkForm.getTitle())
            .url(deepLinkForm.getUrl())
            .mediaType(deepLinkForm.getMediaType())
            .build()
        item.text = deepLinkForm.getText()
        return item
    }
}