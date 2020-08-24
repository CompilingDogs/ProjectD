package lexical_analyzer;

import org.junit.Test;
import stages.LexicalAnalyzer;
import tokens.Keyword;
import tokens.Separator;
import tokens.Token;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class LexicalAnalyzerTest {

    private final LexicalAnalyzer lexicalAnalyzer = LexicalAnalyzer.getInstance();

    @Test
    public void testCaseEight() throws IOException {
        var sourceCode = new File("src/test/resources/case_8.pd");
        var generatedTokens = lexicalAnalyzer.tokenize(sourceCode);
        var correctTokens = Arrays.asList(
                new Separator.NewLineSeparator("\n", 1, 1),
                new Separator.NewLineSeparator("\n", 2, 1),
                new Keyword.VarKeyword("var", 3, 1)
        );

        var fileOut = new FileOutputStream(new File("src/test/resources/analyzer_results/case_8.txt"));
        String res = "[\n" +
                generatedTokens.stream()
                        .map(Token::toString)
                        .collect(Collectors.joining(",\n")) +
                "\n]";
        fileOut.write(res.getBytes());
    }

}
