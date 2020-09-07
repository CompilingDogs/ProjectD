package com.compilingdogs.parser.ast


open class ConcatenationNode(
        val children: MutableList<ASTNode> = ArrayList()
) : ASTNode() {

    operator fun ASTNode.unaryPlus() = children.add(this)


    override fun match(parentNode: FASTNode?): FASTNode? {
        // If this node contains its own mapped FASTNode, use it.
        // If not, propagate parent FASTNode instead.
        val fastNode = attachedTo?.newInstance() ?: parentNode.clone()

        for (child in children) {
            // Do creation stuff
            child.createCallbacks.forEach {
                it(fastNode, child)
            }
            // Try to match the AST node
            val m = child.match(fastNode)

            // If child did not match, abort
            if (m == null)
                return null

            // If match was successful, fire appropriate callbacks
            child.successCallbacks.forEach {
                it(fastNode, child)
            }
        }

        return this
    }
}

fun concat(init: ConcatenationNode.() -> Unit): ConcatenationNode = initialize(init)