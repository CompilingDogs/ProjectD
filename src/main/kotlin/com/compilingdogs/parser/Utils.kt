package com.compilingdogs.parser


fun <T> List<T>.subList(fromIndex: Int): List<T> = subList(fromIndex, size)

val lightGray = "\u001B[37m"
val noColor = "\u001B[0m"

// Alternation node
val greenColor = "\u001b[32m"
// Concatenation node
val yellowColor = "\u001b[33m"
// Option node
val blueColor = "\u001b[34m"
// Repetition node
val magentaColor = "\u001b[35m"

fun indent(depth: Int): String =
    StringBuilder().apply {
        (0 until depth).forEach { _ ->
            append("${lightGray}┆${noColor} ")
        }
    }.toString()