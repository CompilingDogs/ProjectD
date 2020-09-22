package com.compilingdogs.parser.ast

import com.compilingdogs.parser.*
import com.compilingdogs.parser.greenColor
import tokens.Token
import java.lang.IllegalStateException


class OptionalNode(
    var node: ASTNode
) : ASTNode() {

    override fun match(tokens: List<Token>, parentNode: FASTNode, depth: Int, enablePrints: Boolean): Int? {
        if (enablePrints && logNodeTraversal)
            println("${indent(depth)}Matching OptionalNode $name; parent is $parentNode")

        // If this node contains its own mapped FASTNode, use it.
        // If not, propagate parent FASTNode instead.
        val fastNode = attachedTo?.newInstance() ?: parentNode

        var res: Int? = 0

        for (n in listOf(fastNode.clone(), fastNode)) {
            res = node.match(tokens, n, depth + 1, enablePrints && System.identityHashCode(n) != System.identityHashCode(fastNode))
            if (res == null)
                return 0
        }

        if (attachedTo != null)
            parentNode.consume(fastNode)

        if (enablePrints)
            println("${indent(depth + 1)}${blueColor}Stopping with parent = $parentNode$noColor")
        return res
    }

    override fun clone(): ASTNode = OptionalNode(node.clone()).also { it.name = name }

    override fun get(name: String): ASTNode? = if (node.name == name) node else null

    override fun toString(): String {
        return "OptionNode($name)"
    }
}

fun maybe(name: String = "", init: () -> ASTNode): OptionalNode = OptionalNode(init()).apply { this.name = name }

