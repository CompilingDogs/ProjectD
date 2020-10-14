package com.compilingdogs.parser

import com.compilingdogs.interpretation.exception.InterpretationException
import com.compilingdogs.interpretation.value.NumericValue
import com.compilingdogs.interpretation.value.Runtime
import com.compilingdogs.interpretation.value.Value
import com.compilingdogs.interpretation.value.impl.*
import com.compilingdogs.parser.ast.FASTNode
import tokens.Identifier
import tokens.Literal
import tokens.Token
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.abs

class FASTToken<T : Token>(
    val token: T
) : FASTExpression() {
    override fun clone(): FASTNode {
        return FASTToken(token)
    }

    override fun consume(node: FASTNode) {
        throw NotImplementedError("Consume not applicable to " + this::class.simpleName)
    }

    override fun evaluate(runtime: Runtime): Value? {
        return runtime.getValue(token.token) ?: throw InterpretationException(
            "Unresolved reference ${this.token.token} at line ${this.token.line} column ${this.token.column}"
        )
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

    override fun evaluate(runtime: Runtime): Value? {
        return statements.map {
            if (!runtime.stopped)
                it.evaluate(runtime) ?: IntValue(BigInteger.ZERO)
            else
                null
        }.findLast { true }
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

    override fun evaluate(runtime: Runtime): Value? {
        this.definitions.forEach { it.evaluate(runtime) }
        return null
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

    override fun evaluate(runtime: Runtime): Value? {
        val value = this.value!!.evaluate(runtime)!!

        if (this.reference is FASTArrayReference)
            runtime.assignment(reference as FASTArrayReference, value)
        else
            runtime.register(reference!!.identifier!!.token, value)

        return value
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

    override fun evaluate(runtime: Runtime): Value? {
        println(
            values.map {
                it.evaluate(runtime)!!
            }.joinToString(", ")
        )

        return null
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

    override fun evaluate(runtime: Runtime): Value? {
        runtime.stopped = true
        if (this.value == null) {
            return null
        }
        return this.value!!.evaluate(runtime)
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

    override fun evaluate(runtime: Runtime): Value? {
        val value = this.value?.evaluate(runtime)
        runtime.register(this.name!!.token, value)
        return null
    }

    override fun toString(): String {
        return "FASTVarDefinition($name = $value)"
    }
}

open class FASTReference(
    open var identifier: Identifier? = null,
    open var reference: FASTExpression? = null
) : FASTExpression() {
    override fun clone(): FASTNode {
        return FASTReference(identifier, reference)
    }

    override fun consume(node: FASTNode) {
        if (node is FASTToken<*> && node.token is Identifier)
            this.identifier = (node as FASTToken<Identifier>).token
        else if (node is FASTExpression)
            this.reference = node
        else
            throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
    }

    override fun toString(): String {
        return "FASTReference(identifier=$identifier, reference=$reference)"
    }

    override fun evaluate(runtime: Runtime): Value? {
        return runtime.getValue(identifier!!.token)
    }

}

class FASTArrayReference(
    override var identifier: Identifier? = null,
    override var reference: FASTExpression? = null
) : FASTReference(identifier, reference) {
    override fun clone(): FASTNode {
        return FASTArrayReference(identifier, reference)
    }

    override fun consume(node: FASTNode) {
        if (node is FASTToken<*> && node.token is Identifier)
            this.identifier = (node as FASTToken<Identifier>).token
        else if (node is FASTExpression)
            this.reference = node
        else
            throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
    }

    override fun toString(): String {
        return "FASTArrayReference(identifier=$identifier, reference=$reference)"
    }

    override fun evaluate(runtime: Runtime): Value? {
        var r: Value = StrValue("")
        try {
            r = reference!!.evaluate(runtime)!!
            val v = runtime.getValue(identifier!!.token)

            if (v is ArrValue && r is IntValue)
                return v.value[r.value.toInt()]
            else if (v is TupleValue && r is StrValue)
                return v.value[r.value]
            else
                throw Exception("ERROR")

        } catch (e: Exception) {
            throw InterpretationException(
                "Error: Could not interpret ${identifier!!.token}[${r.value}] at line ${identifier!!.line} and column ${identifier!!.column}"
            )
        }
    }

}

abstract class FASTExpression : FASTNode()

class FASTOrOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTOrOperator().also {
            it.left = left
            it.right = right
        }
    }

    override fun evaluate(runtime: Runtime): Value? {
        if (this.right == null) {
            return this.left!!.evaluate(runtime)
        } else {
            try {
                val left = left!!.evaluate(runtime) as BoolValue
                val right = right!!.evaluate(runtime) as BoolValue
                return BoolValue(
                    left.value or right.value
                )
            } catch (e: Exception) {
                throw InterpretationException("Something went wrong during execution of OR Operation")
            }
        }
    }

    override fun toString(): String {
        return "FASTOrOperator(left=$left, right=$right)"
    }
}

class FASTAndOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTAndOperator().also {
            it.left = left
            it.right = right
        }
    }

    override fun evaluate(runtime: Runtime): Value? {
        if (this.right == null) {
            return this.left!!.evaluate(runtime)
        } else {
            try {
                val left = left!!.evaluate(runtime) as BoolValue
                val right = right!!.evaluate(runtime) as BoolValue
                return BoolValue(
                    left.value and right.value
                )
            } catch (e: Exception) {
                throw InterpretationException("Something went wrong during execution of OR Operation")
            }
        }
    }

    override fun toString(): String {
        return "FASTAndOperator(left=$left, right=$right)"
    }
}

class FASTXorOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTXorOperator().also {
            it.left = left
            it.right = right
        }
    }

    override fun evaluate(runtime: Runtime): Value? {
        if (this.right == null) {
            return this.left!!.evaluate(runtime)
        } else {
            try {
                val left = left!!.evaluate(runtime) as BoolValue
                val right = right!!.evaluate(runtime) as BoolValue
                return BoolValue(
                    left.value xor right.value
                )
            } catch (e: Exception) {
                throw InterpretationException("Something went wrong during execution of OR Operation")
            }
        }
    }

    override fun toString(): String {
        return "FASTXorOperator(left=$left, right=$right)"
    }
}


class FASTLessOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTLessOperator().also {
            it.left = left
            it.right = right
        }
    }

    override fun evaluate(runtime: Runtime): Value? {
        if (this.right == null) {
            return this.left!!.evaluate(runtime)
        } else {
            try {
                val left = left!!.evaluate(runtime)
                val right = right!!.evaluate(runtime)

                if (left is IntValue && right is IntValue) {
                    return BoolValue(
                        left.value.toLong() < right.value.toLong()
                    )
                } else if (left is NumericValue && right is NumericValue) {
                    val l =
                        if (left is IntValue) BigDecimal(left.value.toLong()) else left as BigDecimal
                    val r =
                        if (right is IntValue) BigDecimal(right.value.toLong()) else right as BigDecimal

                    return BoolValue(
                        l.toDouble() < r.toDouble()
                    )
                } else {
                    throw InterpretationException(
                        "Less Operation is not applicable to $left. $right"
                    )
                }
            } catch (e: Exception) {
                throw InterpretationException("Something went wrong during execution of Less Operation: " + e.message)
            }
        }
    }

    override fun toString(): String {
        return "FASTLessOperator(left=$left, right=$right)"
    }
}

class FASTLessEqualOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTLessEqualOperator().also {
            it.left = left
            it.right = right
        }
    }

    override fun evaluate(runtime: Runtime): Value? {
        if (this.right == null) {
            return this.left!!.evaluate(runtime)
        } else {
            try {
                val left = left!!.evaluate(runtime)
                val right = right!!.evaluate(runtime)

                if (left is IntValue && right is IntValue) {
                    return BoolValue(
                        left.value.toLong() <= right.value.toLong()
                    )
                } else if (left is NumericValue && right is NumericValue) {
                    val l =
                        if (left is IntValue) BigDecimal(left.value.toLong()) else left as BigDecimal
                    val r =
                        if (right is IntValue) BigDecimal(right.value.toLong()) else right as BigDecimal

                    return BoolValue(
                        l.toDouble() <= r.toDouble()
                    )
                } else {
                    throw InterpretationException(
                        "Less Or Equal Operation is not applicable to $left. $right"
                    )
                }
            } catch (e: Exception) {
                throw InterpretationException("Something went wrong during execution of LEQ Operation: " + e.message)
            }
        }
    }

    override fun toString(): String {
        return "FASTLessEqualOperator(left=$left, right=$right)"
    }
}

class FASTGreaterOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTGreaterOperator().also {
            it.left = left
            it.right = right
        }
    }

    override fun evaluate(runtime: Runtime): Value? {
        if (this.right == null) {
            return this.left!!.evaluate(runtime)
        } else {
            try {
                val left = left!!.evaluate(runtime)
                val right = right!!.evaluate(runtime)

                if (left is IntValue && right is IntValue) {
                    return BoolValue(
                        left.value.toLong() > right.value.toLong()
                    )
                } else if (left is NumericValue && right is NumericValue) {
                    val l =
                        if (left is IntValue) BigDecimal(left.value.toLong()) else left as BigDecimal
                    val r =
                        if (right is IntValue) BigDecimal(right.value.toLong()) else right as BigDecimal

                    return BoolValue(
                        l.toDouble() > r.toDouble()
                    )
                } else {
                    throw InterpretationException(
                        "Greater Operation is not applicable to $left. $right"
                    )
                }
            } catch (e: Exception) {
                throw InterpretationException("Something went wrong during execution of Greater Operation: " + e.message)
            }
        }
    }

    override fun toString(): String {
        return "FASTGreaterOperator(left=$left, right=$right)"
    }
}

class FASTGreaterEqualOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTGreaterEqualOperator().also {
            it.left = left
            it.right = right
        }
    }

    override fun evaluate(runtime: Runtime): Value? {
        if (this.right == null) {
            return this.left!!.evaluate(runtime)
        } else {
            try {
                val left = left!!.evaluate(runtime)
                val right = right!!.evaluate(runtime)

                if (left is IntValue && right is IntValue) {
                    return BoolValue(
                        left.value.toLong() >= right.value.toLong()
                    )
                } else if (left is NumericValue && right is NumericValue) {
                    val l =
                        if (left is IntValue) BigDecimal(left.value.toLong()) else left as BigDecimal
                    val r =
                        if (right is IntValue) BigDecimal(right.value.toLong()) else right as BigDecimal

                    return BoolValue(
                        l.toDouble() >= r.toDouble()
                    )
                } else {
                    throw InterpretationException(
                        "Greater or Equal Operation is not applicable to $left. $right"
                    )
                }
            } catch (e: Exception) {
                throw InterpretationException("Something went wrong during execution of GEQ Operation: " + e.message)
            }
        }
    }

    override fun toString(): String {
        return "FASTGreaterEqualOperator(left=$left, right=$right)"
    }
}

class FASTEqualOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTEqualOperator().also {
            it.left = left
            it.right = right
        }
    }

    override fun evaluate(runtime: Runtime): Value? {
        if (this.right == null) {
            return this.left!!.evaluate(runtime)
        } else {
            try {
                val left = left!!.evaluate(runtime)
                val right = right!!.evaluate(runtime)

                if (left is IntValue && right is IntValue) {
                    return BoolValue(
                        left.value.toLong() == right.value.toLong()
                    )
                } else if (left is NumericValue && right is NumericValue) {
                    val l =
                        if (left is IntValue) BigDecimal(left.value.toLong()) else left as BigDecimal
                    val r =
                        if (right is IntValue) BigDecimal(right.value.toLong()) else right as BigDecimal

                    return BoolValue(
                        l.toDouble() == r.toDouble()
                    )
                } else {
                    throw InterpretationException(
                        "Equal Operation is not applicable to $left. $right"
                    )
                }
            } catch (e: Exception) {
                throw InterpretationException("Something went wrong during execution of Equal Operation: " + e.message)
            }
        }
    }

    override fun toString(): String {
        return "FASTEqualOperator(left=$left, right=$right)"
    }
}

class FASTNotEqualOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTNotEqualOperator().also {
            it.left = left
            it.right = right
        }
    }

    override fun evaluate(runtime: Runtime): Value? {
        if (this.right == null) {
            return this.left!!.evaluate(runtime)
        } else {
            try {
                val left = left!!.evaluate(runtime)
                val right = right!!.evaluate(runtime)

                if (left is IntValue && right is IntValue) {
                    return BoolValue(
                        left.value.toLong() != right.value.toLong()
                    )
                } else if (left is NumericValue && right is NumericValue) {
                    val l =
                        if (left is IntValue) BigDecimal(left.value.toLong()) else left as BigDecimal
                    val r =
                        if (right is IntValue) BigDecimal(right.value.toLong()) else right as BigDecimal

                    return BoolValue(
                        l.toDouble() != r.toDouble()
                    )
                } else {
                    throw InterpretationException(
                        "Not Equal Operation is not applicable to $left. $right"
                    )
                }
            } catch (e: Exception) {
                throw InterpretationException("Something went wrong during execution of Not Equal Operation: " + e.message)
            }
        }
    }

    override fun toString(): String {
        return "FASTNotEqualOperator(left=$left, right=$right)"
    }
}


class FASTAddOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTAddOperator().also {
            it.left = left
            it.right = right
        }
    }

    override fun evaluate(runtime: Runtime): Value? {
        if (this.right == null) {
            return this.left!!.evaluate(runtime)
        }

        val left = this.left!!.evaluate(runtime)
        val right = this.right!!.evaluate(runtime)

        if (left is IntValue && right is IntValue) {
            return IntValue(
                left.value.add(right.value)
            )
        } else if (left is StrValue && right is StrValue) {
            return StrValue(
                left.value + right.value
            )
        } else if (left is NumericValue && right is NumericValue) {
            val lval =
                if (left is IntValue) DecValue(BigDecimal.valueOf(left.value.longValueExact())) else left as DecValue
            val rval =
                if (right is IntValue) DecValue(BigDecimal.valueOf(right.value.longValueExact())) else right as DecValue

            return DecValue(lval.value.add(rval.value))
        } else if (left is TupleValue && right is TupleValue) {
            val r = left.value + right.value
            return TupleValue(r.toMutableMap())
        } else if (left is ArrValue && right is ArrValue) {
            val res = left.value + right.value
            return ArrValue(res.toMutableMap())
        } else {
            throw InterpretationException("Error in Addition: Unsupported operand types")
        }
    }

    override fun toString(): String {
        return "FASTAddOperator(left=$left, right=$right)"
    }
}

class FASTSubtractOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTSubtractOperator().also {
            it.left = left
            it.right = right
        }
    }


    override fun evaluate(runtime: Runtime): Value? {
        if (this.right == null) {
            return this.left!!.evaluate(runtime)
        }

        val left = this.left!!.evaluate(runtime)
        val right = this.right!!.evaluate(runtime)

        return if (left is IntValue && right is IntValue) {
            IntValue(
                left.value.subtract(right.value)
            )
        } else if (left is NumericValue && right is NumericValue) {
            val lval =
                if (left is IntValue) DecValue(BigDecimal.valueOf(left.value.longValueExact())) else left as DecValue
            val rval =
                if (right is IntValue) DecValue(BigDecimal.valueOf(right.value.longValueExact())) else right as DecValue

            DecValue(lval.value.subtract(rval.value))
        } else {
            throw InterpretationException("Error in Subtraction: Unsupported operand types")
        }
    }

    override fun toString(): String {
        return "FASTSubtractOperator(left=$left, right=$right)"
    }
}

class FASTMultiplyOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTMultiplyOperator().also {
            it.left = left
            it.right = right
        }
    }

    override fun evaluate(runtime: Runtime): Value? {
        if (this.right == null) {
            return this.left!!.evaluate(runtime)
        }

        val left = this.left!!.evaluate(runtime)
        val right = this.right!!.evaluate(runtime)

        return if (left is IntValue && right is IntValue) {
            IntValue(
                left.value.multiply(right.value)
            )
        } else if (left is NumericValue && right is NumericValue) {
            val lval =
                if (left is IntValue) DecValue(BigDecimal.valueOf(left.value.longValueExact())) else left as DecValue
            val rval =
                if (right is IntValue) DecValue(BigDecimal.valueOf(right.value.longValueExact())) else right as DecValue

            DecValue(lval.value.multiply(rval.value))
        } else {
            throw InterpretationException("Error in Multiplication: Unsupported operand types")
        }
    }

    override fun toString(): String {
        return "FASTMultiplyOperator(left=$left, right=$right)"
    }
}

class FASTDivideOperator : FASTBinaryOperator() {
    override fun clone(): FASTNode {
        return FASTDivideOperator().also {
            it.left = left
            it.right = right
        }
    }

    override fun evaluate(runtime: Runtime): Value? {
        if (this.right == null) {
            return this.left!!.evaluate(runtime)
        }

        val left = this.left!!.evaluate(runtime)
        val right = this.right!!.evaluate(runtime)

        return if (left is IntValue && right is IntValue) {
            IntValue(
                left.value.divide(right.value)
            )
        } else if (left is NumericValue && right is NumericValue) {
            val lval =
                if (left is IntValue) DecValue(BigDecimal.valueOf(left.value.longValueExact())) else left as DecValue
            val rval =
                if (right is IntValue) DecValue(BigDecimal.valueOf(right.value.longValueExact())) else right as DecValue

            DecValue(lval.value.divide(rval.value))
        } else {
            throw InterpretationException("Error in Division: Unsupported operand types")
        }
    }

    override fun toString(): String {
        return "FASTDivideOperator(left=$left, right=$right)"
    }
}

class FASTPositiveOperator : FASTUnaryOperator() {
    override fun clone(): FASTNode {
        return FASTPositiveOperator().also {
            it.value = value
        }
    }

    override fun evaluate(runtime: Runtime): Value? {
        val v = this.value!!.evaluate(runtime)

        return if (v is NumericValue) {
            if (v is IntValue) {
                IntValue(BigInteger.valueOf(abs(v.value.toLong())))
            } else {
                DecValue(BigDecimal.valueOf(abs((v as DecValue).value.toDouble())))
            }
        } else {
            throw InterpretationException("Unary Operator + is not applicable to $v")
        }

    }

    override fun toString(): String {
        return "FASTPositiveOperator(value=$value)"
    }
}

class FASTNegativeOperator : FASTUnaryOperator() {
    override fun clone(): FASTNode {
        return FASTNegativeOperator().also {
            it.value = value
        }
    }

    override fun evaluate(runtime: Runtime): Value? {
        val v = this.value!!.evaluate(runtime)

        return if (v is NumericValue) {
            if (v is IntValue) {
                IntValue(BigInteger.valueOf(-(v.value.toLong())))
            } else {
                DecValue(BigDecimal.valueOf(-((v as DecValue).value.toDouble())))
            }
        } else {
            throw InterpretationException("Unary Operator + is not applicable to $v")
        }

    }

    override fun toString(): String {
        return "FASTNegativeOperator(value=$value)"
    }

}

class FASTNotOperator : FASTUnaryOperator() {
    override fun clone(): FASTNode {
        return FASTNotOperator().also {
            it.value = value
        }
    }

    override fun evaluate(runtime: Runtime): Value? {
        val v = this.value!!.evaluate(runtime)

        return if (v is BoolValue) {
            BoolValue(!v.value)
        } else {
            throw InterpretationException("Unary Operator + is not applicable to $v")
        }

    }

    override fun toString(): String {
        return "FASTNotOperator(value=$value)"
    }

}

abstract class FASTBinaryOperator(
    var left: FASTExpression? = null,
    var right: FASTExpression? = null
) : FASTExpression() {
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
) : FASTExpression() {
    override fun consume(node: FASTNode) {
        if (node is FASTExpression) {
            this.value = node
        } else {
            throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
        }
    }
}

abstract class FASTTypeIndicator(val name: String) : FASTExpression() {
    override fun consume(node: FASTNode) {
       return
    }
}

class FASTTypeIndicatorInt : FASTTypeIndicator("int") {
    override fun clone(): FASTNode {
        return FASTTypeIndicatorInt()
    }

    override fun evaluate(runtime: Runtime): Value? {
        return IntValue(BigInteger.ZERO)
    }

    override fun toString(): String {
        return "FASTTypeIndicatorInt"
    }
}

class FASTTypeIndicatorReal : FASTTypeIndicator("real") {
    override fun clone(): FASTNode {
        return FASTTypeIndicatorReal()
    }

    override fun evaluate(runtime: Runtime): Value? {
        return DecValue(BigDecimal.ZERO)
    }

    override fun toString(): String {
        return "FASTTypeIndicatorReal"
    }
}

class FASTTypeIndicatorBool : FASTTypeIndicator("bool") {
    override fun clone(): FASTNode {
        return FASTTypeIndicatorBool()
    }

    override fun evaluate(runtime: Runtime): Value? {
        return BoolValue(false)
    }

    override fun toString(): String {
        return "FASTTypeIndicatorBool"
    }
}

class FASTTypeIndicatorString : FASTTypeIndicator("string") {
    override fun clone(): FASTNode {
        return FASTTypeIndicatorString()
    }

    override fun evaluate(runtime: Runtime): Value? {
        return StrValue("")
    }

    override fun toString(): String {
        return "FASTTypeIndicatorString"
    }
}

class FASTTypeIndicatorEmpty : FASTTypeIndicator("empty") {
    override fun clone(): FASTNode {
        return FASTTypeIndicatorEmpty()
    }

    override fun evaluate(runtime: Runtime): Value? {
        return null
    }

    override fun toString(): String {
        return "FASTTypeIndicatorEmpty"
    }
}

class FASTTypeIndicatorArray : FASTTypeIndicator("[]") {
    override fun clone(): FASTNode {
        return FASTTypeIndicatorArray()
    }

    override fun evaluate(runtime: Runtime): Value? {
        return ArrValue(ArrayList())
    }

    override fun toString(): String {
        return "FASTTypeIndicatorArray"
    }
}

class FASTTypeIndicatorTuple : FASTTypeIndicator("{}") {
    override fun clone(): FASTNode {
        return FASTTypeIndicatorTuple()
    }

    override fun evaluate(runtime: Runtime): Value? {
        return TupleValue(ArrayList())
    }

    override fun toString(): String {
        return "FASTTypeIndicatorTuple"
    }
}

// todo
class FASTTypeIndicatorFunc : FASTTypeIndicator("func") {
    override fun clone(): FASTNode {
        return FASTTypeIndicatorFunc()
    }

    override fun evaluate(runtime: Runtime): Value? {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "FASTTypeIndicatorFunc"
    }
}


data class FASTFunctionLiteral(
    var args: MutableList<Identifier> = mutableListOf(),
    var body: FASTBody? = null
) : FASTExpression() {
    override fun clone() = FASTFunctionLiteral(args.toMutableList(), body?.clone())
    override fun consume(node: FASTNode) {
        if (node is FASTTypeIndicatorFunc)
            return

        if (node is FASTToken<*> && node.token is Identifier)
            this.args.add((node as FASTToken<Identifier>).token)
        else if (node is FASTBody)
            this.body = node
        else
            throw IllegalArgumentException("Argument of type " + node::class.simpleName + " not supported")
    }

    override fun evaluate(runtime: Runtime): Value? {
        TODO("Not yet implemented")
    }
}


class FASTReadIntCall : FASTExpression() {
    override fun clone() = FASTReadIntCall()

    override fun consume(node: FASTNode) {
        throw IllegalStateException("Not implemented for this node")
    }

    override fun evaluate(runtime: Runtime): Value? {
        return runtime.readInt()
    }

    override fun toString(): String {
        return "FASTReadIntCall"
    }
}

class FASTReadRealCall : FASTExpression() {
    override fun clone() = FASTReadRealCall()

    override fun consume(node: FASTNode) {
        throw IllegalStateException("Not implemented for this node")
    }

    override fun evaluate(runtime: Runtime): Value? {
        return runtime.readReal()
    }

    override fun toString(): String {
        return "FASTReadRealCall"
    }
}

class FASTReadStringCall : FASTExpression() {
    override fun clone() = FASTReadStringCall()

    override fun consume(node: FASTNode) {
        throw IllegalStateException("Not implemented for this node")
    }

    override fun evaluate(runtime: Runtime): Value? {

        return runtime.readString()
    }

    override fun toString(): String {
        return "FASTReadStringCall"
    }
}


data class FASTFunctionCall(
    var args: MutableList<FASTExpression> = mutableListOf()
) : FASTExpression() {
    override fun clone() = FASTFunctionCall(args.toMutableList())

    override fun consume(node: FASTNode) {
        when (node) {
            is FASTExpression -> args.add(node)
            else -> throw IllegalArgumentException()
        }
    }

    override fun evaluate(runtime: Runtime): Value? {
        TODO("Not yet implemented")
    }
}

class FASTTypeCheckOperator : FASTBinaryOperator() {
    override fun clone() = FASTTypeCheckOperator().also {
        it.left = left
        it.right = right
    }

    override fun evaluate(runtime: Runtime): Value? {
        if (right == null)
            return left!!.evaluate(runtime)

        if (left!!.evaluate(runtime) == null && !(right is FASTTypeIndicatorEmpty)) {
            return BoolValue(
                false
            )
        }

        if (right is FASTTypeIndicatorEmpty)
            return BoolValue(
                left!!.evaluate(runtime) == null
            )

        return BoolValue(
            left!!.evaluate(runtime)!!::class == right!!.evaluate(runtime)!!::class
        )
    }

    override fun toString(): String {
        return "FASTTypeCheckOperator(left=$left, right=$right)"
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

    override fun evaluate(runtime: Runtime): Value? {
        return statements.map {
            if (!runtime.stopped)
                it.evaluate(runtime) ?: IntValue(BigInteger.ZERO)
            else
                null
        }.findLast { true }
    }
}

data class FASTIntegerLiteral(
    var value: Int? = null
) : FASTExpression() {
    constructor(token: Token) : this(token.token.toInt())

    override fun clone() = FASTIntegerLiteral(value)
    override fun consume(node: FASTNode) {
        throw NotImplementedError("Consume not applicable to " + this::class.simpleName)
    }

    override fun evaluate(runtime: Runtime): Value? {
        return IntValue(BigInteger.valueOf(this.value!!.toLong()))
    }
}

data class FASTRealLiteral(
    var value: Float? = null
) : FASTExpression() {
    constructor(token: Token) : this(token.token.toFloat())

    override fun clone() = FASTRealLiteral(value)
    override fun consume(node: FASTNode) {
        throw NotImplementedError("Consume not applicable to " + this::class.simpleName)
    }

    override fun evaluate(runtime: Runtime): Value? {
        return DecValue(BigDecimal.valueOf(this.value!!.toDouble()))
    }
}

data class FASTStringLiteral(
    var value: String? = null
) : FASTExpression() {
    constructor(token: Token) : this((token as Literal.StringLiteral).value)

    override fun clone() = FASTStringLiteral(value)
    override fun consume(node: FASTNode) {
        throw NotImplementedError("Consume not applicable to " + this::class.simpleName)
    }

    override fun evaluate(runtime: Runtime): Value? {
        return StrValue(this.value!!)
    }
}

data class FASTBooleanLiteral(
    var value: Boolean? = null
) : FASTExpression() {
    constructor(token: Token) : this(token.token.toBoolean())

    override fun clone() = FASTBooleanLiteral(value)
    override fun consume(node: FASTNode) {
        throw NotImplementedError("Consume not applicable to " + this::class.simpleName)
    }

    override fun evaluate(runtime: Runtime): Value? {
        return BoolValue(this.value!!)
    }
}

data class FASTArrayLiteral(
    var members: MutableList<FASTExpression> = mutableListOf()
) : FASTExpression() {

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

    override fun evaluate(runtime: Runtime): Value? {
        return ArrValue(this.members.mapIndexed { index, fastExpression -> ArrElement(index + 1, fastExpression.evaluate(runtime)!!) }.toList())
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

    override fun evaluate(runtime: Runtime): Value? {
        return TupleValue(
            this.members.map { it.evaluate(runtime) as TupleElement }
        )
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

    override fun evaluate(runtime: Runtime): Value? {
        return TupleElement(name!!.token, value!!.evaluate(runtime)!!)
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

    override fun evaluate(runtime: Runtime): Value? {
        val newRuntime = runtime.clone()
        val condition = (condition!!.evaluate(newRuntime) as BoolValue).value

        val res = when {
            condition -> {
                body!!.evaluate(runtime)
            }
            elseBody != null -> {
                elseBody!!.evaluate(runtime)
            }
            else -> {
                null
            }
        }

        runtime.merge(newRuntime)
        return res
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

    override fun evaluate(runtime: Runtime): Value? {
        try {
            val newRuntime = runtime.clone()
            val results = ArrayList<Value?>()
            val varName = this.varName!!.token

            if (iterable != null){
                val iterable = this.iterable!!.evaluate(newRuntime)

                if (iterable is ArrValue){
                    iterable.value.forEach { (_, value) ->
                        run {
                            if (!newRuntime.stopped) {
                                newRuntime.register(varName, value)
                                results.add(body!!.evaluate(newRuntime))
                            }
                        }
                    }
                } else if (iterable is TupleValue){
                    iterable.value.forEach { (_, value) ->
                        run {
                            if (!newRuntime.stopped) {
                                newRuntime.register(varName, value)
                                results.add(body!!.evaluate(newRuntime))
                            }
                        }
                    }
                } else {
                    throw Exception("ERROR")
                }

            } else {
                var i = (rangeBegin!!.evaluate(runtime) as IntValue).value.toLong()
                val max = (rangeEnd!!.evaluate(runtime) as IntValue).value.toLong()

                while (i < max && !newRuntime.stopped){
                    newRuntime.register(varName, IntValue(BigInteger.valueOf(i)))
                    results.add(body!!.evaluate(newRuntime))
                    ++i
                }

            }

            runtime.merge(newRuntime)
            return results.findLast { true }
        } catch (e: Exception){
            throw InterpretationException("Condition for FOR loop is Incomplete at line $this.varName!!.line")
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

    override fun evaluate(runtime: Runtime): Value? {
        try {
            val newRuntime = runtime.clone()
            val results = ArrayList<Value?>()

            while ((cond!!.evaluate(newRuntime) as BoolValue).value && !newRuntime.stopped) {
                results.add(body!!.evaluate(newRuntime))
            }

            runtime.merge(newRuntime)
            return results.findLast { true }
        } catch (e: Exception){
            throw InterpretationException("Condition for while loop can not be empty")
        }
    }
}

