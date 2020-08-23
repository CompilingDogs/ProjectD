package tokens;

import exception.LexicalAnalysisExceprion;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

import static java.lang.String.format;

@Slf4j
@ToString
@SuperBuilder
public class Identifier extends Token {

    private static final Pattern regexp = Pattern.compile("^([a-zA-Z_][a-zA-Z\\d_]*)$");

    public Identifier(String token, Integer line, Integer column) throws LexicalAnalysisExceprion {
        super(token, line, column);
        if (!isAcceptableIdentifier(token)) {
            var message = format(
                    "Error in lexical analysis at line - %d, column - %d. Unacceptable Identifier: %s.",
                    line, column, token
            );
            log.error(message);
            throw new LexicalAnalysisExceprion(message);
        }
    }

    private static Boolean isAcceptableIdentifier(String token) {
        var matcher = regexp.matcher(token);
        return !token.isBlank() && matcher.matches();
    }

}
