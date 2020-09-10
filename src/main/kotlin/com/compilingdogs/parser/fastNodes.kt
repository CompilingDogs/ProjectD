package com.compilingdogs.parser

import com.compilingdogs.parser.ast.FASTNode
import tokens.Token


class FASTArrayLiteral(
    var members: MutableList<FASTNode> = mutableListOf()
) : FASTNode() {

    override fun clone(): FASTNode {
        return FASTArrayLiteral(members.toMutableList())
    }

    override fun toString(): String {
        return "ArrayLiteral $members"
    }
}

class FASTIdentifier(val name: String) : FASTNode() {
    override fun clone(): FASTNode {
        return FASTIdentifier(name)
    }
}

class FASTToken<T : Token>(
    val token: T
) : FASTNode() {
    override fun clone(): FASTNode {
        return FASTToken(token)
    }

    override fun toString(): String {
        return "FASTToken($token)"
    }
}

//class FASTExpression(
//    val leftRelation: FASTRelation,
////    val rightRelation: FASTRelation?,
////    val relation: FASTLogicalOperator
//) : FASTNode() {
//    override fun clone(): FASTNode {
//        TODO("Not yet implemented")
//    }
//}

//class FASTRelation(
//    val leftRelation: FASTFactor,
//    val rightRelation: FASTFactor?,
//    val relation: FASTComparisonOperator
//) : FASTNode() {
//    override fun clone(): FASTNode {
//        TODO("Not yet implemented")
//    }
//}

class FASTFactor(

) : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

