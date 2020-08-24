package tokens;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    private String token;
    private Integer line;
    private Integer column;

    public static Token tokenize(String token, Integer line, Integer column) {
        if (Separator.isSeparator(token)) {
            return Separator.getSeparator(token, line, column);
        } else if (Operator.isOperator(token)) {
            return Operator.getOperator(token, line, column);
        } else if (Keyword.isKeyword(token)) {
            return Keyword.getKeyword(token, line, column);
        } else if (Literal.isLiteral(token)) {
            return Literal.getLiteral(token, line, column);
        } else {
            return Identifier.getIdentifier(token, line, column);
        }
    }

    @Override
    public String toString() {
        return "tokens.Token{" +
                "token='" + token.replace("\n", "\\n") + '\'' +
                ", line=" + line +
                ", column=" + column +
                '}';
    }
}
