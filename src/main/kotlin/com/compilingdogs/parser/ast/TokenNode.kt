package com.compilingdogs.parser.ast

import com.compilingdogs.parser.FASTToken
import com.compilingdogs.parser.indent
import tokens.Token


// Holds a token parsed from lexer. A very basic unit of an AST parser.
class TokenNode<T>(
    val nodeType: Class<T>,
    val shouldBeReturned: Boolean = false,
) : ASTNode() where T : Token {

    override fun match(tokens: List<Token>, parentNode: FASTNode, depth: Int): Int? {
        if (logNodeTraversal)
            println("${indent(depth)}Matching TokenNode of type ${nodeType.simpleName}; parent is $parentNode")

        if (tokens.isNotEmpty() && tokens[0].javaClass == nodeType) {
            if (shouldBeReturned) {
                parentNode.consume(FASTToken(tokens[0] as T).apply {
                    if (logFASTTokens)
                        println("Matched ${tokens[0]} to $this")
                })
            }
            return 1
        } else
            return null
    }

    override fun clone(): ASTNode {
        return TokenNode(nodeType, shouldBeReturned)
    }

    override fun get(name: String): ASTNode? {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "TokenNode(${nodeType.simpleName})"
    }
}


