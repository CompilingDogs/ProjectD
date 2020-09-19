package com.compilingdogs.parser.ast

import com.compilingdogs.parser.FASTEmptyOptionalNode
import com.compilingdogs.parser.indent
import tokens.Token
import java.lang.IllegalStateException


class OptionalNode(
    var node: ASTNode
) : ASTNode() {

    override fun match(tokens: List<Token>, parentNode: FASTNode?, depth: Int): Pair<Int, FASTNode>? {
        if (logNodeTraversal)
            println("${indent(depth)}Matching OptionalNode $name; parent is $parentNode")

        // If this node contains its own mapped FASTNode, use it.
        // If not, propagate parent FASTNode instead.
        val fastNode = attachedTo?.newInstance() ?: parentNode?.clone()
        ?: throw IllegalStateException("No FASTNode provided, and ASTNode is not mapped to any FASTNode")

        if (logFASTNodes)
            println("${indent(depth)}Node after update: $fastNode")

        // Do creation stuff
        node.createCallback?.invoke(fastNode)

        val res = node.match(tokens, fastNode, depth + 1)

        if (res == null)
            return Pair(0, FASTEmptyOptionalNode())

        // If match was successful, fire appropriate callbacks
        node.successCallback?.invoke(fastNode, res.second)
        if (node.attachedTo != null) {
            println("Adding ${res.second} to $fastNode")
            fastNode.consume(res.second)
            println("Now parent is $fastNode")
        }

        return Pair(res.first, res.second)
    }

    override fun clone(): ASTNode = OptionalNode(node.clone()).also { it.name = name }

    override fun get(name: String): ASTNode? = if (node.name == name) node else null

    override fun toString(): String {
        return "OptionNode($name)"
    }
}

fun maybe(name: String = "", init: () -> ASTNode): OptionalNode = OptionalNode(init()).apply { this.name = name }

