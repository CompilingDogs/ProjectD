package tokens;

import exception.LexicalAnalysisExceprion;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import static java.lang.String.format;

@Slf4j
@ToString
public class Literal extends Token {

    public static final String EMPTY_LITERAL_TOKEN = "empty";

    public Literal(String token, Integer line, Integer column) {
        super(token, line, column);
    }

    public static Boolean isEmptyLiteral(String token) {
        return token.equals(EMPTY_LITERAL_TOKEN);
    }

    public static Boolean isStringLiteral(String token) {
        var beginningChar = token.charAt(0);
        var endingChar = token.charAt(token.length() - 1);

        return (beginningChar == '\"' && endingChar == '\"') || (beginningChar == '\'' && endingChar == '\'');
    }

    public static Boolean isIntegerLiteral(String token) {
        return Integer.valueOf(Integer.parseInt(token)).toString().equals(token.trim());
    }

    public static Boolean isRealLiteral(String token) {
        return Double.valueOf(Double.parseDouble(token)).toString().equals(token.trim());
    }

    /**
     * Converts string containing token to the Literal class.
     *
     * @param token  - String containing literal
     * @param line   - Line number of the token occurrence
     * @param column - Column number of the token's first character occurrence
     * @return Instance of one of Literal's Successors.
     */
    public static Literal getLiteral(String token, Integer line, Integer column) throws LexicalAnalysisExceprion {
        if (isEmptyLiteral(token)) {
            return new EmptyLiteral(token, line, column);
        } else if (isStringLiteral(token)) {
            return new StringLiteral(token, line, column);
        } else if (isIntegerLiteral(token)) {
            return new IntegerLiteral(token, line, column);
        } else if (isRealLiteral(token)) {
            return new RealLiteral(token, line, column);
        } else {
            var message = format(
                    "Error in lexical analysis at line - %d, column - %d. Unacceptable literal: %s.",
                    line, column, token
            );
            log.warn(message);
            throw new LexicalAnalysisExceprion(message);
        }
    }

    @Getter
    @ToString
    public static class IntegerLiteral extends Literal {
        private final Integer value;

        public IntegerLiteral(String token, Integer line, Integer column) {
            super(token, line, column);
            this.value = Integer.parseInt(token);
        }
    }

    @Getter
    @ToString
    public static class RealLiteral extends Literal {
        private final Double value;

        public RealLiteral(String token, Integer line, Integer column) {
            super(token, line, column);
            this.value = Double.parseDouble(token);
        }
    }

    @Getter
    @ToString
    public static class StringLiteral extends Literal {
        private final String value;

        public StringLiteral(String token, Integer line, Integer column) {
            super(token, line, column);
            this.value = parseString(token);
        }

        private String parseString(String tokenString) {
            return tokenString.substring(1, tokenString.length() - 1);
        }
    }

    @Getter
    @ToString
    public static class EmptyLiteral extends Literal {
        public EmptyLiteral(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }

}
