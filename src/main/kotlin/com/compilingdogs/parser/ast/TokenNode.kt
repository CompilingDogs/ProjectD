package com.compilingdogs.parser.ast

import com.compilingdogs.parser.FASTToken
import tokens.Token
import kotlin.reflect.typeOf


// Holds a token parsed from lexer. A very basic unit of an AST parser.
class TokenNode<T>(
    val nodeType: Class<T>
) : ASTNode() where T : Token {

    override fun match(tokens: List<Token>, parentNode: FASTNode?): Pair<Int, FASTNode>? {
//        println("Trying to match token node of type ${nodeType.simpleName}")

        return if (tokens.isNotEmpty() && tokens[0].javaClass == nodeType)
            Pair(1, FASTToken(tokens[0] as T)).apply { println("Matched ${tokens[0]} to $second") }
        else
            null
    }

    override fun clone(): ASTNode {
        return TokenNode(nodeType)
    }

    override fun get(name: String): ASTNode? {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "TokenNode(${nodeType.simpleName})"
    }
}


