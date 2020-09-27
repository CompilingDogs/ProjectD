package com.compilingdogs.parser.ast

import com.compilingdogs.parser.*
import tokens.Token
import java.lang.IllegalStateException

open class ConcatenationNode(
    val children: MutableList<ASTNode> = ArrayList()
) : ASTNode() {

    operator fun ASTNode.unaryPlus() = children.add(this)


    override fun match(tokens_: List<Token>, depth: Int, enablePrints: Boolean): MatchResults {
        if (enablePrints && logNodeTraversal) {
            println("${indent(depth)}Matching ConcatenationNode $name")
            println("${indent(depth + 1)}${lightGray}Tokens: ${tokens_.joinToString(" ")}${noColor}")
        }

        // Create a separate mutable tokens variable to alter it during parsing
        var tokens = tokens_

        // If this node contains its own mapped FASTNode, use it.
        val fastNode = attachedTo?.newInstance()
        // In case fastNode is undefined, results are stored in this list instead
        var results = mutableListOf<FASTNode>()

        var result = MatchResults(listOf(), tokens, ParsingError(name, "Empty cases in ConcatenationNode"))

        if (children.size == 0)
            return result

        for (child in children) {
            // Try to match the AST node
            result = child.match(
//                    transformTokens(tokens.subList(offset, tokens.size)),
                tokens,
                depth + 1,
                enablePrints
            )

            // If child did not match, abort and propagate the error up
            if (result.error != null) return result

            if (fastNode != null)
                result.result.forEach { node -> fastNode.consume(node) }
            else
            // TODO: think about this
                results.addAll(result.result)

            val parsedTokensSize = tokens.size - result.remainingTokens.size
            tokens = tokens.subList(parsedTokensSize, tokens.size)
        }

        if (enablePrints)
            println("${indent(depth + 1)}${yellowColor}Stopping $name")

        return MatchResults(
            if (fastNode != null)
                listOf(fastNode)
            else
                results,
            tokens,
            null
        )
    }

    override fun clone(): ASTNode = ConcatenationNode(children.toMutableList()).also { it.name = name }

    override fun get(name: String): ASTNode? = children.firstOrNull { it.name == name }

    override fun toString(): String {
        return "ConcatenationNode($name)"
    }
}


fun concat(name: String = "", init: ConcatenationNode.() -> Unit): ConcatenationNode = initialize(init, name)
