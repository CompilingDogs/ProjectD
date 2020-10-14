package com.compilingdogs.parser

import com.compilingdogs.interpretation.value.Runtime
import com.compilingdogs.parser.ast.*
import stages.LexicalAnalyzer
import tokens.*
import java.io.File

/**
 * Identifiers
 */
val identifier = TokenNode(Identifier::class.java, true)

/**
 * Keywords
 */
val varKeyword = TokenNode(Keyword.VarKeyword::class.java)
val int = TokenNode(Keyword.IntKeyword::class.java, true).apply { mapTo<FASTTypeIndicatorInt>() }
var real = TokenNode(Keyword.RealKeyword::class.java, true).apply { mapTo<FASTTypeIndicatorReal>() }
var bool = TokenNode(Keyword.BoolKeyword::class.java, true).apply { mapTo<FASTTypeIndicatorBool>() }
val string =
    TokenNode(Keyword.StringKeyword::class.java, true).apply { mapTo<FASTTypeIndicatorString>() }
var empty =
    TokenNode(Keyword.EmptyKeyword::class.java, true).apply { mapTo<FASTTypeIndicatorEmpty>() }
var func = TokenNode(Keyword.FuncKeyword::class.java, true).apply { mapTo<FASTTypeIndicatorFunc>() }
val readInt = TokenNode(Keyword.ReadIntKeyword::class.java, true).apply { mapTo<FASTReadIntCall>() }
val readReal =
    TokenNode(Keyword.ReadRealKeyword::class.java, true).apply { mapTo<FASTReadRealCall>() }
val readString =
    TokenNode(Keyword.ReadStringKeyword::class.java, true).apply { mapTo<FASTReadStringCall>() }
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
val notEqual = TokenNode(Operator.ComparisonNotEqualOperator::class.java)
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
val integerLiteral =
    TokenNode(Literal.IntegerLiteral::class.java, true).apply { mapTo<FASTIntegerLiteral>() }
val realLiteral =
    TokenNode(Literal.RealLiteral::class.java, true).apply { mapTo<FASTRealLiteral>() }
val stringLiteral =
    TokenNode(Literal.StringLiteral::class.java, true).apply { mapTo<FASTStringLiteral>() }
val trueKeyword =
    TokenNode(Keyword.TrueKeyword::class.java, true).apply { mapTo<FASTBooleanLiteral>() }
val falseKeyword =
    TokenNode(Keyword.FalseKeyword::class.java, true).apply { mapTo<FASTBooleanLiteral>() }


/**
 * AST Declaration
 */
val literal = any("literal") {
    +integerLiteral
    +realLiteral
    +stringLiteral
    +trueKeyword
    +falseKeyword
    +empty
//    +arrayLiteral    // this is added separately to break circular dependency cycle
//    +tupleLiteral    // this is added separately to break cir1cular dependency cycle
//    +functionLiteral // this is added separately to break circular dependency cycle
}

// TODO: decide what to do with this
val referencePath = any("referencePath") {
    +identifier
    +concat("path") {
        +identifier
        +period
//        +referencePath // this is added separately to break circular dependency cycle
    }
}

val simpleReference = concat("simpleReference") {
    mapTo<FASTReference>()
    +identifier

//    +referencePath
}

val arrayReference = concat("arrayReference") {
    mapTo<FASTArrayReference>()

    +referencePath
    +openBracket
//        +expression // this is added separately to break circular dependency cycle
    +closeBracket
}

// TODO: and this
val reference = any("reference") {
    +simpleReference
    +arrayReference
}

val typeIndicator = any("typeIndicator") {
    +int
    +real
    +bool
    +string
    +empty
    +func
    +concat("arrayTypeIndicator") {
        mapTo<FASTTypeIndicatorArray>()

        +openBracket
        +closeBracket
    }
    +concat("tupleTypeIndicator") {
        mapTo<FASTTypeIndicatorTuple>()

        +openBrace
        +closeBrace
    }
}

val primary = any("primary") {
    +concat("functionCall") {
        mapTo<FASTFunctionCall>()

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
        +closeParenthesis
    }
    +concat("groupedExpression") {
        // TODO: think if we need to make a fast node for that
        +openParenthesis
//        +expression // this is added separately to break circular dependency cycle
        +closeParenthesis
    }
    +reference
    +readInt
    +readReal
    +readString
    +literal
}


val unary = any("unary") {
    +concat("typeCheckOperator") {
        mapTo<FASTTypeCheckOperator>()

        +reference
        +isOp
        +typeIndicator
    }
    +reference
    +primary

    +concat("unaryPrimaryPlus") {
        mapTo<FASTPositiveOperator>()

        +plus
        +primary
    }
    +concat("unaryPrimaryMinus") {
        mapTo<FASTNegativeOperator>()

        +minus
        +primary
    }
    +concat("unaryPrimaryNot") {
        mapTo<FASTNotOperator>()

        +not
        +primary
    }
}


var termMult = concat("termMult") {
    mapTo<FASTMultiplyOperator>()

    +unary
    +repeat("termMultRepeat") {
        +concat("termMultConcat") {
            +mult
//            +term // this is added separately to break circular dependency cycle
        }
    }
}

val termDiv = concat("termDiv") {
    mapTo<FASTDivideOperator>()

    +unary
    +repeat("termDivRepeat") {
        +concat("termDivConcat") {
            +div
//            +term // this is added separately to break circular dependency cycle
        }
    }
}

val term = any("term") {
    +termMult
    +termDiv
}


val factorPlus = concat("factorPlus") {
    mapTo<FASTAddOperator>()

    +term
    +repeat("factorRepeat") {
        +concat("factorConcat") {
            +plus
//            +factor // this is added separately to break circular dependency cycle
        }
    }
}

val factorMinus = concat("factorMinus") {
    mapTo<FASTSubtractOperator>()

    +term
    +repeat("factorRepeat") {
        +concat("factorConcat") {
            +minus
//            +factor // this is added separately to break circular dependency cycle
        }
    }
}

val factor = any("factor") {
    +factorPlus
    +factorMinus
}

val lessRelation = concat("lessRelation") {
    mapTo<FASTLessOperator>()

    +factor
    +maybe("relationMaybe") {
        concat("relationConcat") {
            +less
//            +relation // this is added separately to break circular dependency cycle
        }
    }
}

val lessOrEqualRelation = concat("lessOrEqualRelation") {
    mapTo<FASTLessEqualOperator>()

    +factor
    +maybe("relationMaybe") {
        concat("relationConcat") {
            +lessOrEqual
//            +relation // this is added separately to break circular dependency cycle
        }
    }
}

val equalRelation = concat("equalRelation") {
    mapTo<FASTEqualOperator>()

    +factor
    +maybe("relationMaybe") {
        concat("relationConcat") {
            +equal
//            +relation // this is added separately to break circular dependency cycle
        }
    }
}

val notEqualRelation = concat("notEqualRelation") {
    mapTo<FASTNotEqualOperator>()

    +factor
    +maybe("relationMaybe") {
        concat("relationConcat") {
            +notEqual
//            +relation // this is added separately to break circular dependency cycle
        }
    }
}

val greaterRelation = concat("greaterRelation") {
    mapTo<FASTGreaterOperator>()

    +factor
    +maybe("relationMaybe") {
        concat("relationConcat") {
            +greater
//            +relation // this is added separately to break circular dependency cycle
        }
    }
}

val greaterOrEqualRelation = concat("greaterOrEqualRelation") {
    mapTo<FASTGreaterEqualOperator>()

    +factor
    +maybe("relationMaybe") {
        concat("relationConcat") {
            +greaterOrEqual
//            +relation // this is added separately to break circular dependency cycle
        }
    }
}

val relation = any("relation") {
    +lessRelation
    +lessOrEqualRelation
    +equalRelation
    +notEqualRelation
    +greaterRelation
    +greaterOrEqualRelation
}

val expressionOr = concat("expressionOr") {
    mapTo<FASTOrOperator>()

    +relation
    +repeat("expressionRepeat") {
        +concat("expressionRepeatConcat") {
            +or
//            +expression // this is added separately to break circular dependency cycle
        }
    }
}

val expressionAnd = concat("expressionAnd") {
    mapTo<FASTAndOperator>()

    +relation
    +repeat("expressionRepeat") {
        +concat("expressionRepeatConcat") {
            +and
//            +expression // this is added separately to break circular dependency cycle
        }
    }
}

val expressionXor = concat("expressionXor") {
    mapTo<FASTXorOperator>()

    +relation
    +repeat("expressionRepeat") {
        +concat("expressionRepeatConcat") {
            +xor
//            +expression // this is added separately to break circular dependency cycle
        }
    }
}

val expression = any("expression") {
    +expressionOr
    +expressionAnd
    +expressionXor
}

val varDefinition = concat("varDefinition") {
    mapTo<FASTVarDefinition>()

    +identifier
    +maybe("maybeVarValue") {
        concat("concatVarValue") {
            +assignmentOperator
            +expression
        }
    }
}

val declaration = concat("declaration") {
    mapTo<FASTDeclarationStatement>()

    +varKeyword
    +varDefinition
    // TODO: uncomment and fix
//    +repeat {
//        concat {
//            +comma
//            +varDefinition
//        }
//    }
}
val assignmentStatement = concat("assignmentStatement") {
    mapTo<FASTAssignmentStatement>()

    +reference
    +assignmentOperator
    +expression
}
val controlStructure = any("controlStructure") {
//    +ifControlStructure   // this is added separately to break circular dependency cycle
//    +loopControlStructure // this is added separately to break circular dependency cycle
}
val printStatement = concat("printStatement") {
    mapTo<FASTPrintStatement>()

    +print
    +expression
    +repeat("printStatementRepeat") {
        +comma
        +expression
    }
}
val returnStatement = concat("returnStatement") {
    mapTo<FASTReturnStatement>()

    +returnKeyword
    +maybe("returnStatementMaybe") {
        expression
    }
}
val statement = any("statement") {
    +declaration
    +assignmentStatement
    +controlStructure
    +printStatement
    +returnStatement
}


val body = repeat("body") {
    mapTo<FASTBody>()

    +concat("statement+separator") {
        +repeat("repeatSeparator") {
            +newLine
        }

        +statement

        +any("anySeparator") {
            +newLine
            +semicolon
        }
        +repeat("repeatSeparator2") {
            +newLine
        }
    }
}

val program = repeat("program") {
    mapTo<FASTProgram>()

    +concat("statement+separator") {
        +repeat("repeatSeparator") {
            +newLine
        }

        +statement

        +any("anySeparator") {
            +newLine
            +semicolon
        }
        +repeat("repeatSeparator2") {
            +newLine
        }
    }
}


val ifControlStructure = concat("ifControlStructure") {
    mapTo<FASTIfStructure>()

    +ifKeyword
    +expression
    +thenKeyword
    +body
    +maybe("ifControlStructureMaybe") {
        concat("ifControlStructureConcat") {
            +elseKeyword
            +body
        }
    }
    +end
}


val forControlStructure = concat("forControlStructure") {
    mapTo<FASTForLoop>()

    +forKeyword
    +any("forControlStructureAny") {
        +concat("forControlStructureConcat") {
            +maybe("forControlStructureMaybe") {
                concat("forControlStructureConcat2") {
                    +identifier
                    +inKeyword
                }
            }
            +expression
            +range
            +expression
        }
        +concat("forControlStructureConcat3") {
            +identifier
            +inKeyword
            +expression
        }
    }
    +loopKeyword
    +body
    +end
}

val whileControlStructure = concat("whileControlStructure") {
    mapTo<FASTWhileLoop>()

    +whileKeyword
    +expression
    +loopKeyword
    +body
    +end
}


val loopControlStructure = any("loopControlStructure") {
    +forControlStructure
    +whileControlStructure
}


val funcBody = any("funcBody") {
    // TODO: complete this

    +concat("funcBodyConcat") {
        +isOp
        +body
        +end
    }
    +concat("funcBodyConcat2") {
        mapTo<FASTBody>()

        +arrow
        // that was expression... somewhen in the past :D
        +statement
    }
}

val tupleElement = concat("tupleElement") {
    mapTo<FASTTupleElement>()

    +maybe("tupleElementMaybe") {
        concat("tupleElementConcat") {
            +identifier
            +assignmentOperator
        }
    }
    +expression
}
val tupleLiteral = concat("tupleLiteral") {
    mapTo<FASTTupleLiteral>()

    +openBrace
    +maybe("tupleLiteralMaybe") {
        concat("tupleLiteralConcat") {
            +tupleElement
            +repeat("tupleLiteralRepeat") {
                +comma
                +tupleElement
            }
        }
    }
    +closeBrace
}

val arrayLiteral = concat("arrayLiteral") {
    mapTo<FASTArrayLiteral>()

    +openBracket
    +maybe("maybeArrayElements") {
        concat("concatArrayElements") {
            +expression
            +repeat("followingArrayElements") {
                +comma
                +expression
            }
        }
    }
    +closeBracket
}

val functionLiteral = concat("functionLiteral") {
    mapTo<FASTFunctionLiteral>()

    +func
    +maybe("functionLiteralMaybe") {
        concat("functionLiteralConcat") {
            +openParenthesis
            +identifier
            +repeat("functionLiteralRepeat") {
                +comma
                +identifier
            }
            +closeParenthesis
        }

    }
    +funcBody
}

var initialized = false
fun initialize() {
    literal.apply { +arrayLiteral }
    literal.apply { +tupleLiteral }
    literal.apply { +functionLiteral }

    (referencePath["path"] as ConcatenationNode).children.add(referencePath)
//    (referencePath["path"] as ConcatenationNode).apply { children.add(reference) }

    arrayReference.children.add(2, expression)

    (primary["functionCall"]!!["maybe"]!!["concat"] as ConcatenationNode).children.add(
        0,
        expression
    )
    (primary["functionCall"]!!["maybe"]!!["concat"]!!["repeat"] as RepetitionNode).children.add(
        expression
    )
    (primary["groupedExpression"] as ConcatenationNode).children.add(1, expression)

    (termMult["termMultRepeat"]!!["termMultConcat"] as ConcatenationNode).children.add(term)
    (termDiv["termDivRepeat"]!!["termDivConcat"] as ConcatenationNode).children.add(term)

    (factorPlus["factorRepeat"]!!["factorConcat"] as ConcatenationNode).children.add(term)
    (factorMinus["factorRepeat"]!!["factorConcat"] as ConcatenationNode).children.add(term)

    (expressionOr["expressionRepeat"]!!["expressionRepeatConcat"]!! as ConcatenationNode).children.add(
        expression
    )
    (expressionAnd["expressionRepeat"]!!["expressionRepeatConcat"]!! as ConcatenationNode).children.add(
        expression
    )
    (expressionXor["expressionRepeat"]!!["expressionRepeatConcat"]!! as ConcatenationNode).children.add(
        expression
    )

    (lessRelation["relationMaybe"]!!["relationConcat"]!! as ConcatenationNode).children.add(relation)
    (lessOrEqualRelation["relationMaybe"]!!["relationConcat"]!! as ConcatenationNode).children.add(relation)
    (notEqualRelation["relationMaybe"]!!["relationConcat"]!! as ConcatenationNode).children.add(relation)
    (equalRelation["relationMaybe"]!!["relationConcat"]!! as ConcatenationNode).children.add(relation)
    (greaterOrEqualRelation["relationMaybe"]!!["relationConcat"]!! as ConcatenationNode).children.add(relation)
    (greaterRelation["relationMaybe"]!!["relationConcat"]!! as ConcatenationNode).children.add(relation)

    controlStructure.apply { +ifControlStructure }
    controlStructure.apply { +loopControlStructure }
}

fun main() {
    runTest()
}


fun runTest() {
    val path = "src/test/resources/maxim_test_1.pd"
    val lexer = LexicalAnalyzer.getInstance()
    val tokens = lexer.tokenize(File(path))
    val runtime = Runtime()

    var tree: FASTNode
    val time = measureTimeMillis {
        tree = parse(tokens)
        tree.evaluate(runtime)

//        println()
//        runtime.symbolTable.entries.forEach {
//            println("${it.key} : ${it.value}")
//        }
    }
    println("Elapsed time: ${time.toFloat() / 1000} seconds")
}

fun parse(tokens: List<Token>): FASTNode {
    if (!initialized) {
        initialize()
        initialized = true
    }

    val results = program.match(tokens, 0)
    if (results.error != null)
        error(results.error)

//    println("Node: ${results.result}")
//    println()
    return results.result.first()
}
