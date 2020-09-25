package com.compilingdogs.parser.ast

import com.compilingdogs.parser.*
import tokens.Token
import java.lang.IllegalStateException

open class ConcatenationNode(
    val children: MutableList<ASTNode> = ArrayList()
) : ASTNode() {

    operator fun ASTNode.unaryPlus() = children.add(this)


    override fun match(tokens: List<Token>, parentNode: FASTNode, depth: Int, enablePrints: Boolean): MatchResults {
        if (enablePrints && logNodeTraversal) {
            println("${indent(depth)}Matching ConcatenationNode $name; parent is $parentNode")
            println("${indent(depth + 1)}${lightGray}Tokens: ${tokens.joinToString(" ")}${noColor}")
        }

        // If this node contains its own mapped FASTNode, use it.
        // If not, propagate parent FASTNode instead.
        val fastNode = attachedTo?.newInstance() ?: parentNode

        // Offset in the token list
        var offset = 0

        // First try applying on the clone node, then on the real one
        for (node in listOf(fastNode.clone(), fastNode)) {
            offset = 0

            for (child in children) {
                // Try to match the AST node
                val m = child.match(transformTokens(tokens.subList(offset, tokens.size)),
                    node, depth + 1, enablePrints && System.identityHashCode(node) != System.identityHashCode(fastNode))

                // If child did not match, abort and propagate the error up
                if (m.error != null) return m

                offset += m.parsedTokens!!
            }
        }

        if (attachedTo != null)
            parentNode.consume(fastNode)

        if (enablePrints)
            println("${indent(depth + 1)}${yellowColor}Stopping $name with parent = $parentNode$noColor")
        return MatchResults(offset, null)
    }

    override fun clone(): ASTNode = ConcatenationNode(children.toMutableList()).also { it.name = name }

    override fun get(name: String): ASTNode? = children.firstOrNull { it.name == name }

    override fun toString(): String {
        return "ConcatenationNode($name)"
    }
}


fun concat(name: String = "", init: ConcatenationNode.() -> Unit): ConcatenationNode = initialize(init, name)
