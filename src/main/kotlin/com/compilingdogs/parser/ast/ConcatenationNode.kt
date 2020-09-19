package com.compilingdogs.parser.ast

import com.compilingdogs.parser.indent
import tokens.Token
import java.lang.IllegalStateException

open class ConcatenationNode(
    val children: MutableList<ASTNode> = ArrayList()
) : ASTNode() {

    operator fun ASTNode.unaryPlus() = children.add(this)


    override fun match(tokens: List<Token>, parentNode: FASTNode?, depth: Int): Pair<Int, FASTNode>? {
        if (logNodeTraversal)
            println("${indent(depth)}Matching ConcatenationNode $name; parent is $parentNode")

        // If this node contains its own mapped FASTNode, use it.
        // If not, propagate parent FASTNode instead.
        val fastNode = attachedTo?.newInstance() ?: parentNode?.clone()
        ?: throw IllegalStateException("No FASTNode provided, and ASTNode is not mapped to any FASTNode")

        if (logFASTNodes)
            println("${indent(depth)}Node after update: $fastNode")

        // Offset in the token list
        var offset = 0

        for (child in children) {
//            println("Matching concatenation child in $name ${counter++}")

            // Do creation stuff
            child.createCallback?.invoke(fastNode)

            // Try to match the AST node
            val m = child.match(tokens.subList(offset, tokens.size), fastNode, depth + 1)

            // If child did not match, abort
            if (m == null)
                return null

            offset += m.first

            if (child.successCallback != null) {
                println("fastNode before callback: $fastNode")
                // If match was successful, fire appropriate callbacks
                child.successCallback?.invoke(fastNode, m.second)
                if (child.attachedTo != null)
                    fastNode.consume(m.second)
                println("fastNode after callback: $fastNode")
            }
        }

        return Pair(offset, fastNode)
    }

    override fun clone(): ASTNode = ConcatenationNode(children.toMutableList()).also { it.name = name }

    override fun get(name: String): ASTNode? = children.firstOrNull { it.name == name }

    override fun toString(): String {
        return "ConcatenationNode($name)"
    }
}


fun concat(name: String = "", init: ConcatenationNode.() -> Unit): ConcatenationNode = initialize(init, name)
