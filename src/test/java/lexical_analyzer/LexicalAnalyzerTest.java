package lexical_analyzer;

import com.google.gson.*;
import exception.LexicalAnalysisException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import stages.LexicalAnalyzer;
import tokens.Token;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

@Slf4j
public class LexicalAnalyzerTest {

    private final LexicalAnalyzer lexicalAnalyzer = LexicalAnalyzer.getInstance();

    private final String[] testingSourceCodes = new String[]{
<<<<<<< HEAD
            "src/test/resources/case_7.pd",
            "src/test/resources/case_8.pd",
            "src/test/resources/case_9.pd",
            "src/test/resources/case_10.pd",
            "src/test/resources/case_11.pd",
            "src/test/resources/case_12.pd",
            "src/test/resources/case_13.pd",
            "src/test/resources/case_14.pd",
            "src/test/resources/case_15.pd",
            "src/test/resources/case_16.pd"
=======
            "src/test/resources/case_2.pd"
>>>>>>> test/PD-Lexical_Analysis
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

        try {
            var generatedTokens = lexicalAnalyzer.tokenize(sourceCode);
            var fileOut = new FileOutputStream(
                    new File("src/test/resources/analyzer_results/" + fname + ".json")
            );

            String res = gson.toJson(generatedTokens);
            fileOut.write(res.getBytes());
        } catch (LexicalAnalysisException e){
            log.error(e.getMessage());
            e.printStackTrace();
        }


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
