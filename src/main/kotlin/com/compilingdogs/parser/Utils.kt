package com.compilingdogs.parser


fun <T> List<T>.subList(fromIndex: Int): List<T> = subList(fromIndex, size)

const val lightGray = "\u001B[37m"
const val noColor = "\u001B[0m"

// Alternation node
const val greenColor = "\u001b[32m"

// Concatenation node
const val yellowColor = "\u001b[33m"

// Option node
const val blueColor = "\u001b[34m"

// Repetition node
const val magentaColor = "\u001b[35m"

// Only works for 0..99
fun twoPlacesInt(i: Int): String {
    return if (i < 10)
        " $i"
    else
        i.toString()
}

fun indent(depth: Int): String =
    StringBuilder().apply {
        append(twoPlacesInt(depth))

        (0 until depth).forEach { _ ->
            append("${lightGray}┆${noColor} ")
        }
    }.toString()

/**
 * Executes the given [block] and returns elapsed time in milliseconds.
 */
inline fun measureTimeMillis(block: () -> Unit): Long {
    val start = System.currentTimeMillis()
    block()
    return System.currentTimeMillis() - start
}