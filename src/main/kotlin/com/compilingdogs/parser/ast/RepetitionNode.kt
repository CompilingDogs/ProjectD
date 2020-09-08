package com.compilingdogs.parser.ast

import tokens.Token


// Used to represent { } from EBNF.
class RepetitionNode(
        val children: MutableList<ASTNode> = ArrayList()
) : ASTNode() {

    operator fun ASTNode.unaryPlus() = children.add(this)

    override fun match(tokens: List<Token>, parentNode: FASTNode?): Pair<Int, FASTNode>? {
        TODO("Not yet implemented")
    }
}

fun repeat(init: RepetitionNode.() -> Unit): RepetitionNode = initialize(init)