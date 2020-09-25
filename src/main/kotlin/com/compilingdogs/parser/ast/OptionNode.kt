package com.compilingdogs.parser.ast

import com.compilingdogs.parser.blueColor
import com.compilingdogs.parser.indent
import com.compilingdogs.parser.lightGray
import com.compilingdogs.parser.noColor
import tokens.Token
import java.lang.Error


class OptionalNode(
    var node: ASTNode
) : ASTNode() {

    override fun match(tokens: List<Token>, parentNode: FASTNode, depth: Int, enablePrints: Boolean): MatchResults {
        // Debugging stuff.
        if (enablePrints && logNodeTraversal) {
            println("${indent(depth)}Matching OptionalNode $name; parent is $parentNode")
            println("${indent(depth + 1)}${lightGray}Tokens: ${tokens.joinToString(" ")}${noColor}")
        }

        // If this node contains its own mapped FASTNode, use it.
        // If not, propagate parent FASTNode instead.
        val fastNode = attachedTo?.newInstance() ?: parentNode

        // Result of parsing. Contains amount of parsed nodes.
        var res: MatchResults = MatchResults(null, Error("WTF???"))

        // Firstly, try parsing into a dummy node. If parsing succeeds, parse into parent node.
        for (n in listOf(fastNode.clone(), fastNode)) {
            res = node.match(
                transformTokens(tokens),
                n,
                depth + 1,
                enablePrints && System.identityHashCode(n) != System.identityHashCode(fastNode)
            )
            if (res.parsedTokens == null)
                return MatchResults(0, null)
        }

        // If this AST node is attached to some FAST node, consume the attached node to parent.
        if (attachedTo != null)
            parentNode.consume(fastNode)

        // Debugging stuff.
        if (enablePrints)
            println("${indent(depth + 1)}${blueColor}Stopping $name with parent = $parentNode$noColor")
        return res
    }

    override fun clone(): ASTNode = OptionalNode(node.clone()).also { it.name = name }

    override fun get(name: String): ASTNode? = if (node.name == name) node else null

    override fun toString(): String {
        return "OptionNode($name)"
    }
}

fun maybe(name: String = "", init: () -> ASTNode): OptionalNode = OptionalNode(init()).apply { this.name = name }

