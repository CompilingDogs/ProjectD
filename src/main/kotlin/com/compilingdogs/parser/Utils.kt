package com.compilingdogs.parser


fun <T> List<T>.subList(fromIndex: Int): List<T> = subList(fromIndex, size)

val lightGray = "\u001B[37m"
val noColor = "\u001B[0m"

fun indent(depth: Int): String =
    StringBuilder().apply {
        (0..depth).forEach { _ ->
            append("${lightGray}┆${noColor} ")
        }
    }.toString()