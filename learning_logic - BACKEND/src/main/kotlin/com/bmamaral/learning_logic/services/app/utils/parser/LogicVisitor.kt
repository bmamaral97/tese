package com.bmamaral.learning_logic.services.app.utils.parser

import lexer.pl_lexer
import org.antlr.v4.runtime.RuleContext
import org.antlr.v4.runtime.tree.TerminalNode
import parser.logic_parser
import parser.logic_parserBaseVisitor

class LogicVisitor : logic_parserBaseVisitor<String>() {

    var subFormulas: MutableSet<String> = mutableSetOf()
    var operators: MutableList<TerminalNode> = mutableListOf()
    var hasBinaryOps: Boolean = false
    var onlyNegatedLiterals: Boolean = true

    private lateinit var ruleNames: Array<String>
    private lateinit var source: String

    fun loadParser(parser: logic_parser, source: String) {  //get parser
        this.ruleNames = parser.ruleNames //load parser rules from parser
        this.source = source
    }

    override fun visitStart(ctx: logic_parser.StartContext?): String {
        return if (ctx != null) {
            println("Start context: ${ctx.text}")
            return super.visitStart(ctx) ?: ""
        } else {
            println("Start context is null!")
            ""
        }
    }

    override fun visitExpression(ctx: logic_parser.ExpressionContext?): String {
        return if (ctx != null) {
            println("Expression context: ${ctx.text}")
            for (i in 0 until ctx.childCount) {
                val child = ctx.getChild(i)
                if (child is TerminalNode) {
                    operators.add(child)
                }
            }
            return super.visitExpression(ctx) ?: ""
        } else {
            println("Expression context is null!")
            ""
        }
    }

    override fun visitBidirectional(ctx: logic_parser.BidirectionalContext?): String {
        return if (ctx != null) {
            println("Bidirectional context: ${ctx.text}")
            ctx.children.forEach { element ->
                if (element.text == "=>" || element.text == "<=>") {
                    hasBinaryOps = true
                }
            }
            for (i in 0 until ctx.childCount) {
                val child = ctx.getChild(i)
                if (child is TerminalNode) {
                    operators.add(child)
                }
            }
            return super.visitBidirectional(ctx) ?: ""
        } else {
            println("Bidirectional context is null!")
            ""
        }
    }

    override fun visitConjunction(ctx: logic_parser.ConjunctionContext?): String {
        return if (ctx != null) {
            println("Conjunction context: ${ctx.text}")
            if (ctx.text.length > 1 && ctx.text != source) {
                var subFormula = ctx.text
                if (ctx.start.type == pl_lexer.LPAREN && ctx.stop.type == pl_lexer.RPAREN) {
                    subFormula = subFormula.drop(1)
                    subFormula = subFormula.dropLast(1)
                }
                subFormulas.add(subFormula)
            }

            for (i in 0 until ctx.childCount) {
                val child = ctx.getChild(i)
                if (child is TerminalNode) {
                    operators.add(child)
                }
            }
            return super.visitConjunction(ctx) ?: ""
        } else {
            println("Conjunction context is null!")
            ""
        }
    }

    override fun visitDisjunction(ctx: logic_parser.DisjunctionContext?): String {
        return if (ctx != null) {
            println("Disjunction context: ${ctx.text}")
            for (i in 0 until ctx.childCount) {
                val child = ctx.getChild(i)
                if (child is TerminalNode) {
                    operators.add(child)
                }
            }
            return super.visitDisjunction(ctx) ?: ""
        } else {
            println("Disjunction context is null!")
            ""
        }
    }

    override fun visitAtom(ctx: logic_parser.AtomContext?): String {
        return if (ctx != null) {
            println("Atom context: ${ctx.text}")
            if (ctx.childCount > 1) {
                val ruleName = ruleNames[(ctx.getChild(1) as RuleContext).ruleIndex]
                if (ctx.getStart().text == "~" && ruleName == "atom") {
                    val nextAtom = ctx.getChild(1)
                    if (nextAtom.childCount > 1) onlyNegatedLiterals = false
                }
                if (ctx.text.length > 1 && ctx.text != source) {
                    var subFormula = ctx.text
                    if (ctx.start.type == pl_lexer.LPAREN && ctx.stop.type == pl_lexer.RPAREN) {
                        subFormula = subFormula.drop(1)
                        subFormula = subFormula.dropLast(1)
                    }
                    subFormulas.add(subFormula)
                }
            }
            return super.visitAtom(ctx) ?: ""
        } else {
            println("Atom context is null!")
            ""
        }
    }

    override fun visitVariable(ctx: logic_parser.VariableContext?): String {
        return if (ctx != null) {
            println("Variable context: ${ctx.text}")
            super.visitVariable(ctx) ?: ""
        } else {
            println("Variable context is null!")
            ""
        }
    }
}