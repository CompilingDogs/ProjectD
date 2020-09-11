package com.compilingdogs.parser.ast

/**
 * FAST stands for Final Abstract Syntax Tree :)
 */

abstract class FASTNode {
    abstract fun clone(): FASTNode
}
