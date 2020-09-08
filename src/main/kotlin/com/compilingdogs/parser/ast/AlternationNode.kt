package com.compilingdogs.parser.ast

import tokens.Token


class AlternationNode(
        val variants: MutableList<ASTNode> = ArrayList()
) : ASTNode() {

    operator fun ASTNode.unaryPlus() = variants.add(this)
    override fun match(tokens: List<Token>, parentNode: FASTNode?): Pair<Int, FASTNode>? {
        TODO("Not yet implemented")
    }
}

// DSL function to create an empty node. Have no idea why anyone would need this.
fun any(init: AlternationNode.() -> Unit): AlternationNode = initialize(init)