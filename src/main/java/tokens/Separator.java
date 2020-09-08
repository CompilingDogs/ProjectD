package tokens;

import exception.LexicalAnalysisException;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.HashSet;

import static java.lang.String.format;

public class Separator extends Token {

    public static final String OPENING_PARENTHESIS_SEPARATOR_TOKEN = "(";
    public static final String CLOSING_PARENTHESIS_SEPARATOR_TOKEN = ")";
    public static final String OPENING_BRACKET_SEPARATOR_TOKEN = "[";
    public static final String CLOSING_BRACKET_SEPARATOR_TOKEN = "]";
    public static final String OPENING_CURLY_BRACKET_SEPARATOR_TOKEN = "{";
    public static final String CLOSING_CURLY_BRACKET_SEPARATOR_TOKEN = "}";
    public static final String SEMICOLON_SEPARATOR_TOKEN = ";";
    public static final String COMMA_SEPARATOR_TOKEN = ",";
    public static final String PERIOD_SEPARATOR_TOKEN = ".";
    public static final String WHITE_SPACE_SEPARATOR_TOKEN = " ";
    public static final String TAB_SPACE_SEPARATOR_TOKEN = "\t";
    public static final String NEW_LINE_SEPARATOR_TOKEN = "\n";
    public static final String WINDOWS_NEW_LINE_SEPARATOR_TOKEN = "\r";

    private static final HashSet<String> separatorTokens = new HashSet<>(
            Arrays.asList(
                    OPENING_PARENTHESIS_SEPARATOR_TOKEN,
                    CLOSING_PARENTHESIS_SEPARATOR_TOKEN,
                    OPENING_BRACKET_SEPARATOR_TOKEN,
                    CLOSING_BRACKET_SEPARATOR_TOKEN,
                    OPENING_CURLY_BRACKET_SEPARATOR_TOKEN,
                    CLOSING_CURLY_BRACKET_SEPARATOR_TOKEN,
                    SEMICOLON_SEPARATOR_TOKEN,
                    COMMA_SEPARATOR_TOKEN,
                    PERIOD_SEPARATOR_TOKEN,
                    WHITE_SPACE_SEPARATOR_TOKEN,
                    TAB_SPACE_SEPARATOR_TOKEN,
                    NEW_LINE_SEPARATOR_TOKEN,
                    WINDOWS_NEW_LINE_SEPARATOR_TOKEN
            )
    );

    private static final HashSet<String> persistentSeparatorTokens = new HashSet<>(
            Arrays.asList(
                    OPENING_PARENTHESIS_SEPARATOR_TOKEN,
                    CLOSING_PARENTHESIS_SEPARATOR_TOKEN,
                    OPENING_BRACKET_SEPARATOR_TOKEN,
                    CLOSING_BRACKET_SEPARATOR_TOKEN,
                    OPENING_CURLY_BRACKET_SEPARATOR_TOKEN,
                    CLOSING_CURLY_BRACKET_SEPARATOR_TOKEN,
                    SEMICOLON_SEPARATOR_TOKEN,
                    COMMA_SEPARATOR_TOKEN,
                    PERIOD_SEPARATOR_TOKEN,
                    NEW_LINE_SEPARATOR_TOKEN
            )
    );
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Separator.class);

    public Separator(String token, Integer line, Integer column) {
        super(token, line, column);
    }

    public static Boolean isSeparator(String token) {
        return separatorTokens.contains(token);
    }

    public static Boolean isPersistentSeparator(String token) {
        return persistentSeparatorTokens.contains(token);
    }

    /**
     * Converts string containing token to the Separator class.
     *
     * @param token  - String containing separator
     * @param line   - Line number of the token occurrence
     * @param column - Column number of the token's first character occurrence
     * @return Instance of one of Separator's Successors.
     */
    public static Separator getSeparator(
            String token,
            Integer line,
            Integer column
    ) throws LexicalAnalysisException {
        log.debug("Parsing token as separator {}: ({}:{}).", token, line, column);
        return switch (token) {
            case OPENING_PARENTHESIS_SEPARATOR_TOKEN -> new OpeningParenthesisSeparator(
                    token,
                    line,
                    column
            );
            case CLOSING_PARENTHESIS_SEPARATOR_TOKEN -> new ClosingParenthesisSeparator(
                    token,
                    line,
                    column
            );
            case OPENING_BRACKET_SEPARATOR_TOKEN -> new OpeningBracketSeparator(
                    token,
                    line,
                    column
            );
            case CLOSING_BRACKET_SEPARATOR_TOKEN -> new ClosingBracketSeparator(
                    token,
                    line,
                    column
            );
            case OPENING_CURLY_BRACKET_SEPARATOR_TOKEN -> new OpeningCurlyBracketSeparator(
                    token,
                    line,
                    column
            );
            case CLOSING_CURLY_BRACKET_SEPARATOR_TOKEN -> new ClosingCurlyBracketSeparator(
                    token,
                    line,
                    column
            );
            case SEMICOLON_SEPARATOR_TOKEN -> new SemicolonSeparator(token, line, column);
            case COMMA_SEPARATOR_TOKEN -> new CommaSeparator(token, line, column);
            case PERIOD_SEPARATOR_TOKEN -> new PeriodSeparator(token, line, column);
            case WHITE_SPACE_SEPARATOR_TOKEN -> new WhiteSpaceSeparator(token, line, column);
            case TAB_SPACE_SEPARATOR_TOKEN -> new TabSpaceSeparator(token, line, column);
            case NEW_LINE_SEPARATOR_TOKEN -> new NewLineSeparator(token, line, column);
            case WINDOWS_NEW_LINE_SEPARATOR_TOKEN -> new WindowsNewLineSeparator(
                    token,
                    line,
                    column
            );
            default -> {
                var message = format(
                        "Error in lexical analysis at line - %d, column - %d. Unacceptable Separator: \"%s\".",
                        line,
                        column,
                        token
                );
                throw new LexicalAnalysisException(message);
            }
        };
    }

    public static class OpeningParenthesisSeparator extends Separator {
        public OpeningParenthesisSeparator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class ClosingParenthesisSeparator extends Separator {
        public ClosingParenthesisSeparator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class OpeningBracketSeparator extends Separator {
        public OpeningBracketSeparator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class ClosingBracketSeparator extends Separator {
        public ClosingBracketSeparator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class OpeningCurlyBracketSeparator extends Separator {
        public OpeningCurlyBracketSeparator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class ClosingCurlyBracketSeparator extends Separator {
        public ClosingCurlyBracketSeparator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class SemicolonSeparator extends Separator {
        public SemicolonSeparator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class CommaSeparator extends Separator {
        public CommaSeparator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class PeriodSeparator extends Separator {
        public PeriodSeparator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class WhiteSpaceSeparator extends Separator {
        public WhiteSpaceSeparator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class TabSpaceSeparator extends Separator {
        public TabSpaceSeparator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class NewLineSeparator extends Separator {
        public NewLineSeparator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

    public static class WindowsNewLineSeparator extends Separator {
        public WindowsNewLineSeparator(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

}
