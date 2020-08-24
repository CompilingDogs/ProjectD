package com.compilingdogs.parser.ast


class OptionalNode(
        var node: ASTNode
) : ASTNode()

fun maybe(init: () -> ASTNode): OptionalNode = OptionalNode(init())