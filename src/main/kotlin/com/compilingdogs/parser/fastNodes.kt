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

class FASTExpression(
    val leftRelation: FASTRelation,
//    val rightRelation: FASTRelation?,
//    val relation: FASTLogicalOperator
) : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTRelation(
    val leftRelation: FASTFactor,
    val rightRelation: FASTFactor?,
    val relation: FASTComparisonOperator
) : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTFactor(

) : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

