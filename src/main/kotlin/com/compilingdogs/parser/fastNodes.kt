package com.compilingdogs.parser

import com.compilingdogs.parser.ast.FASTNode
import tokens.Token

class FASTToken<T : Token>(
    val token: T
) : FASTNode() {
    override fun clone(): FASTNode {
        return FASTToken(token)
    }

    override fun consume(node: FASTNode) {
        throw NotImplementedError("Consume not applicable to " + this::class.simpleName)
    }

    override fun toString(): String {
        return "FASTToken($token)"
    }
}

class FASTProgram(
    val statements: MutableList<FASTStatement> = mutableListOf()
) : FASTNode() {
    override fun clone(): FASTNode {
        return FASTProgram(statements.toMutableList())
    }

    override fun consume(node: FASTNode) {
        if (node is FASTStatement) {
            statements.add(node)
        } else {
            throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
    }
}

abstract class FASTStatement : FASTNode()

class FASTDeclarationStatement(
    val definitions: MutableList<FASTVarDefinition> = mutableListOf()
) : FASTStatement() {
    override fun clone(): FASTNode {
        return FASTDeclarationStatement(definitions.toMutableList())
    }

    override fun consume(node: FASTNode) {
        if (node is FASTVarDefinition) {
            definitions.add(node)
        } else {
            throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
    }
}

class FASTAssignmentStatement(
    var reference: FASTReference,
    var value: FASTExpression
) : FASTStatement() {
    override fun clone(): FASTNode {
        return FASTAssignmentStatement(reference, value)
    }

    override fun consume(node: FASTNode) {
        when (node) {
            is FASTReference -> this.reference = node
            is FASTExpression -> this.value = node
            else -> throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
    }
}

abstract class FASTControlStatement : FASTStatement()

class FASTPrintStatement(
    var values: MutableList<FASTExpression> = mutableListOf()
) : FASTStatement() {
    override fun clone(): FASTNode {
        return FASTPrintStatement(values.toMutableList())
    }

    override fun consume(node: FASTNode) {
        if (node is FASTExpression) {
            this.values.add(node)
        } else {
            throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
    }
}

class FASTReturnStatement(
    var value: FASTExpression?
) : FASTStatement() {
    override fun clone(): FASTNode {
        return FASTReturnStatement(value)
    }

    override fun consume(node: FASTNode) {
        if (node is FASTExpression) {
            this.value = node
        } else {
            throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
    }
}

class FASTVarDefinition(
    var name: FASTIdentifier,
    var value: FASTExpression?
) : FASTNode() {
    override fun clone(): FASTNode {
        return FASTVarDefinition(name, value)
    }

    override fun consume(node: FASTNode) {
        when (node) {
            is FASTIdentifier -> this.name = node
            is FASTExpression -> this.value = node
            else -> throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
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

    override fun consume(node: FASTNode) {
        if (node is FASTExpression) {
            this.member = node
        } else {
            throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
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

    override fun consume(node: FASTNode) {
        when (node) {
            is FASTIdentifier -> this.identifier = node
            is FASTMemberReference -> this.references.add(node)
            else -> throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
    }
}

open class FASTIdentifier(
    var name: String
) : FASTNode() {
    override fun clone() = FASTIdentifier(name)

    override fun consume(node: FASTNode) {
        throw NotImplementedError("Consume not applicable to " + this::class.simpleName)
    }
}

abstract class FASTExpression : FASTNode()

// TODO: Done By Alecsey
class FASTOrOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTOrOperator().also {
            it.left = left
            it.right = right
        }
    }
}

// TODO: Done By Alecsey
class FASTAndOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTAndOperator().also {
            it.left = left
            it.right = right
        }
    }
}

// TODO: Done By Alecsey
class FASTXorOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTXorOperator().also {
            it.left = left
            it.right = right
        }
    }
}


// TODO: Done By Alecsey
class FASTLessOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTLessOperator().also {
            it.left = left
            it.right = right
        }
    }
}

// TODO: Done By Alecsey
class FASTLessEqualOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTLessEqualOperator().also {
            it.left = left
            it.right = right
        }
    }
}

// TODO: Done By Alecsey
class FASTGreaterOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTGreaterOperator().also {
            it.left = left
            it.right = right
        }
    }
}

// TODO: Done By Alecsey
class FASTGreaterEqualOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTGreaterEqualOperator().also {
            it.left = left
            it.right = right
        }
    }
}

// TODO: Done By Alecsey
class FASTEqualOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTEqualOperator().also {
            it.left = left
            it.right = right
        }
    }
}

// TODO: Done By Alecsey
class FASTNotEqualOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTNotEqualOperator().also {
            it.left = left
            it.right = right
        }
    }
}


// TODO: Done By Alecsey
class FASTAddOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTAddOperator().also {
            it.left = left
            it.right = right
        }
    }
}

// TODO: Done By Alecsey
class FASTSubtractOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTSubtractOperator().also {
            it.left = left
            it.right = right
        }
    }
}


// TODO: Done By Alecsey
class FASTMultiplyOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTMultiplyOperator().also {
            it.left = left
            it.right = right
        }
    }
}

// TODO: Done By Alecsey
class FASTDivideOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTDivideOperator().also {
            it.left = left
            it.right = right
        }
    }
}

// TODO: Done By Alecsey
class FASTIsOperator : FASTBinaryOperator() {
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
    override fun consume(node: FASTNode) {
        if (node is FASTLiteral) {
            this.literal = node
        } else {
            throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
    }
}

class FASTExpressionReferenceLeaf(
    var reference: FASTReference
) : FASTExpressionLeaf() {
    override fun clone() = FASTExpressionReferenceLeaf(reference)
    override fun consume(node: FASTNode) {
        if (node is FASTReference) {
            this.reference = node
        } else {
            throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
    }
}


abstract class FASTBinaryOperator(
    var left: FASTExpression? = null,
    var right: FASTExpression? = null
) : FASTNode() {
    override fun consume(node: FASTNode) {
        if (node is FASTExpression) {

            if (this.left == null) {
                this.left = node
            } else if (this.right == null) {
                this.right = node
            } else {
                throw IllegalStateException("Both of left and right operands are satisfied for " + this::class.simpleName)
            }

        } else {
            throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
    }
}

abstract class FASTUnaryOperator(
    var value: FASTExpression? = null
) : FASTNode() {
    override fun consume(node: FASTNode) {
        if (node is FASTExpression) {
            this.value = node
        } else {
            throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
    }
}

abstract class FASTTypeIndicator(name: String) : FASTIdentifier(name)

class FASTTypeIndicatorInt : FASTTypeIndicator("int")
class FASTTypeIndicatorReal : FASTTypeIndicator("real")
class FASTTypeIndicatorBool : FASTTypeIndicator("bool")
class FASTTypeIndicatorString : FASTTypeIndicator("string")
class FASTTypeIndicatorEmpty : FASTTypeIndicator("empty")
class FASTTypeIndicatorArray : FASTTypeIndicator("[]")
class FASTTypeIndicatorTuple : FASTTypeIndicator("{}")
class FASTTypeIndicatorFunc : FASTTypeIndicator("func")


class FASTFunctionLiteral(
    var args: MutableList<FASTIdentifier> = mutableListOf(),
    var body: FASTFunctionBody
) : FASTLiteral() {
    override fun clone() = FASTFunctionLiteral(args.toMutableList(), body.clone())
    override fun consume(node: FASTNode) {
        when (node) {
            is FASTIdentifier -> this.args.add(node)
            is FASTFunctionBody -> this.body = node
            else -> throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
    }
}

class FASTFunctionBody(
    var statements: MutableList<FASTStatement> = mutableListOf()
) : FASTNode() {
    override fun clone() = FASTFunctionBody(statements.toMutableList())
    override fun consume(node: FASTNode) {
        if (node is FASTStatement) {
            this.statements.add(node)
        } else {
            throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
    }
}

abstract class FASTLiteral : FASTNode()

class FASTIntegerLiteral(
    var value: Int
) : FASTLiteral() {
    override fun clone() = FASTIntegerLiteral(value)
    override fun consume(node: FASTNode) {
        throw NotImplementedError("Consume not applicable to " + this::class.simpleName)
    }
}
class FASTRealLiteral(
    var value: Float
) : FASTLiteral() {
    override fun clone() = FASTRealLiteral(value)
    override fun consume(node: FASTNode) {
        throw NotImplementedError("Consume not applicable to " + this::class.simpleName)
    }
}

class FASTStringLiteral(
    var value: String
) : FASTLiteral() {
    override fun clone() = FASTStringLiteral(value)
    override fun consume(node: FASTNode) {
        throw NotImplementedError("Consume not applicable to " + this::class.simpleName)
    }
}

class FASTBooleanLiteral(
    var value: Boolean
) : FASTLiteral() {
    override fun clone() = FASTBooleanLiteral(value)
    override fun consume(node: FASTNode) {
        throw NotImplementedError("Consume not applicable to " + this::class.simpleName)
    }
}

class FASTEmptyLiteral : FASTLiteral() {
    override fun clone() = this
    override fun consume(node: FASTNode) {
        throw NotImplementedError("Consume not applicable to " + this::class.simpleName)
    }
}

class FASTArrayLiteral(
    var members: MutableList<FASTExpression> = mutableListOf()
) : FASTNode() {

    override fun clone(): FASTNode {
        return FASTArrayLiteral(members.toMutableList())
    }

    override fun consume(node: FASTNode) {
        if (node is FASTExpression) {
            this.members.add(node)
        } else {
            throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
    }

    override fun toString(): String {
        return "FASTArrayLiteral($members)"
    }
}

class FASTTupleLiteral(
    var members: MutableList<FASTTupleElement> = mutableListOf()
) : FASTLiteral() {
    override fun clone() = FASTTupleLiteral(members.toMutableList())
    override fun consume(node: FASTNode) {
        if (node is FASTTupleElement) {
            this.members.add(node)
        } else {
            throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
    }
}

class FASTTupleElement(
    var name: FASTIdentifier?,
    var value: FASTExpression
) : FASTNode() {
    override fun clone() = FASTTupleElement(name, value)
    override fun consume(node: FASTNode) {
        when (node) {
            is FASTIdentifier -> this.name = node
            is FASTExpression -> this.value = node
            else -> throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
    }
}

class FASTIfStructure(
    var condition: FASTExpression,
    var body: FASTFunctionBody?,
    var elseBody: FASTFunctionBody?
) : FASTControlStatement() {
    override fun clone() = FASTIfStructure(condition, body, elseBody)
    override fun consume(node: FASTNode) {
        when (node) {
            is FASTExpression -> this.condition = node
            is FASTFunctionBody -> {
                if (this.body == null) {
                    this.body = node
                } else if (this.elseBody == null) {
                    this.elseBody = node
                } else {
                    throw IllegalStateException("Both of left and right operands are satisfied for " + this::class.simpleName)
                }
            }
        }
    }
}

abstract class FASTLoopStructure : FASTControlStatement()

class FASTForLoop(
    var varName: FASTIdentifier?,
    var rangeBegin: FASTExpression?,
    var rangeEnd: FASTExpression?,
    var iterable: FASTExpression?,
    var body: FASTFunctionBody?
) : FASTLoopStructure() {
    override fun clone() = FASTForLoop(varName, rangeBegin, rangeEnd, iterable, body)
    override fun consume(node: FASTNode) {
        when (node) {
            is FASTIdentifier -> this.varName = node
            is FASTFunctionBody -> this.body = node
            is FASTExpression -> {
                if (this.iterable == null) {
                    this.iterable = node
                } else {
                    if (this.rangeBegin == null) {
                        this.rangeBegin = this.iterable
                        this.rangeEnd = node
                        this.iterable = null
                    } else {
                        throw IllegalStateException("Both of left and right operands are satisfied for " + this::class.simpleName)
                    }
                }
            }
            else -> throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
    }
}

class FASTWhileLoop(
    var cond: FASTExpression,
    var body: FASTFunctionBody
) : FASTLoopStructure() {
    override fun clone() = FASTWhileLoop(cond, body)
    override fun consume(node: FASTNode) {
        when (node) {
            is FASTExpression -> this.cond = node
            is FASTFunctionBody -> this.body = node
            else -> throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
    }
}

class FASTEmptyOptionalNode : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }

    override fun consume(node: FASTNode) {
        throw NotImplementedError("Consume not applicable to " + this::class.simpleName)
    }
}

