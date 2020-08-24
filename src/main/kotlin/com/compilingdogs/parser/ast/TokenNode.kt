package com.compilingdogs.parser.ast

import com.compilingdogs.lexer.Token


// Holds a token parsed from lexer. A very basic unit of an AST parser.
class TokenNode<T> (
        var token: T
) : ASTNode() where T : Token {
    override fun clone(): ASTNode = TokenNode(token)

    override fun match(): ASTNode? {

    }
}


