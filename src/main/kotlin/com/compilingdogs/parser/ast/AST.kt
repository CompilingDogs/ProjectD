package com.compilingdogs.parser.ast

import tokens.Token

/*
* This package contains the EBNF framework to express your grammar in the DSL form for further automatic parsing.
*
* We took some terms from the EBNF Wikipedia article:
*     https://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_form
*/


val logNodeTraversal = true
val logFASTNodes = false
val logFASTTokens = false


// Function used to generate different kinds of nodes in DSL. See particular usages of this function below.
inline fun <reified T : ASTNode> initialize(init: T.() -> Unit, name: String = ""): T {
    // TODO: find another way for this (if possible)
    val node = T::class.java.newInstance()
    node.name = name
    node.init()
    return node
}


// The base class for all AST nodes
abstract class ASTNode(
    var name: String = "",
    // Specifies the data structure that this node is attached to.
    // While parsing, the particular values will be accumulated here.
    var attachedTo: Class<FASTNode>? = null,
//    var createCallback: ((FASTNode?) -> Unit)? = null,
//    var successCallback: ((FASTNode?, FASTNode?) -> Unit)? = null,

    // If this is true and when node is parsed to an end,
    // and some tokens are still left till the end, throw exception.
//    var mandatory: Boolean = false

    var transformTokens: (List<Token>) -> List<Token> = { s -> s }
) {
    abstract fun match(tokens: List<Token>, parentNode: FASTNode, depth: Int, enablePrints: Boolean): Int?

    abstract fun clone(): ASTNode

    abstract operator fun get(name: String): ASTNode?

    inline fun <reified T : FASTNode> mapTo() {
        attachedTo = T::class.java as Class<FASTNode>
    }
}

// DSL function to create an empty node. Can be used as a placeholder for not implemented AST nodes.
fun node(name: String = "", init: ASTNode.() -> Unit): ASTNode = initialize(init, name)


// --------------------------------------------------------------------------------------------------------

//infix fun <T : FASTNode?> ASTNode.onCreate(callback: (fastNode: T) -> Unit): ASTNode {
//    val newNode = clone()
//    newNode.createCallback = callback as (FASTNode?) -> Unit
//    return newNode
//}

//infix fun <T : FASTNode?> ASTNode.onSuccess(callback: (fastNode: T, nextFastNode: FASTNode) -> Unit): ASTNode {
//    val newNode = clone()
//    newNode.successCallback = callback as (FASTNode?, FASTNode?) -> Unit
//    return newNode
//}





