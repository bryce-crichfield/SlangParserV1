import parser.syntax.NodeVisitor;
import util.FileUtils;
import util.View;
import parser.Parser;
import parser.ParserResult;
import parser.syntax.Program;
import tokenizer.Token;
import tokenizer.TokenKind;
import tokenizer.Tokenizer;
import tokenizer.TokenizerResult;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        var testOpt = FileUtils.read("src/main/java/test.slang");
        if (testOpt.isEmpty()) {
            System.out.println("Could not read test file");
            return;
        }

        var test = testOpt.get();

        Tokenizer tokenizer = new Tokenizer(test);
        TokenizerResult tokenizerResult = tokenizer.tokenize();
        if (tokenizerResult.isError()) {
            System.out.println("Lex Error: " + tokenizerResult.getMessage());
            return;
        }
        List<Token> tokens = tokenizerResult.getTokens();
        tokens = tokens.stream().filter(token -> token.kind() != TokenKind.WHITESPACE).toList();

        ParserResult<Program> result = Parser.parseProgram(View.of(tokens));
        if (result.isError()) {
            System.out.println(result.getMessage());
            return;
        }

        // Print the AST
        Program tree = result.getValue();
        NodeVisitor visitor = new NodePrinter();
        tree.accept(visitor);
    }
}