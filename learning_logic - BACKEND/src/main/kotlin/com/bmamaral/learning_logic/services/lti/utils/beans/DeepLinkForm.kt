package com.bmamaral.learning_logic.services.lti.utils.beans

class DeepLinkForm(
    private var title: String,
    private var text: String,
    private var url: String,
    private var documentTarget: String,
    private var type: String,
    private var mediaType: String,
) {
    constructor() : this("", "", "", "", "", "")

    fun setTitle(title: String) {
        this.title = title
    }

    fun setText(text: String) {
        this.text = text
    }

    fun setUrl(url: String) {
        this.url = url
    }

    fun setDocumentTarget(documentTarget: String) {
        this.documentTarget = documentTarget
    }

    fun setType(type: String) {
        this.type = type
    }

    fun setMediaType(mediaType: String) {
        this.mediaType = mediaType
    }

    fun getTitle(): String {
        return title
    }

    fun getText(): String {
        return text
    }

    fun getUrl(): String {
        return url
    }

    fun getDocumentTarget(): String {
        return documentTarget
    }

    fun getType(): String {
        return type
    }

    fun getMediaType(): String {
        return mediaType
    }


}