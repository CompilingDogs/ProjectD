package tokens;

import exception.LexicalAnalysisExceprion;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;

import static java.lang.String.format;

@Slf4j
@ToString
@NoArgsConstructor
public class Keyword extends Token {

    public static final String VAR_KEYWORD_TOKEN = "var";
    public static final String TRUE_KEYWORD_TOKEN = "true";
    public static final String FALSE_KEYWORD_TOKEN = "false";
    public static final String IS_KEYWORD_TOKEN = "is";
    public static final String END_KEYWORD_TOKEN = "end";
    public static final String IF_KEYWORD_TOKEN = "if";
    public static final String THEN_KEYWORD_TOKEN = "then";
    public static final String ELSE_KEYWORD_TOKEN = "else";
    public static final String FOR_KEYWORD_TOKEN = "for";
    public static final String WHILE_KEYWORD_TOKEN = "while";
    public static final String IN_KEYWORD_TOKEN = "in";
    public static final String LOOP_KEYWORD_TOKEN = "loop";
    public static final String PRINT_KEYWORD_TOKEN = "print";
    public static final String RETURN_KEYWORD_TOKEN = "return";
    public static final String INT_KEYWORD_TOKEN = "int";
    public static final String REAL_KEYWORD_TOKEN = "real";
    public static final String BOOL_KEYWORD_TOKEN = "bool";
    public static final String STRING_KEYWORD_TOKEN = "string";
    public static final String EMPTY_KEYWORD_TOKEN = "empty";
    public static final String FUNC_KEYWORD_TOKEN = "func";
    public static final String ARRAY_KEYWORD_TOKEN = "array";
    public static final String TUPLE_KEYWORD_TOKEN = "tuple";

    private static final HashSet<String> keywordTokens = new HashSet<>(
            Arrays.asList(
                    VAR_KEYWORD_TOKEN, TRUE_KEYWORD_TOKEN, FALSE_KEYWORD_TOKEN, IS_KEYWORD_TOKEN, END_KEYWORD_TOKEN,
                    IF_KEYWORD_TOKEN, THEN_KEYWORD_TOKEN, ELSE_KEYWORD_TOKEN, FOR_KEYWORD_TOKEN, WHILE_KEYWORD_TOKEN,
                    IN_KEYWORD_TOKEN, LOOP_KEYWORD_TOKEN, PRINT_KEYWORD_TOKEN, RETURN_KEYWORD_TOKEN, INT_KEYWORD_TOKEN,
                    REAL_KEYWORD_TOKEN, BOOL_KEYWORD_TOKEN, STRING_KEYWORD_TOKEN, EMPTY_KEYWORD_TOKEN, FUNC_KEYWORD_TOKEN,
                    ARRAY_KEYWORD_TOKEN, TUPLE_KEYWORD_TOKEN
            )
    );

    public Keyword(String token, Integer line, Integer column) {
        super(token, line, column);
    }

    public static Boolean isKeyword(String token) {
        return keywordTokens.contains(token);
    }

    /**
     * Converts string containing token to the Keyword class.
     *
     * @param token  - String containing keyword
     * @param line   - Line number of the token occurrence
     * @param column - Column number of the token's first character occurrence
     * @return Instance of one of Keyword's Successors.
     */
    public static Keyword getKeyword(String token, Integer line, Integer column) throws LexicalAnalysisExceprion {
        return switch (token) {
            case VAR_KEYWORD_TOKEN -> new VarKeyword(token, line, column);
            case TRUE_KEYWORD_TOKEN -> new TrueKeyword(token, line, column);
            case FALSE_KEYWORD_TOKEN -> new FalseKeyword(token, line, column);
            case IS_KEYWORD_TOKEN -> new IsKeyword(token, line, column);
            case END_KEYWORD_TOKEN -> new EndKeyword(token, line, column);
            case IF_KEYWORD_TOKEN -> new IfKeyword(token, line, column);
            case THEN_KEYWORD_TOKEN -> new ThenKeyword(token, line, column);
            case ELSE_KEYWORD_TOKEN -> new ElseKeyword(token, line, column);
            case FOR_KEYWORD_TOKEN -> new ForKeyword(token, line, column);
            case WHILE_KEYWORD_TOKEN -> new WhileKeyword(token, line, column);
            case IN_KEYWORD_TOKEN -> new InKeyword(token, line, column);
            case LOOP_KEYWORD_TOKEN -> new LoopKeyword(token, line, column);
            case PRINT_KEYWORD_TOKEN -> new PrintKeyword(token, line, column);
            case RETURN_KEYWORD_TOKEN -> new ReturnKeyword(token, line, column);
            case INT_KEYWORD_TOKEN -> new IntKeyword(token, line, column);
            case REAL_KEYWORD_TOKEN -> new RealKeyword(token, line, column);
            case BOOL_KEYWORD_TOKEN -> new BoolKeyword(token, line, column);
            case STRING_KEYWORD_TOKEN -> new StringKeyword(token, line, column);
            case EMPTY_KEYWORD_TOKEN -> new EmptyKeyword(token, line, column);
            case FUNC_KEYWORD_TOKEN -> new FuncKeyword(token, line, column);
            case ARRAY_KEYWORD_TOKEN -> new ArrayKeyword(token, line, column);
            case TUPLE_KEYWORD_TOKEN -> new TupleKeyword(token, line, column);
            default -> {
                var message = format(
                        "Error in lexical analysis at line - %d, column - %d. Unacceptable Keyword: %s.",
                        line, column, token
                );
                log.warn(message);
                throw new LexicalAnalysisExceprion(message);
            }
        };
    }

    @ToString
    public static class VarKeyword extends Keyword {
        public VarKeyword(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    @ToString
    public static class TrueKeyword extends Keyword {
        public TrueKeyword(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    @ToString
    public static class FalseKeyword extends Keyword {
        public FalseKeyword(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    @ToString
    public static class IsKeyword extends Keyword {
        public IsKeyword(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    @ToString
    public static class EndKeyword extends Keyword {
        public EndKeyword(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    @ToString
    public static class IfKeyword extends Keyword {
        public IfKeyword(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    @ToString
    public static class ThenKeyword extends Keyword {
        public ThenKeyword(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    @ToString
    public static class ElseKeyword extends Keyword {
        public ElseKeyword(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    @ToString
    public static class ForKeyword extends Keyword {
        public ForKeyword(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    @ToString
    public static class WhileKeyword extends Keyword {
        public WhileKeyword(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    @ToString
    public static class InKeyword extends Keyword {
        public InKeyword(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    @ToString
    public static class LoopKeyword extends Keyword {
        public LoopKeyword(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    @ToString
    public static class PrintKeyword extends Keyword {
        public PrintKeyword(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    @ToString
    public static class ReturnKeyword extends Keyword {
        public ReturnKeyword(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    @ToString
    public static class IntKeyword extends Keyword {
        public IntKeyword(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    @ToString
    public static class RealKeyword extends Keyword {
        public RealKeyword(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    @ToString
    public static class BoolKeyword extends Keyword {
        public BoolKeyword(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    @ToString
    public static class StringKeyword extends Keyword {
        public StringKeyword(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    @ToString
    public static class EmptyKeyword extends Keyword {
        public EmptyKeyword(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    @ToString
    public static class FuncKeyword extends Keyword {
        public FuncKeyword(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    @ToString
    public static class ArrayKeyword extends Keyword {
        public ArrayKeyword(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    @ToString
    public static class TupleKeyword extends Keyword {
        public TupleKeyword(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

}
