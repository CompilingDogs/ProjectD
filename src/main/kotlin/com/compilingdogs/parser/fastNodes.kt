package com.compilingdogs.parser

import com.compilingdogs.parser.ast.FASTNode
import tokens.Token

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

//class FASTVarDefinition(
//    val leftTerm: FASTIdentifier,
//    val rightTerm: FASTExpression?,
//    val relation: FASTCompareOperator?
//) : FASTNode() {
//    override fun clone(): FASTNode {
//        TODO("Not yet implemented")
//    }
//}
//
//class FASTAssignment(
//    val leftTerm: FASTReference,
//    val rightTerm: FASTExpression,
//    val relation: FASTCompareOperator
//) : FASTNode() {
//    override fun clone(): FASTNode {
//        TODO("Not yet implemented")
//    }
//}
//
//class FASTExpression(
//    val leftRelation: FASTRelation,
//    val rightRelation: FASTRelation?,
//    val relation: FASTLogicalOperator?
//) : FASTNode() {
//    override fun clone(): FASTNode {
//        TODO("Not yet implemented")
//    }
//}
//
//class FASTRelation(
//    val leftRelation: FASTFactor,
//    val rightRelation: FASTFactor?,
//    val relation: FASTComparisonOperator?
//) : FASTNode() {
//    override fun clone(): FASTNode {
//        TODO("Not yet implemented")
//    }
//}
//
//class FASTFactor(
//    val leftTerm: FASTTerm,
//    val rightTerm: FASTTerm?,
//    val relation: FASTSimpleArithmeticOperator?
//) : FASTNode() {
//    override fun clone(): FASTNode {
//        TODO("Not yet implemented")
//    }
//}
//
//class FASTTerm(
//    val leftTerm: FASTUnary,
//    val rightTerm: FASTUnary?,
//    val relation: FASTComplicatedArithmeticOperator?
//) : FASTNode() {
//    override fun clone(): FASTNode {
//        TODO("Not yet implemented")
//    }
//}

class FASTArrayLiteral(
    var members: MutableList<FASTNode> = mutableListOf()
) : FASTNode() {

    override fun clone(): FASTNode {
        return FASTArrayLiteral(members.toMutableList())
    }

    override fun toString(): String {
        return "FASTArrayLiteral($members)"
    }
}

class FASTEmptyOptionalNode() : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTIdentifier(val name: String) : FASTNode() {
    override fun clone(): FASTNode {
        return FASTIdentifier(name)
    }
}
