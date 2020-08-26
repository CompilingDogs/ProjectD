package lexical_analyzer;

import com.google.gson.*;
import org.junit.Test;
import stages.LexicalAnalyzer;
import tokens.Token;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

public class LexicalAnalyzerTest {

    private final LexicalAnalyzer lexicalAnalyzer = LexicalAnalyzer.getInstance();

    private final String[] testingSourceCodes = new String[]{
            "src/test/resources/case_2.pd"
    };

    @Test
    public void getTestJSON() throws IOException {
        for (String testingSourceCode : this.testingSourceCodes) {
            this.createTestingResults(testingSourceCode);
        }
    }

    public void createTestingResults(String fullPath) throws IOException {
        var gson = new GsonBuilder().registerTypeHierarchyAdapter(Token.class, new TDeserializer())
                                    .create();
        var sourceCode = new File(fullPath);
        var fname = sourceCode.getName().split("\\.")[0];
        var generatedTokens = lexicalAnalyzer.tokenize(sourceCode);

        var fileOut = new FileOutputStream(
                new File("src/test/resources/analyzer_results/" + fname + ".json")
        );

        String res = gson.toJson(generatedTokens);
        fileOut.write(res.getBytes());
    }

    public static class TDeserializer implements JsonSerializer<Token> {
        @Override
        public JsonElement serialize(Token src, Type typeOfSrc, JsonSerializationContext context) {
            Gson gson = new Gson();
            JsonElement serialize = gson.toJsonTree(src);
            JsonObject o = (JsonObject) serialize;
            o.addProperty("class", src.getClass().getSimpleName());
            return serialize;
        }
    }
}
