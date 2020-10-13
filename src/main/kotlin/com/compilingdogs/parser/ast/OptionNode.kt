package com.compilingdogs.parser.ast

import com.compilingdogs.parser.blueColor
import com.compilingdogs.parser.indent
import com.compilingdogs.parser.lightGray
import com.compilingdogs.parser.noColor
import tokens.Token
import java.lang.Error


class OptionalNode(
    var node: ASTNode
) : ASTNode() {

    override fun match(tokens: List<Token>, depth: Int): MatchResults {
        // Debugging stuff.
        if (logNodeTraversal) {
            println("${indent(depth)}Matching OptionalNode $name")
            println("${indent(depth + 1)}${lightGray}Tokens: ${tokens.joinToString(" ")}${noColor}")
        }

        // If this node contains its own mapped FASTNode, use it.
        val fastNode = attachedTo?.newInstance()

        val res = node.match(tokens, depth + 1)

        // Check if parsing failed. Do nothing in this case.
        if (res.error != null)
            return MatchResults(listOf(), tokens, null)

        if (fastNode != null)
            res.result.forEach { node -> fastNode.consume(node) }

        val result =
            if (fastNode != null)
                listOf(fastNode)
            else
                res.result

        // Debugging stuff.
        if (logNodeTraversal)
            println("${indent(depth + 1)}${blueColor}Stopping $name${noColor} with result $result")

        return MatchResults(
            result,
            res.remainingTokens,
            null
        )
    }

    override fun clone(): ASTNode = OptionalNode(node.clone()).also { it.name = name }

    override fun get(name: String): ASTNode? = if (node.name == name) node else null

    override fun toString(): String {
        return "OptionNode($name)"
    }
}

/**
 * Creates a node that may be present, but is not necessary.
 * If such node fails to match, nothing happens, it is just being skipped.
 */
fun maybe(name: String = "", init: () -> ASTNode): OptionalNode = OptionalNode(init()).apply { this.name = name }

