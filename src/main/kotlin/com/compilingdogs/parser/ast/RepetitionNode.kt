package com.compilingdogs.parser.ast


// Used to represent { } from EBNF.
class RepetitionNode(
        val children: MutableList<ASTNode> = ArrayList()
) : ASTNode() {

    operator fun ASTNode.unaryPlus() = children.add(this)
}

fun repeat(init: RepetitionNode.() -> Unit): RepetitionNode = initialize(init)