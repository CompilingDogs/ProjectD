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
abstract class ASTNode(
    // Specifies the data structure that this node is attached to.
    // While parsing, the particular values will be accumulated here.
    var attachedTo: Class<FASTNode>? = null,
    var createCallback: ((FASTNode?) -> Unit)? = null,
    var successCallback: ((FASTNode?, FASTNode) -> Unit)? = null,
) {
    abstract fun match(tokens: List<Token>, parentNode: FASTNode?): Pair<Int, FASTNode>?

    abstract fun clone(): ASTNode

    // TODO: check if this works
    inline fun <reified T : FASTNode> mapTo() {
        attachedTo = T::class.java as Class<FASTNode>
    }
}

// DSL function to create an empty node. Can be used as a placeholder for not implemented AST nodes.
fun node(init: ASTNode.() -> Unit): ASTNode = initialize(init)


// --------------------------------------------------------------------------------------------------------

infix fun <T : FASTNode?> ASTNode.onCreate(callback: (fastNode: T) -> Unit): ASTNode {
    val newNode = clone()
    newNode.createCallback = callback as (FASTNode?) -> Unit
    return newNode
}

infix fun <T : FASTNode?> ASTNode.onSuccess(callback: (fastNode: T, nextFastNode: FASTNode) -> Unit): ASTNode {
    val newNode = clone()
    newNode.successCallback = callback as (FASTNode?, FASTNode) -> Unit
    return newNode
}




