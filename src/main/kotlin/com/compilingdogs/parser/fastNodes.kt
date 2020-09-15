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
    val statements: List<FASTStatement>
) : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
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

open class FASTControlStatement(

) : FASTStatement() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTPrintStatement (

) : FASTStatement() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTReturnStatement (

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

) : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTExpression (

) : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTOrOperator : FASTBinaryOperator()
class FASTAndOperator : FASTBinaryOperator()
class FASTXorOperator : FASTBinaryOperator()


class FASTLessOperator : FASTBinaryOperator()
class FASTLessEqualOperator : FASTBinaryOperator()
class FASTGreaterOperator : FASTBinaryOperator()
class FASTGreaterEqualOperator : FASTBinaryOperator()
class FASTEqualOperator : FASTBinaryOperator()
class FASTNotEqualOperator : FASTBinaryOperator()


class FASTAddOperator : FASTBinaryOperator()
class FASTSubtractOperator : FASTBinaryOperator()


class FASTMultiplyOperator : FASTBinaryOperator()
class FASTDivideOperator : FASTBinaryOperator()


class FASTPositiveOperator : FASTUnaryOperator()
class FASTNegativeOperator : FASTUnaryOperator()
class FASTNotOperator : FASTUnaryOperator()



abstract class FASTBinaryOperator (
    var left: FASTExpression,
    var right: FASTExpression
) : FASTNode()

abstract class FASTUnaryOperator (
    var value: FASTExpression
) : FASTNode()




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

) : FASTControlStatement()

abstract class FASTLoopStructure : FASTControlStatement()

class FASTForLoop (

) : FASTLoopStructure()

class FASTWhileLoop (

) : FASTLoopStructure()

