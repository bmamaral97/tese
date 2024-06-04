package com.bmamaral.learning_logic.services.lti.utils.factory

import com.bmamaral.learning_logic.services.lti.utils.beans.DeepLinkForm
import edu.uoc.lti.deeplink.content.Item
import java.util.stream.Collectors

class ItemListFactory() {

    fun itemsFor(deepLinkForms: List<DeepLinkForm>): List<Item> {
        return deepLinkForms.stream().map(this::itemFor).collect(Collectors.toList())
    }

    private fun itemFor(deepLinkForm: DeepLinkForm): Item {
        val itemFactory: ItemFactory = getItemFactory(deepLinkForm.getType())
        return itemFactory.itemFor(deepLinkForm)
    }

    private fun getItemFactory(type: String): ItemFactory {
        if ("link" == type) {
            return LinkItemFactory()
        }
        if ("file" == type) {
            return FileItemFactory()
        }
        if ("html" == type) {
            return HtmlItemFactory()
        }
        if ("ltiResourceLink" == type) {
            return LtiResourceItemFactory()
        }
        throw Exception("Wrong item type: $type")
    }

}