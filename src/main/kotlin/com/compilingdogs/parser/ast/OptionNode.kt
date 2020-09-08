package com.compilingdogs.parser.ast

import tokens.Token
import java.lang.IllegalStateException


class OptionalNode(
        var node: ASTNode
) : ASTNode() {

    override fun match(tokens: List<Token>, parentNode: FASTNode?): Pair<Int, FASTNode>? {
        println("Trying to match option node")

        // If this node contains its own mapped FASTNode, use it.
        // If not, propagate parent FASTNode instead.
        val fastNode = attachedTo?.newInstance() ?: parentNode?.clone()
        ?: throw IllegalStateException("No FASTNode provided, and ASTNode is not mapped to any FASTNode")

        // Do creation stuff
        node.createCallback?.invoke(fastNode)

        val res = node.match(tokens, fastNode)

        if (res == null)
            return null

        // If match was successful, fire appropriate callbacks
        node.successCallback?.invoke(fastNode, res.second)

        return Pair(res.first, fastNode)

    }

    override fun clone(): ASTNode {
        return OptionalNode(node.clone())
    }

    override fun toString(): String {
        return "OptionNode"
    }
}

fun maybe(init: () -> ASTNode): OptionalNode = OptionalNode(init())