package com.compilingdogs.parser

import com.compilingdogs.parser.ast.*
import stages.LexicalAnalyzer
import tokens.*
import java.io.File

/**
 * Identifiers
 */
val identifier = TokenNode(Identifier::class.java)

/**
 * Keywords
 */
val varKeyword = TokenNode(Keyword.VarKeyword::class.java)
val int = TokenNode(Keyword.IntKeyword::class.java)
var real = TokenNode(Keyword.RealKeyword::class.java)
var bool = TokenNode(Keyword.BoolKeyword::class.java)
val string = TokenNode(Keyword.StringKeyword::class.java)
var empty = TokenNode(Keyword.EmptyKeyword::class.java)
var func = TokenNode(Keyword.FuncKeyword::class.java)
val readInt = TokenNode(Keyword.ReadIntKeyword::class.java)
val readReal = TokenNode(Keyword.ReadRealKeyword::class.java)
val readString = TokenNode(Keyword.ReadStringKeyword::class.java)
val end = TokenNode(Keyword.EndKeyword::class.java)
val print = TokenNode(Keyword.PrintKeyword::class.java)
val returnKeyword = TokenNode(Keyword.ReturnKeyword::class.java)
val ifKeyword = TokenNode(Keyword.IfKeyword::class.java)
val thenKeyword = TokenNode(Keyword.ThenKeyword::class.java)
val elseKeyword = TokenNode(Keyword.ElseKeyword::class.java)
val forKeyword = TokenNode(Keyword.ForKeyword::class.java)
val inKeyword = TokenNode(Keyword.InKeyword::class.java)
val loopKeyword = TokenNode(Keyword.LoopKeyword::class.java)
val whileKeyword = TokenNode(Keyword.WhileKeyword::class.java)

/**
 * Operators
 */
val assignmentOperator = TokenNode(Operator.AssignmentOperator::class.java)
val or = TokenNode(Operator.LogicOrOperator::class.java)
val and = TokenNode(Operator.LogicAndOperator::class.java)
val xor = TokenNode(Operator.LogicXorOperator::class.java)
val not = TokenNode(Operator.LogicNotOperator::class.java)
val less = TokenNode(Operator.ComparisonLessOperator::class.java)
val lessOrEqual = TokenNode(Operator.ComparisonLessEqualOperator::class.java)
val equal = TokenNode(Operator.ComparisonEqualOperator::class.java)
val greaterOrEqual = TokenNode(Operator.ComparisonGreaterEqualOperator::class.java)
val greater = TokenNode(Operator.ComparisonGreaterOperator::class.java)
val plus = TokenNode(Operator.ArithmeticPlusOperator::class.java)
val minus = TokenNode(Operator.ArithmeticMinusOperator::class.java)
val mult = TokenNode(Operator.ArithmeticMultiplicationOperator::class.java)
val div = TokenNode(Operator.ArithmeticDivisionOperator::class.java)
val isOp = TokenNode(Operator.LogicIsOperator::class.java)
val arrow = TokenNode(Operator.ArrowOperator::class.java)
val range = TokenNode(Operator.RangeOperator::class.java)

/**
 * Separators
 */
val newLine = TokenNode(Separator.NewLineSeparator::class.java)
val semicolon = TokenNode(Separator.SemicolonSeparator::class.java)
val openBrace = TokenNode(Separator.OpeningCurlyBracketSeparator::class.java)
val closeBrace = TokenNode(Separator.ClosingCurlyBracketSeparator::class.java)
val openBracket = TokenNode(Separator.OpeningBracketSeparator::class.java)
val closeBracket = TokenNode(Separator.ClosingBracketSeparator::class.java)
val openParenthesis = TokenNode(Separator.OpeningParenthesisSeparator::class.java)
val closeParenthesis = TokenNode(Separator.ClosingParenthesisSeparator::class.java)
val comma = TokenNode(Separator.CommaSeparator::class.java)
val period = TokenNode(Separator.PeriodSeparator::class.java)

/**
 * Literals
 */
val integerLiteral = TokenNode(Literal.IntegerLiteral::class.java)
val realLiteral = TokenNode(Literal.RealLiteral::class.java)
val stringLiteral = TokenNode(Literal.StringLiteral::class.java)
val booleanLiteral = TokenNode(Keyword.BoolKeyword::class.java)
val emptyLiteral = TokenNode(Literal.EmptyLiteral::class.java)


/**
 * AST Declaration
 */
val literal = any("literal") {
    +integerLiteral
    +realLiteral
    +stringLiteral
    +booleanLiteral
    +emptyLiteral
//    +arrayLiteral    // this is added separately to break circular dependency cycle
//    +tupleLiteral    // this is added separately to break circular dependency cycle
//    +functionLiteral // this is added separately to break circular dependency cycle
}

val referencePath = any("referencePath") {
    +identifier
    +concat("path") {
        +identifier
        +period
//        +referencePath // this is added separately to break circular dependency cycle
    }
}

val reference = any("reference") {
//    +identifier
    +referencePath
    +concat("operatorGet") {
        +referencePath
        +openBracket
//        +expression // this is added separately to break circular dependency cycle
        +closeBracket
    }
}

val typeIndicator = any("typeIndicator") {
    +int
    +real
    +bool
    +string
    +empty
    +concat("arrayTypeIndicator") {
        +openBracket
        +closeBracket
    }
    +concat("tupleTypeIndicator") {
        +openBrace
        +closeBrace
    }
    +func
}

val primary = any("primary") {
    +literal
    +readInt
    +readReal
    +readString
    +concat("functionCall") {
        +reference
        +openParenthesis
        +maybe("maybe") {
            concat("concat") {
//                +expression // this is added separately to break circular dependency cycle
                +repeat("repeat") {
                    +comma
//                    +expression // this is added separately to break circular dependency cycle
                }
            }
        }
    }
    +concat("groupedExpression") {
        +openParenthesis
//        +expression // this is added separately to break circular dependency cycle
        +closeParenthesis
    }
}

val unary: AlternationNode = any("unary") {
    +reference
    +concat {
        +reference
        +isOp
        +typeIndicator
    }
    +concat {
        +maybe {
            any {
                +plus
                +minus
                +not
            }
        }
        +primary
    }
}

val term: ConcatenationNode = concat("term") {
    +unary
    +repeat {
        +concat {
            +any {
                +mult
                +div
            }
            +unary
        }
    }
}

val factor: ConcatenationNode = concat("factor") {
    +term
    +repeat {
        +concat {
            +any {
                +plus
                +minus
            }
            +term
        }
    }
}

val relation: ConcatenationNode = concat("relation") {
    +factor
    +maybe {
        concat {
            +any {
                +less
                +lessOrEqual
                +equal
                +greater
                +greaterOrEqual
            }
            +factor
        }
    }
}

val expression: ConcatenationNode = concat("expression") {
    +relation
    +repeat {
        +concat {
            +any {
                +or
                +and
                +xor
            }
            +relation
        }
    }
}

val varDefinition: ConcatenationNode = concat("varDefinition") {
    +identifier
    +maybe {
        concat {
            +assignmentOperator
            +expression
        }
    }
}

val declaration: ConcatenationNode = concat("declaration") {
    +varKeyword
    +varDefinition
    +repeat {
        concat {
            +comma
            +varDefinition
        }
    }
}
val assignmentStatement: ConcatenationNode = concat("assignmentStatement") {
    +reference
    +assignmentOperator
    +expression
}
val controlStructure: AlternationNode = any("controlStructure") {
//    +ifControlStructure   // this is added separately to break circular dependency cycle
//    +loopControlStructure // this is added separately to break circular dependency cycle
}
val printStatement: ConcatenationNode = concat("printStatement") {
    +print
    +expression
    +repeat {
        +comma
        +expression
    }
}
val returnStatement: ConcatenationNode = concat("returnStatement") {
    +returnKeyword
    +maybe {
        expression
    }
}
val statement: AlternationNode = any("statement") {
    +declaration
    +assignmentStatement
    +controlStructure
    +printStatement
    +returnStatement
}
val program: ConcatenationNode = concat("program") {
    +repeat {
        +concat {
            +statement
            +any {
                +semicolon
                +newLine
            }
        }
    }
}
val body: AlternationNode = any("body") {
    +program
}

val ifControlStructure: ConcatenationNode = concat("ifControlStructure") {
    +ifKeyword
    +expression
    +thenKeyword
    +body
    +maybe {
        concat {
            +elseKeyword
            +body
        }
    }
    +end
}


val forControlStructure: ConcatenationNode = concat("forControlStructure") {
    +forKeyword
    +any {
        +concat {
            +maybe {
                concat {
                    +identifier
                    +inKeyword
                }
            }
            +expression
            +range
            +expression
        }
        +concat {
            +identifier
            +inKeyword
            +expression
        }
    }
    +loopKeyword
    +body
    +end
}

val whileControlStructure: ConcatenationNode = concat("whileControlStructure") {
    +whileKeyword
    +expression
    +loopKeyword
    +body
    +end
}


val loopControlStructure: AlternationNode = any("loopControlStructure") {
    +forControlStructure
    +whileControlStructure
}


val funcBody: AlternationNode = any("funcBody") {
    +concat {
        +isOp
        +body
        +end
    }
    +concat {
        +arrow
        +expression
    }
}

//val referenceShit: AlternationNode = any {
//    +concat {
//        +reference
//        +openBracket
//        +expression
//        +closeBracket
//    }
//    +concat {
//        +reference
//        +period
//        +identifier
//    }
//}


val tupleElement: ConcatenationNode = concat("tupleElement") {
    +maybe {
        concat {
            +identifier
            +assignmentOperator
        }
    }
    +expression
}
val tupleLiteral: ConcatenationNode = concat("tupleLiteral") {
    +openBrace
    +maybe {
        concat {
            +tupleElement
            +repeat {
                +comma
                +tupleElement
            }
        }
    }
    +closeBrace
}

val arrayLiteral: ConcatenationNode = concat("arrayLiteral") {
    mapTo<FASTArrayLiteral>()

    +openBracket
    +maybe("maybeArrayElements") {
        concat("concatArrayElements") {
            +(expression onSuccess { fastNode: FASTArrayLiteral, nextFastNode ->
                fastNode.members.add(nextFastNode)
            })
            +repeat("followingArrayElements") {
                +comma
                +(expression onSuccess { fastNode: FASTArrayLiteral, nextFastNode ->
                    fastNode.members.add(nextFastNode)
                })
            }
        }
    }
    +closeBracket
}

val functionLiteral: ConcatenationNode = concat("functionLiteral") {
    +func
    +maybe {
        concat {
            +openParenthesis
            +identifier
            +repeat {
                +comma
                +identifier
            }
            +closeBracket
        }

    }
    +funcBody
}


val testRoot = arrayLiteral


fun main() {
    literal.apply { +arrayLiteral }
    literal.apply { +tupleLiteral }
    literal.apply { +functionLiteral }

    (referencePath["path"] as ConcatenationNode).apply { children.add(referencePath) }

    (reference["operatorGet"] as ConcatenationNode).apply { children.add(2, expression) }

//    reference.apply { +referenceShit }
//    (referencePath["path"]).apply { + }

    (primary["functionCall"]!!["maybe"]!!["concat"] as ConcatenationNode).apply { children.add(0, expression) }
    (primary["functionCall"]!!["maybe"]!!["concat"]!!["repeat"] as RepetitionNode).apply { children.add(expression) }
    (primary["groupedExpression"] as ConcatenationNode).apply { children.add(1, expression) }

    controlStructure.apply { +ifControlStructure }
    controlStructure.apply { +loopControlStructure }


    runTest()
}


fun runTest() {
    val path = "src/test/resources/maxim_test_1.pd"
    val lexer = LexicalAnalyzer.getInstance()
    val tokens = lexer.tokenize(File(path))
    parse(tokens)
}

fun parse(tokens: List<Token>) {
    println(tokens)
    val node = testRoot.match(tokens, null, 0)
    println("Node: $node")
    println("Node: ${node?.second}")
    println("Node type: ${node!!::class.qualifiedName}")
}