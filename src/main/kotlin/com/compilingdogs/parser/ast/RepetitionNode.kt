package com.compilingdogs.parser.ast

import com.compilingdogs.parser.greenColor
import com.compilingdogs.parser.indent
import com.compilingdogs.parser.noColor
import tokens.Token
import java.lang.IllegalStateException


// Used to represent { } from EBNF.
class RepetitionNode(
    val children: MutableList<ASTNode> = ArrayList()
) : ASTNode() {

    operator fun ASTNode.unaryPlus() = children.add(this)

    override fun match(tokens: List<Token>, parentNode: FASTNode?, depth: Int): Pair<Int, FASTNode>? {
        if (logNodeTraversal)
            println("${indent(depth)}Matching RepetitionNode $name; parent is $parentNode")

        // If this node contains its own mapped FASTNode, use it.
        // If not, propagate parent FASTNode instead.
        var fastNode = attachedTo?.newInstance() ?: parentNode?.clone()
        ?: throw IllegalStateException("No FASTNode provided, and ASTNode is not mapped to any FASTNode")

        if (logFASTNodes)
            println("${indent(depth)}Node after update: $fastNode")

        // Offset in the token list
        var offset = 0

        while (true) {
//            println("Matching repetition child in $name ${counter++}")

            val fn = fastNode.clone()

            for (child in children) {
                // Do creation stuff
                child.createCallback?.invoke(fn)

                // Try to match the AST node
                val m = child.match(tokens.subList(offset, tokens.size), fn, depth + 1)

                // If child did not match, abort
                if (m == null) {
                    println("${indent(depth + 1)}Returning $fastNode")
                    return Pair(offset, fastNode)
                }

                offset += m.first

                // If match was successful, fire appropriate callbacks
                child.successCallback?.invoke(fn, m.second)
                if (child.attachedTo != null) {
                    println("${indent(depth + 1)}${greenColor}Adding ${m.second} to $fn$noColor")
                    fn.consume(m.second)
                    println("${indent(depth + 1)}${greenColor}Now parent is $fn$noColor")
                }
            }

            fastNode = fn
        }
    }

    override fun clone(): ASTNode = RepetitionNode(children.toMutableList()).also { it.name = name }

    override fun get(name: String): ASTNode? = children.firstOrNull { it.name == name }

    override fun toString(): String {
        return "RepetitionNode($name)"
    }
}

fun repeat(name: String = "", init: RepetitionNode.() -> Unit): RepetitionNode = initialize(init, name)
