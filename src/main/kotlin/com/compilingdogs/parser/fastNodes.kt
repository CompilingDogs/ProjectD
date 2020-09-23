package com.compilingdogs.parser

import com.compilingdogs.parser.ast.FASTNode
import tokens.Identifier
import tokens.Token
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class FASTToken<T : Token>(
    val token: T
) : FASTExpression() {
    override fun clone(): FASTNode {
        return FASTToken(token)
    }

    override fun consume(node: FASTNode) {
        throw NotImplementedError("Consume not applicable to " + this::class.simpleName)
    }

    override fun toString(): String {
        return "FASTToken<${token.javaClass.simpleName}>($token)"
    }
}

data class FASTProgram(
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

    override fun toString(): String {
        return "FASTProgram($statements)"
    }
}

abstract class FASTStatement : FASTNode()

data class FASTDeclarationStatement(
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

    override fun toString(): String {
        return "FASTDeclarationStatement($definitions)"
    }
}

data class FASTAssignmentStatement(
    var reference: FASTReference? = null,
    var value: FASTExpression? = null
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

data class FASTPrintStatement(
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

data class FASTReturnStatement(
    var value: FASTExpression? = null
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

data class FASTVarDefinition(
    var name: Identifier? = null,
    var value: FASTExpression? = null
) : FASTNode() {
    override fun clone(): FASTNode {
        return FASTVarDefinition(name, value)
    }

    override fun consume(node: FASTNode) {
        if (node is FASTToken<*> && node.token is Identifier)
            this.name = (node as FASTToken<Identifier>).token
        else if (node is FASTExpression)
            this.value = node
        else
            throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
    }

    override fun toString(): String {
        return "FASTVarDefinition($name = $value)"
    }
}

// TODO: Done By Alecsey
enum class FASTMemberReferenceType {
    DOT_REFERENCE, BRACKETS_REFERENCE
}

// TODO: Done By Alecsey
data class FASTMemberReference(
    var member: FASTExpression? = null,
    var type: FASTMemberReferenceType? = null
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
data class FASTReference(
    var identifier: Identifier? = null,
    var references: MutableList<FASTMemberReference> = mutableListOf()
) : FASTNode() {
    override fun clone(): FASTNode {
        return FASTReference(identifier, references)
    }

    override fun consume(node: FASTNode) {
        if (node is FASTToken<*> && node.token is Identifier)
            this.identifier = (node as FASTToken<Identifier>).token
        else if (node is FASTMemberReference)
            this.references.add(node)
        else
            throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
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

abstract class FASTTypeIndicator(val name: String) : FASTNode() {
    override fun consume(node: FASTNode) {
        TODO("Not yet implemented")
    }
}

class FASTTypeIndicatorInt : FASTTypeIndicator("int") {
    override fun clone(): FASTNode {
        return FASTTypeIndicatorInt()
    }
}

class FASTTypeIndicatorReal : FASTTypeIndicator("real") {
    override fun clone(): FASTNode {
        return FASTTypeIndicatorReal()
    }
}

class FASTTypeIndicatorBool : FASTTypeIndicator("bool") {
    override fun clone(): FASTNode {
        return FASTTypeIndicatorBool()
    }
}

class FASTTypeIndicatorString : FASTTypeIndicator("string") {
    override fun clone(): FASTNode {
        return FASTTypeIndicatorString()
    }
}

class FASTTypeIndicatorEmpty : FASTTypeIndicator("empty") {
    override fun clone(): FASTNode {
        return FASTTypeIndicatorEmpty()
    }
}

class FASTTypeIndicatorArray : FASTTypeIndicator("[]") {
    override fun clone(): FASTNode {
        return FASTTypeIndicatorArray()
    }
}

class FASTTypeIndicatorTuple : FASTTypeIndicator("{}") {
    override fun clone(): FASTNode {
        return FASTTypeIndicatorTuple()
    }
}

class FASTTypeIndicatorFunc : FASTTypeIndicator("func") {
    override fun clone(): FASTNode {
        return FASTTypeIndicatorFunc()
    }
}


data class FASTFunctionLiteral(
    var args: MutableList<Identifier> = mutableListOf(),
    var body: FASTBody? = null
) : FASTExpression() {
    override fun clone() = FASTFunctionLiteral(args.toMutableList(), body?.clone())
    override fun consume(node: FASTNode) {
        if (node is FASTToken<*> && node.token is Identifier)
            this.args.add((node as FASTToken<Identifier>).token)
        else if (node is FASTBody)
            this.body = node
        else
            throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
    }
}


class FASTReadIntCall : FASTNode() {
    override fun clone() = FASTReadIntCall()

    override fun consume(node: FASTNode) {
        throw IllegalStateException("Not implemented for this node")
    }
}

class FASTReadRealCall : FASTNode() {
    override fun clone() = FASTReadRealCall()

    override fun consume(node: FASTNode) {
        throw IllegalStateException("Not implemented for this node")
    }
}

class FASTReadStringCall : FASTNode() {
    override fun clone() = FASTReadStringCall()

    override fun consume(node: FASTNode) {
        throw IllegalStateException("Not implemented for this node")
    }
}


data class FASTFunctionCall(
    var args: MutableList<FASTExpression> = mutableListOf()
) : FASTNode() {
    override fun clone() = FASTFunctionCall(args.toMutableList())

    override fun consume(node: FASTNode) {
        when (node) {
            is FASTExpression -> args.add(node)
            else -> throw IllegalArgumentException()
        }
    }
}

class FASTTypeCheckOperator : FASTBinaryOperator() {
    override fun clone() = FASTTypeCheckOperator().also {
        it.left = left
        it.right = right
    }
}

data class FASTBody(
    var statements: MutableList<FASTStatement> = mutableListOf()
) : FASTNode() {
    override fun clone() = FASTBody(statements.toMutableList())
    override fun consume(node: FASTNode) {
        if (node is FASTStatement) {
            this.statements.add(node)
        } else {
            throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
    }
}

data class FASTIntegerLiteral(
    var value: Int? = null
) : FASTExpression() {
    override fun clone() = FASTIntegerLiteral(value)
    override fun consume(node: FASTNode) {
        throw NotImplementedError("Consume not applicable to " + this::class.simpleName)
    }
}

data class FASTRealLiteral(
    var value: Float? = null
) : FASTExpression() {
    override fun clone() = FASTRealLiteral(value)
    override fun consume(node: FASTNode) {
        throw NotImplementedError("Consume not applicable to " + this::class.simpleName)
    }
}

data class FASTStringLiteral(
    var value: String? = null
) : FASTExpression() {
    override fun clone() = FASTStringLiteral(value)
    override fun consume(node: FASTNode) {
        throw NotImplementedError("Consume not applicable to " + this::class.simpleName)
    }
}

data class FASTBooleanLiteral(
    var value: Boolean? = null
) : FASTExpression() {
    override fun clone() = FASTBooleanLiteral(value)
    override fun consume(node: FASTNode) {
        throw NotImplementedError("Consume not applicable to " + this::class.simpleName)
    }
}

class FASTEmptyLiteral : FASTExpression() {
    override fun clone() = this
    override fun consume(node: FASTNode) {
        throw NotImplementedError("Consume not applicable to " + this::class.simpleName)
    }
}

data class FASTArrayLiteral(
    var members: MutableList<FASTExpression> = mutableListOf()
) : FASTExpression() {

    override fun clone(): FASTNode {
        return FASTArrayLiteral(members.toMutableList())
    }

    override fun consume(node: FASTNode) {
        println("Consuming $node to array literal")

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

data class FASTTupleLiteral(
    var members: MutableList<FASTTupleElement> = mutableListOf()
) : FASTExpression() {
    override fun clone() = FASTTupleLiteral(members.toMutableList())
    override fun consume(node: FASTNode) {
        if (node is FASTTupleElement) {
            this.members.add(node)
        } else {
            throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
    }
}

data class FASTTupleElement(
    var name: Identifier? = null,
    var value: FASTExpression? = null
) : FASTNode() {
    override fun clone() = FASTTupleElement(name, value)
    override fun consume(node: FASTNode) {
        if (node is FASTToken<*> && node.token is Identifier)
            this.name = (node as FASTToken<Identifier>).token
        else if (node is FASTExpression)
            this.value = node
        else
            throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
    }
}

data class FASTIfStructure(
    var condition: FASTExpression? = null,
    var body: FASTBody? = null,
    var elseBody: FASTBody? = null
) : FASTControlStatement() {
    override fun clone() = FASTIfStructure(condition, body, elseBody)
    override fun consume(node: FASTNode) {
        when (node) {
            is FASTExpression -> this.condition = node
            is FASTBody -> {
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

data class FASTForLoop(
    var varName: Identifier? = null,
    var rangeBegin: FASTExpression? = null,
    var rangeEnd: FASTExpression? = null,
    var iterable: FASTExpression? = null,
    var body: FASTBody? = null
) : FASTLoopStructure() {
    override fun clone() = FASTForLoop(varName, rangeBegin, rangeEnd, iterable, body)
    override fun consume(node: FASTNode) {
        when (node) {
            is FASTToken<*> -> if (node.token is Identifier)
                this.varName = node.token
            else
                throw IllegalArgumentException("Unexpected token type ${node.token.javaClass.simpleName} for loop iterator name")
            is FASTBody -> this.body = node
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

data class FASTWhileLoop(
    var cond: FASTExpression? = null,
    var body: FASTBody? = null
) : FASTLoopStructure() {
    override fun clone() = FASTWhileLoop(cond, body)
    override fun consume(node: FASTNode) {
        when (node) {
            is FASTExpression -> this.cond = node
            is FASTBody -> this.body = node
            else -> throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
    }
}

