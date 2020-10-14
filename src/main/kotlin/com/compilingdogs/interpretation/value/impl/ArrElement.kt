package com.compilingdogs.interpretation.value.impl

import com.compilingdogs.interpretation.value.Value

class ArrElement (val key: Int, override val value: Value) : Value {
    override fun clone(): Value? {
        return ArrElement(key, value.clone()!!)
    }

    override fun toString(): String {
        return "$key = ${value.value}"
    }
}