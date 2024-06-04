package com.bmamaral.learning_logic.services.app.utils.normalform

import com.bmamaral.learning_logic.services.app.utils.parser.PLParser
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.TerminalNode

object NFChecker {
    fun checkDNF(source: String): Boolean {
        var isDNF = true

        val visitor = PLParser.visitor(source)
        var ops: MutableList<TerminalNode> = visitor.operators.toMutableList()
        val hasBinaryOps: Boolean = visitor.hasBinaryOps

        val pt: ParseTree = PLParser.parser(source).start().expression()

        var terminalNodeCount = 0
        val range = pt.childCount - 1
        for (i in 0..range) {
            val child = pt.getChild(i)
            if ((child is TerminalNode) && (child.symbol.text == "|")) terminalNodeCount++
            else if ((child is TerminalNode) && (child.symbol.text != "|")) return false
        }

        if (hasBinaryOps) isDNF = false

        ops = ops.drop(terminalNodeCount) as MutableList<TerminalNode>

        for (op in ops) {
            if (op.symbol.text != "&") isDNF = false
        }

        return isDNF
    }

    fun checkCNF(source: String): Boolean {
        var isCNF = true

        val visitor = PLParser.visitor(source)
        var ops: MutableList<TerminalNode> = visitor.operators.toMutableList()
        val hasBinaryOps: Boolean = visitor.hasBinaryOps

        val pt: ParseTree = PLParser.parser(source).start().expression()

        var terminalNodeCount = 0
        val range = pt.childCount - 1
        for (i in 0..range) {
            val child = pt.getChild(i)
            if (child is TerminalNode && child.symbol.text == "&") terminalNodeCount++
            else if (child is TerminalNode && child.symbol.text != "&") return false
        }

        if (hasBinaryOps) isCNF = false

        ops = ops.drop(terminalNodeCount) as MutableList<TerminalNode>

        for (op in ops) {
            if (op.symbol.text != "|") isCNF = false
        }

        return isCNF
    }

    fun checkNNF(source: String): Boolean {
        var isNNF = true

        val visitor = PLParser.visitor(source)
        if (visitor.hasBinaryOps) isNNF = false
        if (!visitor.onlyNegatedLiterals) isNNF = false

        return isNNF
    }

}