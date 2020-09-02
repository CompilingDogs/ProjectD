package com.compilingdogs.parser.ast

import tokens.Token

/*
* This package contains the EBNF framework to express your grammar in the DSL form for further automatic parsing.
*
* We took some terms from the EBNF Wikipedia article:
*     https://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_form
*/


// Function used to generate different kinds of nodes in DSL. See particular usages of this function below.
inline fun <reified T : ASTNode> initialize(init: T.() -> Unit): T {
    // TODO: find another way for this (if possible)
    val node = T::class.java.newInstance()
    node.init()
    return node
}


// The base class for all AST nodes
abstract class ASTNode (
        var attachedTo: Class<ASTNode>? = null
) {
    abstract fun clone(): ASTNode
    abstract fun match(): ASTNode?
    // TODO: check if this works
    inline fun <reified T : ASTNode> mapTo() { attachedTo = T::class.java as Class<ASTNode> }
}

// DSL function to create an empty node. Can be used as a placeholder for not implemented AST nodes.
fun node(init: ASTNode.() -> Unit): ASTNode = initialize(init)






