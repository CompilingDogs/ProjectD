package com.compilingdogs.parser

import com.compilingdogs.parser.ast.FASTNode
import tokens.Token
import java.util.concurrent.locks.Condition

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

class FASTProgram(
    val statements: MutableList<FASTStatement>
) : FASTNode() {
    override fun clone(): FASTNode {
        return FASTProgram(statements.toMutableList())
    }
}

abstract class FASTStatement : FASTNode()

class FASTDeclarationStatement(
    val definitions: List<FASTVarDefinition>
) : FASTStatement() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTAssignmentStatement(
    var reference: FASTReference,
    var value: FASTExpression
) : FASTStatement() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

abstract class FASTControlStatement : FASTStatement()

class FASTPrintStatement(
    var values: MutableList<FASTExpression> = mutableListOf()
) : FASTStatement() {
    override fun clone(): FASTNode {
        return FASTPrintStatement(values.toMutableList())
    }
}

class FASTReturnStatement(
    var value: FASTExpression?
) : FASTStatement() {
    override fun clone(): FASTNode {
        return FASTReturnStatement(value)
    }
}

class FASTVarDefinition(
    var name: FASTIdentifier,
    var value: FASTExpression?
) : FASTNode() {
    override fun clone(): FASTNode {
        return FASTVarDefinition(name, value)
    }
}

class FASTReference(

) : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

open class FASTIdentifier(
    var name: String
) : FASTNode() {
    override fun clone() = FASTIdentifier(name)
}

abstract class FASTExpression : FASTNode()

class FASTOrOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTAndOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTXorOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}


class FASTLessOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTLessEqualOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTGreaterOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTGreaterEqualOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTEqualOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTNotEqualOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}


class FASTAddOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTSubtractOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}


class FASTMultiplyOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTDivideOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTIsOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}


class FASTPositiveOperator : FASTUnaryOperator() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTNegativeOperator : FASTUnaryOperator() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTNotOperator : FASTUnaryOperator() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}


abstract class FASTExpressionLeaf : FASTExpression()

class FASTExpressionLiteralLeaf(
    var literal: FASTLiteral
) : FASTExpressionLeaf() {
    override fun clone() = FASTExpressionLiteralLeaf(literal)
}

class FASTExpressionReferenceLeaf(
    var reference: FASTReference
) : FASTExpressionLeaf() {
    override fun clone() = FASTExpressionReferenceLeaf(reference)
}


abstract class FASTBinaryOperator(
    var left: FASTExpression,
    var right: FASTExpression
) : FASTNode() {
    constructor()
}

abstract class FASTUnaryOperator(
    var value: FASTExpression
) : FASTNode() {
    constructor()
}

abstract class FASTTypeIndicator(name: String) : FASTIdentifier(name)

class FASTTypeIndicatorInt() : FASTTypeIndicator("int")
class FASTTypeIndicatorReal() : FASTTypeIndicator("real")
class FASTTypeIndicatorBool() : FASTTypeIndicator("bool")
class FASTTypeIndicatorString() : FASTTypeIndicator("string")
class FASTTypeIndicatorEmpty() : FASTTypeIndicator("empty")
class FASTTypeIndicatorArray() : FASTTypeIndicator("[]")
class FASTTypeIndicatorTuple() : FASTTypeIndicator("{}")
class FASTTypeIndicatorFunc() : FASTTypeIndicator("func")


class FASTFunctionLiteral(
    var args: MutableList<FASTIdentifier> = mutableListOf(),
    var body: FASTFunctionBody
) : FASTLiteral() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTFunctionBody(
    var statements: List<FASTStatement>
) : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

abstract class FASTLiteral : FASTNode()

class FASTIntegerLiteral(
    var value: Int
) : FASTLiteral() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTRealLiteral(
    var value: Float
) : FASTLiteral() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTStringLiteral(
    var value: String
) : FASTLiteral() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTBooleanLiteral(
    var value: Boolean
) : FASTLiteral() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTEmptyLiteral : FASTLiteral() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

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

class FASTTupleLiteral(
    var members: MutableList<FASTTupleElement> = mutableListOf()
) : FASTLiteral() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTTupleElement(
    var name: FASTIdentifier?,
    var value: FASTExpression
) : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTIfStructure(
    var condition: FASTExpression,
    var body: FASTFunctionBody,
    var elseBody: FASTFunctionBody?
) : FASTControlStatement() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

abstract class FASTLoopStructure : FASTControlStatement()

class FASTForLoop(
    var varName: FASTIdentifier?,
    var rangeBegin: FASTExpression?,
    var rangeEnd: FASTExpression?,
    var iterable: FASTExpression?,
    var body: FASTFunctionBody
) : FASTLoopStructure() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTWhileLoop(
    var cond: FASTExpression,
    var body: FASTFunctionBody
) : FASTLoopStructure() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

