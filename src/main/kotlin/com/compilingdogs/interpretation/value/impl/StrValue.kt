package com.compilingdogs.interpretation.value.impl

import com.compilingdogs.interpretation.value.Value

class StrValue(override val value: String) : Value {
    override fun toString(): String {
        return value.toString()
    }
}