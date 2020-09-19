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
    val definitions: MutableList<FASTVarDefinition>
) : FASTStatement() {
    override fun clone(): FASTNode {
        return FASTDeclarationStatement(definitions.toMutableList())
    }
}

class FASTAssignmentStatement(
    var reference: FASTReference,
    var value: FASTExpression
) : FASTStatement() {
    override fun clone(): FASTNode {
        return FASTAssignmentStatement(reference, value)
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

// TODO: Done By Alecsey
enum class FASTMemberReferenceType {
    DOT_REFERENCE, BRACKETS_REFERENCE
}

// TODO: Done By Alecsey
class FASTMemberReference(
    var member: FASTExpression,
    var type: FASTMemberReferenceType
) : FASTNode() {
    override fun clone(): FASTNode {
        return FASTMemberReference(member, type)
    }
}

// TODO: Done By Alecsey
class FASTReference(
    var identifier: FASTIdentifier,
    var references: MutableList<FASTMemberReference>
) : FASTNode() {
    override fun clone(): FASTNode {
        return FASTReference(identifier, references)
    }
}

open class FASTIdentifier(
    var name: String
) : FASTNode() {
    override fun clone() = FASTIdentifier(name)
}

abstract class FASTExpression : FASTNode()

// TODO: Done By Alecsey
class FASTOrOperator() : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTOrOperator().also {
            it.left = left
            it.right = right
        }
    }
}

// TODO: Done By Alecsey
class FASTAndOperator() : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTAndOperator().also {
            it.left = left
            it.right = right
        }
    }
}

// TODO: Done By Alecsey
class FASTXorOperator() : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTXorOperator().also {
            it.left = left
            it.right = right
        }
    }
}


// TODO: Done By Alecsey
class FASTLessOperator() : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTLessOperator().also {
            it.left = left
            it.right = right
        }
    }
}

// TODO: Done By Alecsey
class FASTLessEqualOperator() : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTLessEqualOperator().also {
            it.left = left
            it.right = right
        }
    }
}

// TODO: Done By Alecsey
class FASTGreaterOperator() : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTGreaterOperator().also {
            it.left = left
            it.right = right
        }
    }
}

// TODO: Done By Alecsey
class FASTGreaterEqualOperator() : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTGreaterEqualOperator().also {
            it.left = left
            it.right = right
        }
    }
}

// TODO: Done By Alecsey
class FASTEqualOperator() : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTEqualOperator().also {
            it.left = left
            it.right = right
        }
    }
}

// TODO: Done By Alecsey
class FASTNotEqualOperator() : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTNotEqualOperator().also {
            it.left = left
            it.right = right
        }
    }
}


// TODO: Done By Alecsey
class FASTAddOperator() : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTAddOperator().also {
            it.left = left
            it.right = right
        }
    }
}

// TODO: Done By Alecsey
class FASTSubtractOperator() : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTSubtractOperator().also {
            it.left = left
            it.right = right
        }
    }
}


// TODO: Done By Alecsey
class FASTMultiplyOperator() : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTMultiplyOperator().also {
            it.left = left
            it.right = right
        }
    }
}

// TODO: Done By Alecsey
class FASTDivideOperator() : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTDivideOperator().also {
            it.left = left
            it.right = right
        }
    }
}

// TODO: Done By Alecsey
class FASTIsOperator() : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTIsOperator().also {
            it.left = left
            it.right = right
        }
    }
}

// TODO: Done By Alecsey
class FASTPositiveOperator : FASTUnaryOperator() {
    override fun clone(): FASTNode {
        return FASTPositiveOperator().also {
            it.value = value
        }
    }
}

// TODO: Done By Alecsey
class FASTNegativeOperator : FASTUnaryOperator() {
    override fun clone(): FASTNode {
        return FASTNegativeOperator().also {
            it.value = value
        }
    }
}

// TODO: Done By Alecsey
class FASTNotOperator : FASTUnaryOperator() {
    override fun clone(): FASTNode {
        return FASTNotOperator().also {
            it.value = value
        }
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
    var left: FASTExpression? = null,
    var right: FASTExpression? = null
) : FASTNode()

abstract class FASTUnaryOperator(
    var value: FASTExpression? = null
) : FASTNode()

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
    override fun clone() = FASTFunctionLiteral(args.toMutableList(), body.clone())
}

class FASTFunctionBody(
    var statements: List<FASTStatement>
) : FASTNode() {
    override fun clone() = FASTFunctionBody(statements.toMutableList())
}

abstract class FASTLiteral : FASTNode()

class FASTIntegerLiteral(
    var value: Int
) : FASTLiteral() {
    override fun clone() = FASTIntegerLiteral(value)
}

class FASTRealLiteral(
    var value: Float
) : FASTLiteral() {
    override fun clone() = FASTRealLiteral(value)
}

class FASTStringLiteral(
    var value: String
) : FASTLiteral() {
    override fun clone() = FASTStringLiteral(value)
}

class FASTBooleanLiteral(
    var value: Boolean
) : FASTLiteral() {
    override fun clone() = FASTBooleanLiteral(value)
}

class FASTEmptyLiteral : FASTLiteral() {
    override fun clone() = this
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
    override fun clone() = FASTTupleLiteral(members.toMutableList())
}

class FASTTupleElement(
    var name: FASTIdentifier?,
    var value: FASTExpression
) : FASTNode() {
    override fun clone() = FASTTupleElement(name, value)
}

class FASTIfStructure(
    var condition: FASTExpression,
    var body: FASTFunctionBody,
    var elseBody: FASTFunctionBody?
) : FASTControlStatement() {
    override fun clone() = FASTIfStructure(condition, body, elseBody)
}

abstract class FASTLoopStructure : FASTControlStatement()

class FASTForLoop(
    var varName: FASTIdentifier?,
    var rangeBegin: FASTExpression?,
    var rangeEnd: FASTExpression?,
    var iterable: FASTExpression?,
    var body: FASTFunctionBody
) : FASTLoopStructure() {
    override fun clone() = FASTForLoop(varName, rangeBegin, rangeEnd, iterable, body)
}

class FASTWhileLoop(
    var cond: FASTExpression,
    var body: FASTFunctionBody
) : FASTLoopStructure() {
    override fun clone() = FASTWhileLoop(cond, body)
}

class FASTEmptyOptionalNode : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

