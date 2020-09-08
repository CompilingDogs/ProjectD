package tokens;

import exception.LexicalAnalysisException;
import org.slf4j.Logger;

import java.util.regex.Pattern;

import static java.lang.String.format;

public class Identifier extends Token {

    private static final Pattern regexp = Pattern.compile("^([a-zA-Z_][a-zA-Z\\d_]*)$");
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Identifier.class);

    public static Identifier getIdentifier(
            String token,
            Integer line,
            Integer column
    ) throws LexicalAnalysisException {
        log.debug("Parsing token as identifier {}: ({}:{}).", token, line, column);
        return new Identifier(token, line, column);
    }

    private Identifier(String token, Integer line, Integer column) throws LexicalAnalysisException {
        super(token, line, column);
        if (!isAcceptableIdentifier(token)) {
            var message = format(
                    "Error in lexical analysis at line - %d, column - %d. Unacceptable literal: \"%s\".",
                    line,
                    column,
                    token
            );
            throw new LexicalAnalysisException(message);
        }
    }

    private static Boolean isAcceptableIdentifier(String token) {
        var matcher = regexp.matcher(token);
        return !token.isBlank() && matcher.matches();
    }

}
