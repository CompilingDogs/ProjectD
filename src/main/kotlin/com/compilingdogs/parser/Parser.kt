package com.compilingdogs.parser

import com.compilingdogs.lexer.Token
import com.compilingdogs.parser.ast.*


class ArrayNode : ConcatenationNode()


// Delimiters
val openBrace = node {}
val closeBrace = node {}
val openBracket = node {}
val closeBracket = node {}
val comma = node {}

// Literals
// TODO: replace these with meaningful definitions
//val integerLiteral = TokenNode<IntegerLiteralToken>
val integerLiteral = node {}
val realLiteral = node {}
val stringLiteral = node {}
val booleanLiteral = node {}
val emptyLiteral = node {}

val arrayLiteral = node {}

val tupleElement = node {}
val tupleLiteral = node {}


// Expressions
val expression = node {}



val literal = any {
    +integerLiteral
    +realLiteral
    +stringLiteral
    +booleanLiteral
    +arrayLiteral
    +tupleLiteral
    +emptyLiteral
}

val array = concat {
    mapTo<ArrayNode>()

    +openBracket
    +maybe {
        concat {
            +expression
            +repeat {
                +comma
                +expression
            }
        }
    }
    +closeBracket
}


fun parse(tokens: List<Token>) {
//    tokens.forEach { token ->
//        if (token is IfKeyword) {
//        }
//    }


}