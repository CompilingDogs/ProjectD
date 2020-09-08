package tokens;

public class Token {
    private String token;
    private Integer line;
    private Integer column;

    public Token(String token, Integer line, Integer column) {
        this.token = token;
        this.line = line;
        this.column = column;
    }

    public Token() {}

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

    public String getToken() {return this.token;}

    public void setToken(String token) {this.token = token; }

    public Integer getLine() {return this.line;}

    public void setLine(Integer line) {this.line = line; }

    public Integer getColumn() {return this.column;}

    public void setColumn(Integer column) {this.column = column; }
}
