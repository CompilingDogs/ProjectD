package com.compilingdogs.interpretation.value.impl

import com.compilingdogs.interpretation.value.NumericValue
import com.compilingdogs.interpretation.value.Value
import java.math.BigDecimal

class DecValue(override val value: BigDecimal) : Value, NumericValue