package com.compilingdogs.interpretation.value.impl

import com.compilingdogs.interpretation.value.NumericValue
import com.compilingdogs.interpretation.value.Value
import java.math.BigInteger

class IntValue(override val value: BigInteger) : Value, NumericValue