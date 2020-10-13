package com.compilingdogs.interpretation.value.impl

import com.compilingdogs.interpretation.value.Value

class TupleValue(override val value: MutableMap<String, Value> = HashMap()) : Value {

    constructor(elements: List<TupleElement>) : this() {
        elements.forEach {
            this.value[it.key] = it.value
        }
    }

    override fun toString(): String {
        return "{${value.entries.map { TupleElement(it.key, it.value) }.joinToString(", ")}}"
    }

}