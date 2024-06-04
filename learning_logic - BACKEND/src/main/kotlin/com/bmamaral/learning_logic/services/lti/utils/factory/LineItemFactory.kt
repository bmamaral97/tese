package com.bmamaral.learning_logic.services.lti.utils.factory;

import edu.uoc.lti.ags.LineItem
import edu.uoc.lti.ags.Submission
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

class LineItemFactory {
    fun newLineItem(label: String, maxScore: Double, tag: String, resourceLinkId: String): LineItem {
        val lineItem = LineItem()
        lineItem.label = label
        lineItem.scoreMaximum = maxScore
        lineItem.tag = tag
        lineItem.resourceLinkId = resourceLinkId
        val submission = Submission()
        val instant: Instant = Instant.now()
        val endInstant: Instant = instant.plus(7, ChronoUnit.DAYS)
        submission.setStartDateTime(instant)
        submission.setEndDateTime(endInstant)
        lineItem.submission = submission
        lineItem.resourceId = UUID.randomUUID().toString()
        return lineItem
    }
}