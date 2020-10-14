package com.compilingdogs.interpretation.value.impl

import com.compilingdogs.interpretation.value.Value

class ArrValue(override val value: MutableMap<Int, Value> = HashMap()) : Value {

    constructor(elements: List<ArrElement>) : this() {
        elements.forEach {
            this.value[it.key] = it.value
        }
    }

    override fun clone(): Value? {
        val arrElements = value.entries.map { ArrElement(it.key, it.value.clone()!!) }
        return ArrValue(arrElements)
    }

    override fun toString(): String {
        return "{${value.entries.map { ArrElement(it.key, it.value) }.joinToString(", ")}}"
    }

    fun update(key: Int, value: Value) {
        this.value[key] = value
    }

}
