# ProjectD
Dynamic Programming Language Interpreter for Compiler Construction Course at Innopolis University

# Instructions

## Setting Up The Project

First of all, in order to be able to work with the project, you 
will need to clone the repository to your local computer.
Open the **Command-Line Interface**(in the text be referred as **CLI**) of your operating system in the directory you want
to clone the project in and type the following command:
```bash
    git clone https://github.com/CompilingDogs/ProjectD.git
``` 
The next steps are following:
1. Open the project with the IDE of your choice.
2. Open CLI in the directory, where the project is stored.
3. Run the following command in the CLI:
```bash
    gradlew build
```

The Last command should have built all the dependencies. Now, you should be able to work with the 
project as with any other project from your IDE. If you still have troubles with running 
the project, please contact **Maxim**.

## Working with Lexical Analyzer

Source code for the Lexical Analysis Stage kept in the **src/main/java** directory.

If you want to see the implementation of the Lexical analyzer, you can refer to the class
**stages.LexicalAnalyzer**. If you want to see the implementation of tokens, you can refer
to all the classes in package **tokens**.

All the information related to the testing kept in **src/test/java** directory.

If you would like to test the implementation on some cases, you can do the following:
1. Add your test case to **src/test/resources** with **.pd** extension.
2. Open Class **src/test/java/lexical_analyzer/LexicalAnalyzerTest.java**.
3. Scroll down to the **testingSourceCodes** field:
```java
public class LexicalAnalyzerTest {
    // ,,,
    private final String[] testingSourceCodes = new String[]{
                "src/test/resources/case_1.pd",
                "src/test/resources/case_2.pd",
                "src/test/resources/case_3.pd",
                "src/test/resources/case_4.pd",
                "src/test/resources/case_5.pd"
    };
    // ...
}
```
4. Add your test case to the list.
5. **Right-click in the editor** -> click **Run \'LexicalAnalyzerTest\'**.
6. Look for your results in **src/test/resources/lexical_analyzer_results**.
7. **Right-Click on the appeared .json file** -> Click **Reformat code**.
8. Congratulations, you are done!

Notice that we are using Google's **gson** library to create beautiful **json**
from our list of tokens, so there may be some problems with appearance of some
 symbols. For example, they might appear as '\\u0023d', etc.

If you have any questions regarding Lexical Analysis,  please contact **Alecsey**.