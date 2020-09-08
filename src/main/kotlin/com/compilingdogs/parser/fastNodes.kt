package com.compilingdogs.parser

import com.compilingdogs.parser.ast.FASTNode
import tokens.Token


class ArrayLiteral(
    var members: MutableList<FASTNode> = mutableListOf()
) : FASTNode() {

    override fun clone(): FASTNode {
        return ArrayLiteral(members.toMutableList())
    }
}

class Identifier(val name: String) : FASTNode() {
    override fun clone(): FASTNode {
        return Identifier(name)
    }
}

class FASTToken<T : Token>(
    val token: T
) : FASTNode() {
    override fun clone(): FASTNode {
        return FASTToken(token)
    }
}

