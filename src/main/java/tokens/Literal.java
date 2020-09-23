package tokens;

import exception.LexicalAnalysisException;
import org.slf4j.Logger;

import java.util.regex.Pattern;

import static java.lang.String.format;

public class Literal extends Token {

    public static final String EMPTY_LITERAL_TOKEN = "empty";
    private static final Pattern numericPattern = Pattern.compile("^[-+]?\\d*\\.?\\d*$");
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Literal.class);

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

    private static Boolean isNumeric(String token) {
        if (token == null) {
            return false;
        }
        return numericPattern.matcher(token).matches();
    }

    public static Boolean isIntegerLiteral(String token) {
        return isNumeric(token) && !token.contains(".");
    }

    public static Boolean isRealLiteral(String token) {
        return isNumeric(token);
    }

    public static Boolean isLiteral(String token) {
        return isRealLiteral(token)
                || isIntegerLiteral(token)
                || isStringLiteral(token)
                || isEmptyLiteral(token);
    }

    /**
     * Converts string containing token to the Literal class.
     *
     * @param token  - String containing literal
     * @param line   - Line number of the token occurrence
     * @param column - Column number of the token's first character occurrence
     * @return Instance of one of Literal's Successors.
     */
    public static Literal getLiteral(
            String token,
            Integer line,
            Integer column
    ) throws LexicalAnalysisException {
        log.debug("Parsing token as literal {}: ({}:{}).", token, line, column);
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
                    "Error in lexical analysis at line - %d, column - %d. Unacceptable literal: \"%s\".",
                    line,
                    column,
                    token
            );
            throw new LexicalAnalysisException(message);
        }
    }

    public static class IntegerLiteral extends Literal {
        private final Integer value;

        public IntegerLiteral(String token, Integer line, Integer column) {
            super(token, line, column);
            this.value = Integer.parseInt(token);
        }

        public Integer getValue() {return this.value;}
    }

    public static class RealLiteral extends Literal {
        private final Double value;

        public RealLiteral(String token, Integer line, Integer column) {
            super(token, line, column);
            this.value = Double.parseDouble(token);
        }

        public Double getValue() {return this.value;}
    }

    public static class StringLiteral extends Literal {
        private final String value;

        public StringLiteral(String token, Integer line, Integer column) {
            super(token, line, column);
            this.value = parseString(token);
        }

        private String parseString(String tokenString) {
            return tokenString.substring(1, tokenString.length() - 1);
        }

        public String getValue() {return this.value;}
    }

    public static class EmptyLiteral extends Literal {
        public EmptyLiteral(String token, Integer line, Integer column) {
            super(token, line, column);
        }
    }
}
