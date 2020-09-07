package com.compilingdogs.parser

import com.compilingdogs.parser.ast.*
import tokens.Token


class ArrayLiteral(
    var members: MutableList<FASTNode>
) : FASTNode() {

    override fun clone(): FASTNode {
        return ArrayLiteral(members.copyOf())
    }
}


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

//val arrayLiteral = node {}

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

val arrayLiteral = concat {
    mapTo<ArrayLiteral>()

    +openBracket
    +maybe {
        concat {
            +(expression onSuccess { fastNode: ArrayLiteral, astNode ->
                fastNode.members.add(astNode)
            })
            +repeat {
                +comma
                +(expression onSuccess { fastNode: ArrayLiteral, astNode ->
                    fastNode.members.add(astNode)
                })
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


