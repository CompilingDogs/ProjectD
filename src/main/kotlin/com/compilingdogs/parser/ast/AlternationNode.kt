package com.compilingdogs.parser.ast

import com.compilingdogs.parser.greenColor
import com.compilingdogs.parser.indent
import com.compilingdogs.parser.lightGray
import com.compilingdogs.parser.noColor
import tokens.Token
import java.lang.IllegalStateException


class AlternationNode(
    val variants: MutableList<ASTNode> = ArrayList()
) : ASTNode() {

    operator fun ASTNode.unaryPlus() = variants.add(this)

    override fun match(tokens: List<Token>, depth: Int, enablePrints: Boolean): MatchResults {
        if (enablePrints && logNodeTraversal) {
            println("${indent(depth)}Matching AlternationNode $name")
            println("${indent(depth + 1)}${lightGray}Tokens: ${tokens.joinToString(" ")}${noColor}")
        }

        // If this node contains its own mapped FASTNode, use it.
        val fastNode = attachedTo?.newInstance()

        // Initialize result with empty cases error
        var result = MatchResults(listOf(), tokens, ParsingError(name, "empty cases in AlternationNode"))

        if (variants.size == 0)
            return result

        for (child in variants) {
            // Try to match the AST nodes
//            result = child.match(transformTokens(tokens), depth + 1, enablePrints)
            result = child.match(tokens, depth + 1, enablePrints)

            // If child did not match, continue. Otherwise - stop matching
            if (result.error != null)
                continue
            else
                break
        }

        if (result.error != null)
            return MatchResults(listOf(), tokens, ParsingError(name, "No matching cases in AlternationNode"))

        // TODO: remake to consume a list of tokens
        result.result.forEach { node -> fastNode?.consume(node) }

        if (enablePrints) {
            println("${indent(depth + 1)}${greenColor}Stopping $name${noColor}")
        }

        return MatchResults(
            if (fastNode == null)
                result.result
            else
                listOf(fastNode),
            result.remainingTokens,
            null
        )
    }

    override fun clone(): ASTNode = AlternationNode(variants.toMutableList()).also { it.name = name }

    override fun get(name: String): ASTNode? = variants.firstOrNull { it.name == name }

    override fun toString(): String {
        return "AlternationNode($name)"
    }
}

// DSL function to create an empty node. Have no idea why anyone would need this.
fun any(name: String = "", init: AlternationNode.() -> Unit): AlternationNode = initialize(init, name)