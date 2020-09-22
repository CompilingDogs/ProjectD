package com.compilingdogs.parser.ast

import com.compilingdogs.parser.greenColor
import com.compilingdogs.parser.indent
import com.compilingdogs.parser.magentaColor
import com.compilingdogs.parser.noColor
import tokens.Token
import java.lang.IllegalStateException


// Used to represent { } from EBNF.
class RepetitionNode(
    val children: MutableList<ASTNode> = ArrayList()
) : ASTNode() {

    operator fun ASTNode.unaryPlus() = children.add(this)

    override fun match(tokens: List<Token>, parentNode: FASTNode, depth: Int, enablePrints: Boolean): Int? {
        if (enablePrints && logNodeTraversal) {
            println("${indent(depth)}Matching RepetitionNode $name; parent is $parentNode")
            println("${indent(depth)}Tokens: ${tokens.joinToString(" ")}")
        }

        // If this node contains its own mapped FASTNode, use it.
        // If not, propagate parent FASTNode instead.
        val fastNode = attachedTo?.newInstance() ?: parentNode

        // Offset in the token list
        var offset = 0

        while (true) {
            var tmpOffset = 0

            for (node in listOf(fastNode.clone(), fastNode)) {
                var localOffset = 0

                for (child in children) {
                    // Try to match the AST node
                    val res = child.match(
                        transformTokens(tokens.subList(offset + localOffset, tokens.size)),
                        node,
                        depth + 1,
                        enablePrints && System.identityHashCode(node) != System.identityHashCode(fastNode)
                    )

                    // If child did not match, abort
                    if (res == null) {
                        if (attachedTo != null)
                            parentNode.consume(fastNode)

                        if (enablePrints)
                            println("${indent(depth + 1)}${magentaColor}Stopping $name with parent = $parentNode$noColor")
                        return offset
                    }

                    localOffset += res
                }

                tmpOffset = localOffset
            }

            offset += tmpOffset
        }
    }

    override fun clone(): ASTNode = RepetitionNode(children.toMutableList()).also { it.name = name }

    override fun get(name: String): ASTNode? = children.firstOrNull { it.name == name }

    override fun toString(): String {
        return "RepetitionNode($name)"
    }
}

fun repeat(name: String = "", init: RepetitionNode.() -> Unit): RepetitionNode = initialize(init, name)
