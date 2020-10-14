package com.compilingdogs.interpretation.value

import com.compilingdogs.interpretation.exception.InterpretationException
import com.compilingdogs.interpretation.value.impl.*
import com.compilingdogs.parser.FASTArrayReference
import java.util.*
import kotlin.collections.HashMap

class Runtime {
    val symbolTable: HashMap<String, Value?> = HashMap()
    val scanner = Scanner(System.`in`).useLocale(Locale.ENGLISH)
    var stopped: Boolean = false

    fun getValue(identifier: String): Value? {
        return symbolTable[identifier]
    }

    fun register(identifier: String, value: Value?) {
        if (symbolTable.containsKey(identifier)) symbolTable.replace(
            identifier,
            value
        ) else symbolTable[identifier] = value
    }

    // todo: finish
    fun assignment(reference: FASTArrayReference, value: Value): Value {
        val identifier = reference.identifier!!
        val r = reference.reference!!.evaluate(this)

        try {
            if (r is IntValue && this.symbolTable[identifier.token] is ArrValue)
                (this.symbolTable[identifier.token] as ArrValue).update(r.value.toInt(), value)
            else if (r is StrValue && this.symbolTable[identifier.token] is TupleValue)
                (this.symbolTable[identifier.token] as TupleValue).update(r.value, value)
            else
                throw Exception("ERROR")
        } catch (e: Exception) {
            throw InterpretationException(
                "Error: Could not interpret ${identifier.token}[$r] at line ${identifier.line} and column ${identifier.column}"
            )
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

    fun clone(): Runtime {
        val newRuntime = Runtime()
        symbolTable.entries.forEach{
            newRuntime.register(it.key, it.value?.clone())
        }
        newRuntime.stopped = stopped
        return newRuntime
    }

    fun merge(other: Runtime) {
        this.stopped = other.stopped
        other.symbolTable.entries.forEach{
            if (this.symbolTable.containsKey(it.key))
                this.symbolTable.replace(it.key, it.value)
        }
    }
}