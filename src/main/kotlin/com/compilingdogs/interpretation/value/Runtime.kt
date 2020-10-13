package com.compilingdogs.interpretation.value

import com.compilingdogs.interpretation.exception.InterpretationException
import com.compilingdogs.interpretation.value.impl.*
import com.compilingdogs.parser.FASTMemberReferenceType.BRACKETS_REFERENCE
import com.compilingdogs.parser.FASTMemberReferenceType.DOT_REFERENCE
import com.compilingdogs.parser.FASTReference
import java.util.*
import kotlin.collections.HashMap

class Runtime {
    val symbolTable: HashMap<String, Value> = HashMap()
    val scanner = Scanner(System.`in`).useLocale(Locale.ENGLISH)

    fun getValue(identifier: String): Value? {
        return symbolTable[identifier]
    }

    fun register(identifier: String, value: Value) {
        if (symbolTable.containsKey(identifier)) symbolTable.replace(
            identifier,
            value
        ) else symbolTable[identifier] = value
    }

    // todo: finish
    fun assignment(reference: FASTReference, value: Value): Value {
        val identifier = reference.identifier!!

        if (reference.references.size == 0) {
            this.symbolTable[identifier.token] = value
        } else {
            val r = reference.references[0].member!!.evaluate(this)

            if (reference.references.size > 1) {
                throw InterpretationException(
                    "Error in ${identifier.token} at line ${identifier.line} column ${identifier.column}: " +
                            "Sequences of references of length > 1 is not supported yet."
                )
            } else {

                if (reference.references[0].type == BRACKETS_REFERENCE && r is IntValue) {
                    (this.symbolTable[identifier.token] as ArrValue).value[r.value.toInt() - 1] =
                        value
                } else if (reference.references[0].type == DOT_REFERENCE && r is StrValue) {
                    (this.symbolTable[identifier.token] as TupleValue).value[r.value] = value
                } else {
                    throw InterpretationException(
                        "Error in ${identifier.token} at line ${identifier.line} column ${identifier.column}: " +
                                "reference ${r!!.value} is not applicable to reference of type ${reference.references[0].type}."
                    )
                }

            }
        }

        return value
    }

    fun readString(): StrValue {
        return StrValue(scanner.nextLine())
    }

    fun readReal(): DecValue {
        return DecValue(scanner.nextBigDecimal())
    }

    fun readInt(): IntValue {
        return IntValue(scanner.nextBigInteger())
    }
}