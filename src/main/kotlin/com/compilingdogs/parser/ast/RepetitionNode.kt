package com.compilingdogs.parser.ast

import tokens.Token
import java.lang.IllegalStateException


// Used to represent { } from EBNF.
class RepetitionNode(
    val children: MutableList<ASTNode> = ArrayList()
) : ASTNode() {

    operator fun ASTNode.unaryPlus() = children.add(this)

    override fun match(tokens: List<Token>, parentNode: FASTNode?): Pair<Int, FASTNode>? {
//        println("Trying to match repetition node")

        // If this node contains its own mapped FASTNode, use it.
        // If not, propagate parent FASTNode instead.
        var fastNode = attachedTo?.newInstance() ?: parentNode?.clone()
        ?: throw IllegalStateException("No FASTNode provided, and ASTNode is not mapped to any FASTNode")

        // Offset in the token list
        var offset = 0

        var counter = 0
        while (true) {
//            println("Matching repetition child in $name ${counter++}")

            val ft = fastNode.clone()

            for (child in children) {
                // Do creation stuff
                child.createCallback?.invoke(ft)

                // Try to match the AST node
                val m = child.match(tokens.subList(offset, tokens.size), ft)

                // If child did not match, abort
                if (m == null)
                    return Pair(offset, fastNode)

                offset += m.first

                // If match was successful, fire appropriate callbacks
                child.successCallback?.invoke(ft, m.second)
            }

            fastNode = ft
        }
    }

    override fun clone(): ASTNode {
        return RepetitionNode(children.toMutableList())
    }

    override fun get(name: String): ASTNode? = children.firstOrNull { it.name == name }

    override fun toString(): String {
        return "RepetitionNode($name)"
    }
}

fun repeat(name: String = "", init: RepetitionNode.() -> Unit): RepetitionNode = initialize(init, name)
