package com.compilingdogs.parser.ast


class AlternationNode(
        val variants: MutableList<ASTNode> = ArrayList()
) : ASTNode() {

    operator fun ASTNode.unaryPlus() = variants.add(this)
}

// DSL function to create an empty node. Have no idea why anyone would need this.
fun any(init: AlternationNode.() -> Unit): AlternationNode = initialize(init)