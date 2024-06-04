package com.bmamaral.learning_logic.services.app.utils.parser

import lexer.pl_lexer
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.misc.ParseCancellationException
import parser.logic_parser

object PLParser {

    fun parser(source: String): logic_parser {
        val charStream = CharStreams.fromString(source)
        val lexer = pl_lexer(charStream)
        lexer.removeErrorListeners()
        lexer.addErrorListener(ThrowingErrorListener())
        val tokens = CommonTokenStream(lexer)
        val parser = logic_parser(tokens)
        parser.removeErrorListeners()
        parser.addErrorListener(ThrowingErrorListener())
        return parser
    }

    fun parse(source: String): Boolean {
        if (source === "") return true
        return try {
            parser(source).start()
            true
        } catch (e: ParseCancellationException) {
            false
        }
    }

    fun visitor(source: String): LogicVisitor {
        val parser = parser(source)
        val visitor = LogicVisitor()
        visitor.loadParser(parser, source)
        visitor.visit(parser.start())
        return visitor
    }

    fun getSubFormulas(source: String): List<String> {
        val visitor = visitor(source)
        val subFormulas = visitor.subFormulas
        subFormulas.add(source)
        return subFormulas.toList()
    }

}