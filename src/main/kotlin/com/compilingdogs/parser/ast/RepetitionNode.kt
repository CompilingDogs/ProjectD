package com.compilingdogs.parser.ast

import com.compilingdogs.parser.*
import tokens.Token
import java.lang.IllegalStateException


// Used to represent { } from EBNF.
class RepetitionNode(
    val children: MutableList<ASTNode> = ArrayList()
) : ASTNode() {

    operator fun ASTNode.unaryPlus() = children.add(this)

    override fun match(tokens: List<Token>, depth: Int, enablePrints: Boolean): MatchResults {
        if (enablePrints && logNodeTraversal) {
            println("${indent(depth)}Matching RepetitionNode $name")
            println("${indent(depth + 1)}${lightGray}Tokens: ${tokens.joinToString(" ")}${noColor}")
        }

        // If this node contains its own mapped FASTNode, use it.
        val fastNode = attachedTo?.newInstance()

        // Offset in the token list
        val results = mutableListOf<FASTNode>()
        var lastSuccessfulRemainingTokens = tokens

        while (true) {
            for (child in children) {
                // Try to match the AST node
                val res = child.match(
                    lastSuccessfulRemainingTokens,
                    depth + 1,
                    enablePrints
                )

                // If child did not match, abort
                if (res.error != null) {
                    if (fastNode != null)
                        res.result.forEach { node -> fastNode.consume(node) }

                    if (enablePrints)
                        println("${indent(depth + 1)}${magentaColor}Stopping $name${noColor}")

                    return MatchResults(
                        if (fastNode != null)
                            listOf(fastNode)
                        else
                            results,
                        lastSuccessfulRemainingTokens,
                        null
                    )
                }

                if (fastNode != null)
                    res.result.forEach { node -> fastNode.consume(node) }
                else
                    results.addAll(res.result)

                lastSuccessfulRemainingTokens = res.remainingTokens
            }
        }
    }

    override fun clone(): ASTNode = RepetitionNode(children.toMutableList()).also { it.name = name }

    override fun get(name: String): ASTNode? = children.firstOrNull { it.name == name }

    override fun toString(): String {
        return "RepetitionNode($name)"
    }
}

fun repeat(name: String = "", init: RepetitionNode.() -> Unit): RepetitionNode = initialize(init, name)
