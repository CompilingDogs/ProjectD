package com.compilingdogs.parser.ast

import tokens.Token
import java.lang.IllegalStateException

open class ConcatenationNode(
    val children: MutableList<ASTNode> = ArrayList()
) : ASTNode() {

    operator fun ASTNode.unaryPlus() = children.add(this)


    override fun match(tokens: List<Token>, parentNode: FASTNode?): Pair<Int, FASTNode>? {
        println("Trying to match concatenation node")

        // If this node contains its own mapped FASTNode, use it.
        // If not, propagate parent FASTNode instead.
        val fastNode = attachedTo?.newInstance() ?: parentNode?.clone()
        ?: throw IllegalStateException("No FASTNode provided, and ASTNode is not mapped to any FASTNode")

        // Offset in the token list
        var offset = 0

        var counter = 0
        for (child in children) {
            println("Matching concatenation child ${counter++}")

            // Do creation stuff
            child.createCallback?.invoke(fastNode)

            // Try to match the AST node
            val m = child.match(tokens.subList(offset, tokens.size), fastNode)

            // If child did not match, abort
            if (m == null)
                return null

            offset += m.first

            // If match was successful, fire appropriate callbacks
            child.successCallback?.invoke(fastNode, m.second)
        }

        return Pair(offset, fastNode)
    }

    override fun clone(): ASTNode {
        return ConcatenationNode(children.toMutableList())
    }

    override fun toString(): String {
        return "ConcatenationNode"
    }
}


fun concat(init: ConcatenationNode.() -> Unit): ConcatenationNode = initialize(init)
