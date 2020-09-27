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

    override fun match(tokens: List<Token>, depth: Int, enablePrints: Boolean): MatchResults {
        if (enablePrints && logNodeTraversal) {
            println("${indent(depth)}Matching TokenNode of type ${nodeType.simpleName}")
            println("${indent(depth + 1)}${lightGray}Tokens: ${tokens.joinToString(" ")}$noColor")
        }

        if (tokens.isNotEmpty() && tokens[0].javaClass == nodeType) {
            // Node matched

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

                return MatchResults(listOf(toConsume), tokens.subList(1, tokens.size), null)
            }
            return MatchResults(listOf(), tokens.subList(1, tokens.size), null)

        } else
            return MatchResults(
                listOf(),
                tokens,
                ParsingError(name, if (tokens.isNotEmpty()) tokens[0].token else "end of file")
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


