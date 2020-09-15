import com.compilingdogs.parser.ParserKt;
import org.slf4j.Logger;
import stages.LexicalAnalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Main.class);

    private static final LexicalAnalyzer lexicalAnalyzer = LexicalAnalyzer.getInstance();

    public static void main(String[] args) {
        if (args.length != 1){
            log.error("Usage: ./ProjectD filename");
            System.exit(1);
        }
        var srcFileName = args[0];

        try {
            var srcFile = new File(srcFileName);
            var tokens = lexicalAnalyzer.tokenize(srcFile);
            var ast = ParserKt.parse(tokens);
        } catch (IOException e) {
            log.error(e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            log.error("Усё погано: {}", e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}