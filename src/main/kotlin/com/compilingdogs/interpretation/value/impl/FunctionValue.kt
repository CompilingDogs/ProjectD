package com.compilingdogs.interpretation.value.impl

import com.compilingdogs.interpretation.value.Runtime
import com.compilingdogs.interpretation.value.Value
import com.compilingdogs.parser.FASTBody
import com.compilingdogs.parser.FASTExpression
import java.util.stream.Collectors

class FunctionValue(private val arguments: List<String>, override val value: FASTBody, private val isLambda: Boolean) : Value {

    fun call(runtime: Runtime, args: MutableList<FASTExpression>): Value?{
        return if (isLambda){
            val newRuntime = runtime.clone()
            args.forEachIndexed { index, fastExpression ->  newRuntime.register(arguments[index], fastExpression.evaluate(runtime))}
            val result = this.value.statements[0].evaluate(newRuntime)
            runtime.merge(newRuntime)
            runtime.stopped = false
            result
        } else {
            val newRuntime = runtime.clone()
            args.forEachIndexed { index, fastExpression ->  newRuntime.register(arguments[index], fastExpression.evaluate(runtime))}
            value.evaluate(newRuntime)
        }
    }

    override fun clone(): Value? {
        val newArgs = arguments.stream().map { el: String -> el }.collect(Collectors.toList())
        val newBody = value.clone()
        return FunctionValue(newArgs, newBody, isLambda)
    }

    override fun toString(): String {
        return "func($arguments)"
    }


}