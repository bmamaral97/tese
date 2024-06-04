package com.bmamaral.learning_logic.services.lti.utils.beans

import edu.uoc.lti.ags.LineItem

class AgsBean(
    private var available: Boolean,
    private var lineItems: List<LineItem>
) {
    constructor() : this(false, emptyList())

    fun getAvailable(): Boolean {
        return available
    }

    fun setAvailable(available: Boolean) {
        this.available = available
    }

    fun getLineitems(): List<LineItem> {
        return lineItems
    }

    fun setLineitems(lineItems: List<LineItem>) {
        this.lineItems = lineItems
    }
}