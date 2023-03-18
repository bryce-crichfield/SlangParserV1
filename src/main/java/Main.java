import parser.Expression;
import parser.NodeVisitor;
import util.FileUtils;
import util.View;
import parser.Parser;
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

        var tokenizer = new Tokenizer(test);
        var tokenizerResult = tokenizer.tokenize();
        if (tokenizerResult.isError()) {
            System.out.println("Lex Error: " + tokenizerResult.getMessage());
            return;
        }
        var tokens = tokenizerResult.getTokens();
        tokens = tokens.stream().filter(token -> token.kind() != TokenKind.WHITESPACE).toList();

        var result = Expression.parse(View.of(tokens));
        if (result.isError()) {
            System.out.println(result.getMessage());
            return;
        }

        // Print the AST
        var tree = result.getValue();
        NodeVisitor visitor = new NodePrinter();
        tree.accept(visitor);

        // Interpret the AST
        var interpreter = new ExpressionInterpreter();
        tree.accept(interpreter);
        System.out.println(interpreter.stack.pop());
    }
}