package com.compilingdogs.parser.ast

import tokens.Token
import java.lang.IllegalStateException


class AlternationNode(
    val variants: MutableList<ASTNode> = ArrayList()
) : ASTNode() {

    operator fun ASTNode.unaryPlus() = variants.add(this)

    override fun match(tokens: List<Token>, parentNode: FASTNode?): Pair<Int, FASTNode>? {
        println("Matching AlternationNode $name")

        // If this node contains its own mapped FASTNode, use it.
        // If not, propagate parent FASTNode instead.
        val fastNode = attachedTo?.newInstance() ?: parentNode?.clone()
        ?: throw IllegalStateException("No FASTNode provided, and ASTNode is not mapped to any FASTNode")

        for (child in variants) {
//            println("Matching alternation child in $name ${counter++}")

            val fn = fastNode.clone()

            // Do creation stuff
            child.createCallback?.invoke(fn)

            // Try to match the AST nodes
            val m = child.match(tokens, fn)

            // If child did not match, continue
            if (m == null)
                continue

            // If match was successful, fire appropriate callbacks
            child.successCallback?.invoke(fn, m.second)

            return Pair(m.first, m.second)
        }

        return null
    }

    override fun clone(): ASTNode {
        return AlternationNode(variants.toMutableList())
    }

    override fun get(name: String): ASTNode? = variants.firstOrNull { it.name == name }

    override fun toString(): String {
        return "AlternationNode($name)"
    }
}

// DSL function to create an empty node. Have no idea why anyone would need this.
fun any(name: String = "", init: AlternationNode.() -> Unit): AlternationNode = initialize(init, name)