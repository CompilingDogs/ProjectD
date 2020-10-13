package com.compilingdogs.interpretation.value

class Runtime {
    val symbolTable: HashMap<String, Value> = HashMap()

    fun getValue(identifier: String): Value? {
        return symbolTable[identifier]
    }

    fun register(identifier: String, value: Value) {
        if (symbolTable.containsKey(identifier)) symbolTable.replace(
            identifier,
            value
        ) else symbolTable[identifier] = value
    }
}