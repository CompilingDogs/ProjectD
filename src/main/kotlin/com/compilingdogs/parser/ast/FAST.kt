package com.compilingdogs.parser.ast

import com.compilingdogs.interpretation.value.Runtime
import com.compilingdogs.interpretation.value.Value

/**
 * FAST stands for Final Abstract Syntax Tree :)
 */

abstract class FASTNode {
    abstract fun clone(): FASTNode
    abstract fun consume(node: FASTNode)
    abstract fun evaluate(runtime: Runtime): Value?
}
