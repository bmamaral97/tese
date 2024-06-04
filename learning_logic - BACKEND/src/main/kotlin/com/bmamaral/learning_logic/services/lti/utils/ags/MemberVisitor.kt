package com.bmamaral.learning_logic.services.lti.utils.ags

import edu.uoc.elc.lti.platform.Member
import edu.uoc.elc.spring.lti.tool.NamesRoleServiceProvider
import edu.uoc.elc.spring.lti.tool.ToolProvider

class MemberVisitor(
    val toolProvider: ToolProvider
) {
    fun getAll(): List<Member> {
        val namesRoleServiceProvider: NamesRoleServiceProvider = toolProvider.namesRoleServiceProvider
        return getFromNameServiceProvider(namesRoleServiceProvider)
    }

    fun canGet(): Boolean {
        val namesRoleServiceProvider: NamesRoleServiceProvider = toolProvider.namesRoleServiceProvider
        return namesRoleServiceProvider.hasNameRoleService()
    }

    private fun getFromNameServiceProvider(namesRoleServiceProvider: NamesRoleServiceProvider): List<Member> {
        try {
            return namesRoleServiceProvider.members
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return emptyList()
    }
}