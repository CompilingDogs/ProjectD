package com.compilingdogs.parser.ast


open class ConcatenationNode(
        val children: MutableList<ASTNode> = ArrayList()
) : ASTNode() {

    operator fun ASTNode.unaryPlus() = children.add(this)
    override fun clone(): ASTNode {
        TODO("Not yet implemented")
    }

    override fun match(): ASTNode? {
        TODO("Not yet implemented")
    }
}

fun concat(init: ConcatenationNode.() -> Unit): ConcatenationNode = initialize(init)