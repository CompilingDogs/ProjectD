package com.compilingdogs.interpretation.value.impl

import com.compilingdogs.interpretation.value.Value

class ArrValue : Value {

    override var value: MutableList<Value>

    constructor(values: List<Value>) {
        value = values.toMutableList()
    }

    constructor(values: Array<Value>) {
        value = mutableListOf(*values)
    }

    override fun toString(): String {
        return "[${value.map { it.value }.joinToString(", ")}]"
    }

    fun update(idx: Int, value: Value) {
        this.value[idx] = value
    }


}