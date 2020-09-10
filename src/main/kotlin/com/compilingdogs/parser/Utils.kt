package com.compilingdogs.parser


fun <T> List<T>.subList(fromIndex: Int): List<T> = subList(fromIndex, size)

fun indent(depth: Int): String =
    StringBuilder().apply {
        (0..depth).forEach { _ ->
            append("  ")
        }
    }.toString()