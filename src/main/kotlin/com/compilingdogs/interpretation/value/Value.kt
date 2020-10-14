package com.compilingdogs.interpretation.value

interface Value {
    abstract fun clone(): Value?

    val value: Any?
}