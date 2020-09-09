package com.compilingdogs.parser

import com.compilingdogs.parser.ast.*
import stages.LexicalAnalyzer
import tokens.*
import tokens.Identifier
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
val assignment = TokenNode(Operator.AssignmentOperator::class.java)
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
val literal: AlternationNode = any {
    +integerLiteral
    +realLiteral
    +stringLiteral
    +booleanLiteral
//    +arrayLiteral
//    +tupleLiteral
    +emptyLiteral
}

val ifControlStructure: ConcatenationNode = concat {
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

val tupleElement: ConcatenationNode = concat {
    +maybe {
        concat {
            +identifier
            +assignment
        }
    }
    +expression
}

val tupleLiteral: ConcatenationNode = concat {
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

val forControlStructure: ConcatenationNode = concat {
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

val whileControlStructure: ConcatenationNode = concat {
    +whileKeyword
    +expression
    +loopKeyword
    +body
    +end
}

val loopControlStructure: AlternationNode = any {
    +forControlStructure
    +whileControlStructure
}

val controlStructure: AlternationNode = any {
    +ifControlStructure
    +loopControlStructure
}

val printStatement: ConcatenationNode = concat {
    +print
    +expression
    +repeat {
        +comma
        +expression
    }
}

val returnStatement: ConcatenationNode = concat {
    +returnKeyword
    +maybe {
        expression
    }
}

val body: AlternationNode = any {
    +program
}

val funcBody: AlternationNode = any {
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

val functionLiteral: ConcatenationNode = concat {
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

val primary: AlternationNode = any {
    +literal
    +readInt
    +readReal
    +readString
    +functionLiteral
    +concat {
        +reference
        +openParenthesis
        +maybe {
            concat {
                +expression
                +repeat {
                    +comma
                    +expression
                }
            }
        }
        +concat {
            +openParenthesis
            +expression
            +closeParenthesis
        }
    }
}

val typeIndicator: AlternationNode = any {
    +int
    +real
    +bool
    +string
    +empty
    +concat {
        +openBracket
        +closeBracket
    }
    +concat {
        +openBrace
        +closeBrace
    }
    +func
}

val reference: AlternationNode = any {
    +identifier
}

val referenceShit: AlternationNode = any {
    +concat {
        +reference
        +openBracket
        +expression
        +closeBracket
    }
    +concat {
        +reference
        +period
        +identifier
    }
}

val unary: AlternationNode = any {
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

val term: ConcatenationNode = concat {
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

val factor: ConcatenationNode = concat {
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
val relation: ConcatenationNode = concat {
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

val expression: ConcatenationNode = concat {
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

val varDefinition: ConcatenationNode = concat {
    +identifier
    +maybe {
        concat {
            +assignment
            +expression
        }
    }
}

val declaration: ConcatenationNode = concat {
    +varKeyword
    +varDefinition
    +repeat {
        concat {
            +comma
            +varDefinition
        }
    }
}

val assignmentStatement: ConcatenationNode = concat {
    +reference
    +assignment
    +expression
}

val statement: AlternationNode = any {
    +declaration
    +assignmentStatement
    +controlStructure
    +printStatement
    +returnStatement
}

val program: ConcatenationNode = concat {
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

val arrayLiteral: ConcatenationNode = concat {
    mapTo<ArrayLiteral>()

    +openBracket
    +maybe {
        concat {
            +(expression onSuccess { fastNode: ArrayLiteral, nextFastNode ->
                fastNode.members.add(nextFastNode)
            })
            +repeat {
                +comma
                +(expression onSuccess { fastNode: ArrayLiteral, nextFastNode ->
                    fastNode.members.add(nextFastNode)
                })
            }
        }
    }
    +closeBracket
}

val testRoot = arrayLiteral


fun main() {
    literal.apply { +arrayLiteral }
    literal.apply { +tupleLiteral }
    reference.apply { +referenceShit }

    runTest()
}


fun runTest() {
    val path = "src/test/resources/maxim_test_1.pd"
    val lexer = LexicalAnalyzer.getInstance()
    val tokens = lexer.tokenize(File(path))
    parse(tokens)
}

fun parse(tokens: List<Token>) {
    print(tokens)
    val node = testRoot.match(tokens, null)
    print(node)
}