package com.bmamaral.learning_logic.services.lti

import com.bmamaral.learning_logic.services.lti.utils.beans.DeepLinkBean
import com.bmamaral.learning_logic.services.lti.utils.beans.DeepLinkCreationResponseBean
import com.bmamaral.learning_logic.services.lti.utils.beans.DeepLinkForm
import com.bmamaral.learning_logic.services.lti.utils.factory.ItemListFactory
import edu.uoc.elc.lti.platform.deeplinking.DeepLinkingClient
import edu.uoc.elc.lti.tool.deeplinking.Settings
import edu.uoc.elc.spring.lti.tool.ToolProvider
import edu.uoc.lti.deeplink.content.Item
import org.springframework.stereotype.Service

@Service
class DeepLinkService {

    lateinit var deepLinkBean: DeepLinkBean
    lateinit var toolProvider: ToolProvider

    fun getMainInfo(toolProvider: ToolProvider): DeepLinkBean {
        val deepLinkBean: DeepLinkBean = prepareResponse(toolProvider)
        deepLinkBean.setDeepLinks(listOf(createDeepLinkForm(toolProvider)))
        this.deepLinkBean = deepLinkBean
        this.toolProvider = toolProvider
        return deepLinkBean
    }

    private fun prepareResponse(toolProvider: ToolProvider): DeepLinkBean {
        val settings: Settings = toolProvider.deepLinkingSettings
        val deepLinkBean = DeepLinkBean()
        deepLinkBean.setSettings(settings)
        return deepLinkBean
    }

    // TODO: check if the deeplink exists
    private fun createDeepLinkForm(toolProvider: ToolProvider): DeepLinkForm {
        val settings: Settings = toolProvider.deepLinkingSettings
        val deepLinkForm = DeepLinkForm()
        deepLinkForm.setTitle(settings.title)
        deepLinkForm.setText(settings.text)
        deepLinkForm.setType("ltiResourceLink")
        //deepLinkForm.setDocumentTarget("window")
        deepLinkForm.setDocumentTarget("frame")

        return deepLinkForm
    }

    fun addItem(deepLinkBean: DeepLinkBean): DeepLinkBean {
        val response: DeepLinkBean = prepareResponse(toolProvider)
        val newDeepLinkForm = createDeepLinkForm(toolProvider)
        val mutableDeepLinks: MutableList<DeepLinkForm> = deepLinkBean.getDeepLinks().toMutableList()
        mutableDeepLinks.add(newDeepLinkForm)
        response.setDeepLinks(mutableDeepLinks.toList())
        return response
    }

    fun removeItemAt(index: Int): DeepLinkBean {
        val response: DeepLinkBean = prepareResponse(toolProvider)
        val mutableDeepLinks: MutableList<DeepLinkForm> = deepLinkBean.getDeepLinks().toMutableList()
        mutableDeepLinks.removeAt(index)
        response.setDeepLinks(mutableDeepLinks)
        return response
    }

    fun save(): DeepLinkCreationResponseBean {
        val items: List<Item> = createItemList(deepLinkBean)
        val deepLinkingClient = getDeepLinkingClient(toolProvider)
        return createItems(items, deepLinkingClient)
    }

    private fun createItemList(deepLinkBean: DeepLinkBean): List<Item> {
        val itemListFactory = ItemListFactory()
        return itemListFactory.itemsFor(deepLinkBean.getDeepLinks())
    }

    private fun getDeepLinkingClient(toolProvider: ToolProvider): DeepLinkingClient {
        return toolProvider.deepLinkingClient
    }

    private fun createItems(items: List<Item>, deepLinkingClient: DeepLinkingClient): DeepLinkCreationResponseBean {
        addItems(items, deepLinkingClient)
        val jwt = deepLinkingClient.buildJWT()
        return DeepLinkCreationResponseBean(jwt, deepLinkingClient.returnUrl)
    }

    private fun addItems(items: List<Item>, deepLinkingClient: DeepLinkingClient) {
        items.stream().forEach { item: Item -> deepLinkingClient.addItem(item) }
    }

}