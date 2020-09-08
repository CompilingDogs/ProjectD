package com.compilingdogs.parser


fun <T> List<T>.subList(fromIndex: Int): List<T> {
    return subList(fromIndex, size)
}
