package stages;

import exception.LexicalAnalysisException;
import org.slf4j.Logger;
import tokens.Token;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static stages.LexicalAnalyzer.State.*;
import static tokens.Operator.*;
import static tokens.Separator.*;

public class LexicalAnalyzer {
    public static final String COLON_ASSIGNMENT_OPERATOR_TOKEN = ":";
    private static final HashSet<String> bracketTokenChars = new HashSet<>(
            Arrays.asList(
                    OPENING_PARENTHESIS_SEPARATOR_TOKEN,
                    CLOSING_PARENTHESIS_SEPARATOR_TOKEN,
                    OPENING_BRACKET_SEPARATOR_TOKEN,
                    CLOSING_BRACKET_SEPARATOR_TOKEN,
                    OPENING_CURLY_BRACKET_SEPARATOR_TOKEN,
                    CLOSING_CURLY_BRACKET_SEPARATOR_TOKEN
            )
    );
    private static final HashSet<String> operatorTokenChars = new HashSet<>(
            Arrays.asList(
                    ARITHMETIC_DIVISION_OPERATOR_TOKEN,
                    ARITHMETIC_MINUS_OPERATOR_TOKEN,
                    ARITHMETIC_MULTIPLICATION_OPERATOR_TOKEN,
                    ARITHMETIC_PLUS_OPERATOR_TOKEN,
                    COMPARISON_EQUAL_OPERATOR_TOKEN,
                    COMPARISON_GREATER_OPERATOR_TOKEN,
                    COMPARISON_LESS_OPERATOR_TOKEN,
                    COLON_ASSIGNMENT_OPERATOR_TOKEN
            )
    );
    private static final HashSet<String> separatorTokenChars = new HashSet<>(
            Arrays.asList(
                    SEMICOLON_SEPARATOR_TOKEN,
                    COMMA_SEPARATOR_TOKEN,
                    PERIOD_SEPARATOR_TOKEN,
                    WHITE_SPACE_SEPARATOR_TOKEN,
                    TAB_SPACE_SEPARATOR_TOKEN,
                    NEW_LINE_SEPARATOR_TOKEN,
                    WINDOWS_NEW_LINE_SEPARATOR_TOKEN
            )
    );
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(LexicalAnalyzer.class);

    private static LexicalAnalyzer instance;

    private LexicalAnalyzer() {}

    public static LexicalAnalyzer getInstance() {
        if (isNull(instance)) {
            instance = new LexicalAnalyzer();
        }
        return instance;
    }

    private static Boolean isBracketTokenChar(Character curChar) {
        return bracketTokenChars.contains(String.valueOf(curChar));
    }

    private static Boolean isOperatorTokenChar(Character curChar) {
        return operatorTokenChars.contains(String.valueOf(curChar));
    }

    private static Boolean isSeparatorTokenChar(Character curChar) {
        return separatorTokenChars.contains(String.valueOf(curChar));
    }

    private StringBuilder buffer;
    private List<Token> tokens;
    private int line;
    private int column;
    private State currState;
    private FileReader reader;

    /**
     * Function, that scans given input file and returns list of tokens
     *
     * @param inputFile file containing source code
     * @return List of tokens
     * @throws IOException              if <b>input file</b> does not exist.
     * @throws LexicalAnalysisException if <b>input file</b> contains lexically incorrect program.
     */
    public List<Token> tokenize(File inputFile) throws IOException, LexicalAnalysisException {
        log.info("Initializing Lexical Analyzer for Parsing {}.", inputFile.getAbsolutePath());
        this.initialize(inputFile);
        log.info("Lexical Analyzer initialized correctly. Starting parsing phase.");
        long startTime = System.nanoTime();

        int curr;
        while ((curr = this.reader.read()) != -1) {
            var currChar = (char) curr;
            if (isBracketTokenChar(currChar)) {
                readBracket(currChar);
            } else if (isOneLineComment(currChar)) {
                skipOneLineComment();
            } else if (isMultiLineComment(currChar)) {
                skipMultiLineComment(currChar);
            } else if (isStringLiteral(currChar)) {
                readStringLiteral(currChar);
            } else {
                var newState = getState(currChar);
                if (newState == currState && newState != READ_SEP) {
                    buffer.append(currChar);
                } else {
                    this.commitBufferedToken();
                    currState = newState;
                    if (newState == READ_ALPHA || newState == READ_OP || isPersistentSeparator(
                            String.valueOf(currChar))) {
                        buffer.append(currChar);
                    } else if (newState == EMPTY) {
                        var message = format(
                                "Error in lexical analysis at line - %d, column - %d. Unrecognized Character: \"%s\".",
                                this.line,
                                this.column,
                                currChar
                        );
                        throw new LexicalAnalysisException(message);
                    }
                }
                this.setNewPosition(currChar);
            }
        }
        this.commitBufferedToken();
        long elapsedTime = System.nanoTime() - startTime;

        log.info(
                "Parsing finished successfully. Time taken to parse sourcecode: {} ms.",
                elapsedTime / 1000000.0
        );
        return tokens;
    }


    private boolean isStringLiteral(Character currChar) {
        return currChar == '\"' || currChar == '\'';
    }

    private boolean isMultiLineComment(Character currChar) {
        return currChar == '*' && this.buffer.length() == 1 && this.buffer.charAt(0) == '/';
    }

    private boolean isOneLineComment(Character currChar) {
        return currChar == '/' && this.buffer.length() == 1 && this.buffer.charAt(0) == '/';
    }

    private void initialize(File inputFile) throws FileNotFoundException {
        this.tokens = new ArrayList<>();
        this.reader = new FileReader(inputFile);
        this.buffer = new StringBuilder();
        this.currState = EMPTY;
        this.column = 1;
        this.line = 1;
    }

    private void readStringLiteral(Character startingQuote) throws IOException {
        int startingLine = this.line;
        int startingColumn = this.column;
        this.commitBufferedToken();
        this.setNewPosition(startingQuote);
        this.buffer.append(startingQuote);

        char curr;
        char prev = ' ';
        while (((curr = (char) this.reader.read()) != startingQuote) || (prev == '\\')) {
            if (curr == '\n' || !this.reader.ready()) {
                var message = format(
                        "Error in lexical analysis at line - %d, column - %d. Unacceptable literal: \"%s\".",
                        startingLine,
                        startingColumn,
                        buffer.toString()
                );
                throw new LexicalAnalysisException(message);
            }
            this.buffer.append(curr);
            this.setNewPosition(curr);
            prev = curr;
        }

        this.buffer.append(curr);
        this.setNewPosition(curr);
        this.commitBufferedToken();
    }

    private void skipMultiLineComment(Character currChar) throws IOException {
        this.setNewPosition(currChar);
        this.cleanBuffer();

        char currC;
        char prevC = ' ';
        while (!((currC = (char) this.reader.read()) == '/' && prevC == '*')) {
            prevC = currC;
            this.setNewPosition(currC);
        }

        this.setNewPosition(currC);
    }

    private void skipOneLineComment() throws IOException {
        //noinspection StatementWithEmptyBody
        while ((char) this.reader.read() != '\n') { }
        this.setNewPosition('\n');
        this.cleanBuffer();
    }

    private void readBracket(Character currChar) {
        this.commitBufferedToken();
        this.setNewPosition(currChar);
        this.commitToken(String.valueOf(currChar));
        this.currState = EMPTY;
    }

    private void commitBufferedToken() {
        if (this.buffer.length() != 0) {
            commitToken(this.buffer.toString());
            cleanBuffer();
        }
    }

    private void commitToken(String token) {
        this.tokens.add(Token.tokenize(token, this.line, this.column - token.length()));
    }

    private void cleanBuffer() {
        this.buffer = new StringBuilder();
    }

    private void setNewPosition(Character curr) {
        var currStringChar = String.valueOf(curr);
        if (currStringChar.equals(NEW_LINE_SEPARATOR_TOKEN)) {
            ++this.line;
            this.column = 1;
        } else if (currStringChar.equals(TAB_SPACE_SEPARATOR_TOKEN)) {
            this.column += 4;
        } else {
            ++this.column;
        }
    }

    public enum State {
        EMPTY, READ_SEP, READ_ALPHA, READ_OP;

        public static State getState(Character currChar) {
            if (isLetter(currChar) || isDigit(currChar) || currChar == '_') {
                return READ_ALPHA;
            } else if (isSeparatorTokenChar(currChar)) {
                return READ_SEP;
            } else if (isOperatorTokenChar(currChar)) {
                return READ_OP;
            } else {
                return EMPTY;
            }
        }
    }

}