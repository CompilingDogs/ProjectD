package com.compilingdogs.parser

import com.compilingdogs.parser.ast.FASTNode
import tokens.Token

class FASTToken<T : Token>(
    val token: T
) : FASTNode() {
    override fun clone(): FASTNode {
        return FASTToken(token)
    }

    override fun toString(): String {
        return "FASTToken($token)"
    }
}

class FASTProgram(
    val statements: List<FASTStatement>
) : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

abstract class FASTStatement : FASTNode()

class FASTDeclarationStatement(
    val definitions: List<FASTVarDefinition>
) : FASTStatement() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTAssignmentStatement(
    var reference: FASTReference,
    var value: FASTExpression
) : FASTStatement() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTControlStatement(

) : FASTStatement() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTPrintStatement (

) : FASTStatement() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTReturnStatement (

) : FASTStatement() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTVarDefinition (
    var name: FASTIdentifier,
    var value: FASTExpression?
) : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTReference (

) : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTIdentifier (

) : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }
}

class FASTExpression (

) : FASTNode() {
    override fun clone(): FASTNode {
        TODO("Not yet implemented")
    }

}

class FASTRelation (

) : FASTNode()

class FASTFactor (

): FASTNode()

class FASTTerm (

) : FASTNode()

class FASTUnary (

) : FASTNode()

class FASTPrimary (

) : FASTNode()

class FASTTypeIndicator (

) : FASTNode()

class FASTFunctionLiteral (

) : FASTLiteral()

class FASTFunctionBody (
    var statement: List<FASTStatement>
) : FASTNode()

class FASTLiteral (

) : FASTNode()

class FASTIntegerLiteral (

) : FASTLiteral()

class FASTRealLiteral (

) : FASTLiteral()

class FASTStringLiteral (

) : FASTLiteral()

class FASTBooleanLiteral (

) : FASTLiteral()

class FASTEmptyLiteral (

) : FASTLiteral()

class FASTArrayLiteral(
    var members: MutableList<FASTNode> = mutableListOf()
) : FASTNode() {

    override fun clone(): FASTNode {
        return FASTArrayLiteral(members.toMutableList())
    }

    override fun toString(): String {
        return "FASTArrayLiteral($members)"
    }
}

class FASTTupleLiteral (

) : FASTLiteral()

class FASTTupleElement (

) : FASTNode()

class FASTIfStructure (

) : FASTControlStatement()

class FASTLoopStructure (

) : FASTControlStatement()

class FASTForLoop (

) : FASTLoopStructure()

class FASTWhileLoop (

) : FASTLoopStructure()

