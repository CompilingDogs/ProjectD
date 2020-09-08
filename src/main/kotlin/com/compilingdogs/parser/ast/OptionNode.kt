package com.compilingdogs.parser.ast

import tokens.Token


class OptionalNode(
        var node: ASTNode
) : ASTNode() {

    override fun match(tokens: List<Token>, parentNode: FASTNode?): Pair<Int, FASTNode>? {
        TODO("Not yet implemented")
    }
}

fun maybe(init: () -> ASTNode): OptionalNode = OptionalNode(init())