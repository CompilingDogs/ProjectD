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

    override fun match(tokens: List<Token>, parentNode: FASTNode?, depth: Int): Pair<Int, FASTNode>? {
        if (logNodeTraversal)
            println("${indent(depth)}Matching AlternationNode $name; parent is $parentNode")

        // If this node contains its own mapped FASTNode, use it.
        // If not, propagate parent FASTNode instead.
        val fastNode = attachedTo?.newInstance() ?: parentNode?.clone()
        ?: throw IllegalStateException("No FASTNode provided, and ASTNode is not mapped to any FASTNode")

        if (logFASTNodes)
            println("${indent(depth)}Node after update: $fastNode")

        for (child in variants) {
//            println("Matching alternation child in $name ${counter++}")

            val fn = fastNode.clone()

            // Do creation stuff
            child.createCallback?.invoke(fn)

            // Try to match the AST nodes
            val m = child.match(tokens, fn, depth + 1)

            // If child did not match, continue
            if (m == null)
                continue

            // If match was successful, fire appropriate callbacks
            child.successCallback?.invoke(fn, m.second)
            if (child.attachedTo != null) {
                println("${indent(depth + 1)}${greenColor}Adding ${m.second} to $fn${noColor}")
                fn.consume(m.second)
                println("${indent(depth + 1)}${greenColor}Now parent is $fn$noColor")
            }

            println("${indent(depth + 1)}Returning $fn")
            return Pair(m.first, fn)
        }

        return null
    }

    override fun clone(): ASTNode = AlternationNode(variants.toMutableList()).also { it.name = name }

    override fun get(name: String): ASTNode? = variants.firstOrNull { it.name == name }

    override fun toString(): String {
        return "AlternationNode($name)"
    }
}

// DSL function to create an empty node. Have no idea why anyone would need this.
fun any(name: String = "", init: AlternationNode.() -> Unit): AlternationNode = initialize(init, name)