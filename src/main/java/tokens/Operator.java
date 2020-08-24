package tokens;

import exception.LexicalAnalysisException;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;

import static java.lang.String.format;

@Slf4j
public class Operator extends Token {
    public static final String ASSIGNMENT_OPERATOR_TOKEN = ":=";
    public static final String LOGIC_NOT_OPERATOR_TOKEN = "not";
    public static final String LOGIC_OR_OPERATOR_TOKEN = "or";
    public static final String LOGIC_AND_OPERATOR_TOKEN = "and";
    public static final String LOGIC_XOR_OPERATOR_TOKEN = "xor";
    public static final String LOGIC_IS_OPERATOR_TOKEN = "is";
    public static final String COMPARISON_LESS_OPERATOR_TOKEN = "<";
    public static final String COMPARISON_LESS_EQUAL_OPERATOR_TOKEN = "<=";
    public static final String COMPARISON_GREATER_OPERATOR_TOKEN = ">";
    public static final String COMPARISON_GREATER_EQUAL_OPERATOR_TOKEN = ">=";
    public static final String COMPARISON_EQUAL_OPERATOR_TOKEN = "=";
    public static final String COMPARISON_NOT_EQUAL_OPERATOR_TOKEN = "/=";
    public static final String ARITHMETIC_PLUS_OPERATOR_TOKEN = "+";
    public static final String ARITHMETIC_MINUS_OPERATOR_TOKEN = "-";
    public static final String ARITHMETIC_MULTIPLICATION_OPERATOR_TOKEN = "*";
    public static final String ARITHMETIC_DIVISION_OPERATOR_TOKEN = "/";
    public static final String ARROW_OPERATOR_TOKEN = "=>";
    public static final String RANGE_OPERATOR_TOKEN = "..";

    private static final HashSet<String> operatorTokens = new HashSet<>(
            Arrays.asList(
                    ASSIGNMENT_OPERATOR_TOKEN, LOGIC_NOT_OPERATOR_TOKEN, LOGIC_OR_OPERATOR_TOKEN,
                    LOGIC_AND_OPERATOR_TOKEN, LOGIC_XOR_OPERATOR_TOKEN, LOGIC_IS_OPERATOR_TOKEN,
                    COMPARISON_LESS_OPERATOR_TOKEN, COMPARISON_LESS_EQUAL_OPERATOR_TOKEN,
                    COMPARISON_GREATER_OPERATOR_TOKEN, COMPARISON_GREATER_EQUAL_OPERATOR_TOKEN,
                    COMPARISON_EQUAL_OPERATOR_TOKEN, COMPARISON_NOT_EQUAL_OPERATOR_TOKEN,
                    ARITHMETIC_PLUS_OPERATOR_TOKEN, ARITHMETIC_MINUS_OPERATOR_TOKEN,
                    ARITHMETIC_MULTIPLICATION_OPERATOR_TOKEN, ARITHMETIC_DIVISION_OPERATOR_TOKEN,
                    ARROW_OPERATOR_TOKEN, RANGE_OPERATOR_TOKEN
            )
    );

    public Operator(String token, Integer line, Integer column) {
        super(token, line, column);
    }

    public static Boolean isOperator(String token) {
        return operatorTokens.contains(token);
    }

    /**
     * Converts string containing token to the Operator class.
     *
     * @param token  - String containing operator
     * @param line   - Line number of the token occurrence
     * @param column - Column number of the token's first character occurrence
     * @return Instance of one of Operator's Successors.
     */
    public static Operator getOperator(
            String token,
            Integer line,
            Integer column
    ) throws LexicalAnalysisException {
        log.debug("Parsing token as operator {}: ({}:{}).", token, line, column);
        return switch (token) {
            case ASSIGNMENT_OPERATOR_TOKEN -> new AssignmentOperator(token, line, column);
            case LOGIC_NOT_OPERATOR_TOKEN -> new LogicNotOperator(token, line, column);
            case LOGIC_OR_OPERATOR_TOKEN -> new LogicOrOperator(token, line, column);
            case LOGIC_AND_OPERATOR_TOKEN -> new LogicAndOperator(token, line, column);
            case LOGIC_XOR_OPERATOR_TOKEN -> new LogicXorOperator(token, line, column);
            case LOGIC_IS_OPERATOR_TOKEN -> new LogicIsOperator(token, line, column);
            case COMPARISON_LESS_OPERATOR_TOKEN -> new ComparisonLessOperator(token, line, column);
            case COMPARISON_LESS_EQUAL_OPERATOR_TOKEN -> new ComparisonLessEqualOperator(
                    token,
                    line,
                    column
            );
            case COMPARISON_GREATER_OPERATOR_TOKEN -> new ComparisonGreaterOperator(
                    token,
                    line,
                    column
            );
            case COMPARISON_GREATER_EQUAL_OPERATOR_TOKEN -> new ComparisonGreaterEqualOperator(
                    token,
                    line,
                    column
            );
            case COMPARISON_EQUAL_OPERATOR_TOKEN -> new ComparisonEqualOperator(
                    token,
                    line,
                    column
            );
            case COMPARISON_NOT_EQUAL_OPERATOR_TOKEN -> new ComparisonNotEqualOperator(
                    token,
                    line,
                    column
            );
            case ARITHMETIC_PLUS_OPERATOR_TOKEN -> new ArithmeticPlusOperator(token, line, column);
            case ARITHMETIC_MINUS_OPERATOR_TOKEN -> new ArithmeticMinusOperator(
                    token,
                    line,
                    column
            );
            case ARITHMETIC_MULTIPLICATION_OPERATOR_TOKEN -> new ArithmeticMultiplicationOperator(
                    token,
                    line,
                    column
            );
            case ARITHMETIC_DIVISION_OPERATOR_TOKEN -> new ArithmeticDivisionOperator(
                    token,
                    line,
                    column
            );
            case ARROW_OPERATOR_TOKEN -> new ArrowOperator(token, line, column);
            case RANGE_OPERATOR_TOKEN -> new RangeOperator(token, line, column);
            default -> {
                var message = format(
                        "Error in lexical analysis at line - %d, column - %d. Unacceptable Operator: %s.",
                        line,
                        column,
                        token
                );
                throw new LexicalAnalysisException(message);
            }
        };
    }

    public static class AssignmentOperator extends Operator {
        public AssignmentOperator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class LogicNotOperator extends Operator {
        public LogicNotOperator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class LogicOrOperator extends Operator {
        public LogicOrOperator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class LogicAndOperator extends Operator {
        public LogicAndOperator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class LogicXorOperator extends Operator {
        public LogicXorOperator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class LogicIsOperator extends Operator {
        public LogicIsOperator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class ComparisonLessOperator extends Operator {
        public ComparisonLessOperator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class ComparisonLessEqualOperator extends Operator {
        public ComparisonLessEqualOperator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class ComparisonGreaterOperator extends Operator {
        public ComparisonGreaterOperator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class ComparisonGreaterEqualOperator extends Operator {
        public ComparisonGreaterEqualOperator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class ComparisonEqualOperator extends Operator {
        public ComparisonEqualOperator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class ComparisonNotEqualOperator extends Operator {
        public ComparisonNotEqualOperator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class ArithmeticPlusOperator extends Operator {
        public ArithmeticPlusOperator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class ArithmeticMinusOperator extends Operator {
        public ArithmeticMinusOperator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class ArithmeticMultiplicationOperator extends Operator {
        public ArithmeticMultiplicationOperator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class ArithmeticDivisionOperator extends Operator {
        public ArithmeticDivisionOperator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class ArrowOperator extends Operator {
        public ArrowOperator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class RangeOperator extends Operator {
        public RangeOperator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

}