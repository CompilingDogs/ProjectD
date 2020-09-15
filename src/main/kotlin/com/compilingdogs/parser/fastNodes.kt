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

class FASTPrintStatement (
    var values : MutableList<FASTExpression> = mutableListOf()
) : FASTStatement() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTReturnStatement (
    var value : FASTExpression?
) : FASTStatement() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTVarDefinition (
    var name: FASTIdentifier,
    var value: FASTExpression?
) : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTReference (

) : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTIdentifier (
    var name: String
) : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
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

class FASTExpressionLiteralLeaf : FASTExpressionLeaf() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTExpressionReferenceLeaf : FASTExpressionLeaf() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}


abstract class FASTBinaryOperator (
    var left: FASTExpression,
    var right: FASTExpression
) : FASTNode() {
    constructor()
}

abstract class FASTUnaryOperator (
    var value: FASTExpression
) : FASTNode() {
    constructor()
}


class FASTTypeIndicator (

) : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTFunctionLiteral (

) : FASTLiteral() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTFunctionBody (
    var statement: List<FASTStatement>
) : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

abstract class FASTLiteral : FASTNode()

class FASTIntegerLiteral (

) : FASTLiteral() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTRealLiteral (

) : FASTLiteral() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTStringLiteral (

) : FASTLiteral() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTBooleanLiteral (

) : FASTLiteral() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTEmptyLiteral (

) : FASTLiteral() {
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

class FASTTupleLiteral (

) : FASTLiteral() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTTupleElement (

) : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTIfStructure (

) : FASTControlStatement() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

abstract class FASTLoopStructure : FASTControlStatement()

class FASTForLoop (

) : FASTLoopStructure() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTWhileLoop (

) : FASTLoopStructure() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

