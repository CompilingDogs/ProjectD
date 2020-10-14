package com.compilingdogs.interpretation.value.impl

import com.compilingdogs.interpretation.value.Value

class TupleElement(val key: String, override val value: Value) : Value {
    override fun clone(): Value? {
        return TupleElement(key, value.clone()!!)
    }

    override fun toString(): String {
        return "$key = ${value.value}"
    }
}