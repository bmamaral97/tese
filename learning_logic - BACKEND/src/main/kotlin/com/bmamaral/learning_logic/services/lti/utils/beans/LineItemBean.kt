package com.bmamaral.learning_logic.services.lti.utils.beans

import edu.uoc.elc.lti.platform.Member
import edu.uoc.lti.ags.LineItem
import edu.uoc.lti.ags.Result

class LineItemBean(
    val lineItem: LineItem,
    val members: List<Member>,
    val results: List<Result>
) {
}