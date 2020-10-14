package com.compilingdogs.interpretation.value.impl

import com.compilingdogs.interpretation.value.NumericValue
import com.compilingdogs.interpretation.value.Value
import java.math.BigDecimal

class DecValue(override val value: BigDecimal) : Value, NumericValue {
    override fun clone(): Value? {
        return DecValue(
            BigDecimal.valueOf(this.value.toDouble())
        )
    }

    override fun toString(): String {
        return value.toString()
    }
}