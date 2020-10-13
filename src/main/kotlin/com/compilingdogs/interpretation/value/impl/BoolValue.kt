package com.compilingdogs.interpretation.value.impl

import com.compilingdogs.interpretation.value.Value

class BoolValue(override val value: Boolean) : Value {
    override fun toString(): String {
        return value.toString()
    }
}