package com.compilingdogs.parser.ast

import com.compilingdogs.parser.greenColor
import com.compilingdogs.parser.indent
import com.compilingdogs.parser.lightGray
import com.compilingdogs.parser.noColor
import tokens.Token
import java.util.stream.Stream


class AlternationNode(
    val variants: MutableList<ASTNode> = ArrayList()
) : ASTNode() {

    operator fun ASTNode.unaryPlus() = variants.add(this)

    override fun match(tokens: List<Token>, depth: Int): MatchResults {
        if (logNodeTraversal) {
            println("${indent(depth)}Matching AlternationNode $name")
            println("${indent(depth + 1)}${lightGray}Tokens: ${tokens.joinToString(" ")}${noColor}")
        }

        // If this node contains its own mapped FASTNode, use it.
        val fastNode = attachedTo?.newInstance()

        // Initialize result with empty cases error
        var maxParsedResult = MatchResults(listOf(), tokens, ParsingError(name, "empty cases in AlternationNode"))

        if (variants.size == 0)
            return maxParsedResult

        var children = variants.parallelStream().map { child ->
            // Try to match the AST nodes
            val result = child.match(tokens, depth + 1)

            // If child did not match, continue. Otherwise - stop matching
            // also, continue if no tokens were parsed
            if (result.error != null) {
                return@map MatchResults(listOf(), tokens, result.error)
            } else {
            }

            return@map result
        }
        children = Stream.concat(listOf(maxParsedResult).stream(), children)

        maxParsedResult = children.reduce { acc, res ->
            if (res == null)
                return@reduce acc

            val len1 = tokens.size - acc!!.remainingTokens.size
            val len2 = tokens.size - res.remainingTokens.size

            if (len2 > len1) {
                return@reduce res
            } else {
                return@reduce acc
            }
        }.get()

        if (maxParsedResult.error != null)
            return MatchResults(listOf(), tokens, ParsingError(name, "No matching cases in AlternationNode"))

        // TODO: remake to consume a list of tokens
        // Only consume results to attached fast node if it is attached
        if (fastNode != null)
            maxParsedResult.result.forEach { node -> fastNode.consume(node) }

        val result =
            if (fastNode == null)
                maxParsedResult.result
            else
                listOf(fastNode)

        if (logNodeTraversal) {
            println("${indent(depth + 1)}${greenColor}Stopping $name${noColor} with result $result")
        }

        return MatchResults(
            result,
            maxParsedResult.remainingTokens,
            null
        )
    }

    override fun clone(): ASTNode = AlternationNode(variants.toMutableList()).also { it.name = name }

    override fun get(name: String): ASTNode? = variants.firstOrNull { it.name == name }

    override fun toString(): String {
        return "AlternationNode($name)"
    }
}

/**
 * Creates a node that should match at least one of the children.
 * The first occurrence is taken, other ones are ignored.
 * If no child matches, this node fails to match as well.
 */
fun any(name: String = "", init: AlternationNode.() -> Unit): AlternationNode = initialize(init, name)