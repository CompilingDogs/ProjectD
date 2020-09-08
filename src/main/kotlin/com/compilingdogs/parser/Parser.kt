package com.compilingdogs.parser

import com.compilingdogs.parser.ast.*
import stages.LexicalAnalyzer
import tokens.Keyword
import tokens.Literal
import tokens.Separator
import tokens.Token
import java.io.File


// Delimiters
val openBrace = TokenNode(Separator.OpeningCurlyBracketSeparator::class.java)
val closeBrace = TokenNode(Separator.ClosingCurlyBracketSeparator::class.java)
val openBracket = TokenNode(Separator.OpeningBracketSeparator::class.java)
val closeBracket = TokenNode(Separator.ClosingBracketSeparator::class.java)
val comma = TokenNode(Separator.CommaSeparator::class.java)

// Literals
// TODO: replace these with meaningful definitions
//val integerLiteral = TokenNode<IntegerLiteralToken>
val integerLiteral = TokenNode(Literal.IntegerLiteral::class.java)
val realLiteral = TokenNode(Literal.RealLiteral::class.java)
val stringLiteral = TokenNode(Literal.StringLiteral::class.java)
val booleanLiteral = TokenNode(Keyword.BoolKeyword::class.java)
val emptyLiteral = TokenNode(Literal.EmptyLiteral::class.java)


//val tupleElement = node {}
//val tupleLiteral = node {}


// Expressions
val expression: AlternationNode = any {
    +literal
}


val literal: AlternationNode = any {
    +integerLiteral
    +realLiteral
    +stringLiteral
    +booleanLiteral
    +arrayLiteral
//    +tupleLiteral
    +emptyLiteral
}

val arrayLiteral: ConcatenationNode = concat {
    mapTo<ArrayLiteral>()

    +openBracket
    +maybe {
        concat {
            +(expression onSuccess { fastNode: ArrayLiteral, nextFastNode ->
                fastNode.members.add(nextFastNode)
            })
            +repeat {
                +comma
                +(expression onSuccess { fastNode: ArrayLiteral, nextFastNode ->
                    fastNode.members.add(nextFastNode)
                })
            }
        }
    }
    +closeBracket
}

val testRoot = arrayLiteral


fun main() {
    runTest()
}


fun runTest() {
    val path = "src/test/resources/maxim_test_1.pd"
    val lexer = LexicalAnalyzer.getInstance()
    val tokens = lexer.tokenize(File(path))
    parse(tokens)
}

fun parse(tokens: List<Token>) {
    val node = testRoot.match(tokens, null)
    print(node)
}


