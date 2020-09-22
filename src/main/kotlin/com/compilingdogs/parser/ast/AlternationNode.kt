package com.compilingdogs.parser.ast

import com.compilingdogs.parser.greenColor
import com.compilingdogs.parser.indent
import com.compilingdogs.parser.noColor
import tokens.Token
import java.lang.IllegalStateException


class AlternationNode(
    val variants: MutableList<ASTNode> = ArrayList()
) : ASTNode() {

    operator fun ASTNode.unaryPlus() = variants.add(this)

    override fun match(tokens: List<Token>, parentNode: FASTNode, depth: Int, enablePrints: Boolean): Int? {
        if (enablePrints && logNodeTraversal)
            println("${indent(depth)}Matching AlternationNode $name; parent is $parentNode")

        // If this node contains its own mapped FASTNode, use it.
        // If not, propagate parent FASTNode instead.
        val fastNode = attachedTo?.newInstance() ?: parentNode


        var parsedLen = -1

        for (child in variants) {
            val fn = fastNode.clone()

            // Try to match the AST nodes
            val m = child.match(tokens, fn, depth + 1, enablePrints)

            // If child did not match, continue
            if (m == null) continue

            // If match was successful, apply the same on the real parent/fastNode
            parsedLen = child.match(tokens, fastNode, depth + 1, false)!!

            break
        }

        if (parsedLen == -1)
            return null

        if (attachedTo != null)
            parentNode.consume(fastNode)

        if (enablePrints)
            println("${indent(depth + 1)}${greenColor}Stopping with parent = $parentNode$noColor")
        return parsedLen
    }

    override fun clone(): ASTNode = AlternationNode(variants.toMutableList()).also { it.name = name }

    override fun get(name: String): ASTNode? = variants.firstOrNull { it.name == name }

    override fun toString(): String {
        return "AlternationNode($name)"
    }
}

// DSL function to create an empty node. Have no idea why anyone would need this.
fun any(name: String = "", init: AlternationNode.() -> Unit): AlternationNode = initialize(init, name)