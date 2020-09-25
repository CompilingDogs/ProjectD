package com.compilingdogs.parser.ast

import com.compilingdogs.parser.FASTToken
import com.compilingdogs.parser.indent
import com.compilingdogs.parser.lightGray
import com.compilingdogs.parser.noColor
import tokens.Token
import java.lang.Error
import java.lang.reflect.Constructor


// Holds a token parsed from lexer. A very basic unit of an AST parser.
class TokenNode<T>(
    val nodeType: Class<T>,
    val shouldBeReturned: Boolean = false,
) : ASTNode() where T : Token {

    override fun match(tokens: List<Token>, parentNode: FASTNode, depth: Int, enablePrints: Boolean): MatchResults {
        if (enablePrints && logNodeTraversal) {
            println("${indent(depth)}Matching TokenNode of type ${nodeType.simpleName}; parent is $parentNode")
            println("${indent(depth + 1)}${lightGray}Tokens: ${tokens.joinToString(" ")}$noColor")
        }

        if (tokens.isNotEmpty() && tokens[0].javaClass == nodeType) {
            if (shouldBeReturned) {
                val toConsume = if (attachedTo != null) {
                    val oneArgNode =
                        attachedTo?.constructors?.firstOrNull { c -> c.parameters.size == 1 && c.parameters[0].type == Token::class.java }
                            ?.newInstance(tokens[0] as T) as FASTNode?
                    oneArgNode
                        ?: attachedTo?.constructors?.firstOrNull { c -> c.parameters.isEmpty() }
                            ?.newInstance() as FASTNode
                } else {
                    FASTToken(tokens[0] as T)
                }

                parentNode.consume(toConsume.apply {
                    if (enablePrints && logFASTTokens)
                        println("Matched ${tokens[0]} to $this")
                })
            }
            return MatchResults(1, null)
        } else
            return MatchResults(null,
                if (tokens.isNotEmpty())
                    Error("Expected $name, got \"${tokens[0]}\"")
                else
                    Error("Expected $name, got end of file")
            )
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


