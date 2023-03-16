import data.View;
import parser.syntax.Statement;
import tokenizer.TokenKind;
import tokenizer.Tokenizer;
import parser.*;
import parser.syntax.Expression;
import parser.syntax.NodeListener;
import parser.syntax.NodeVisitor;

public class Main {
    public static void main(String[] args) {
        var test = "if(a * 2) { 1 + 2; 2 + 3;}";

        var tokenizer = new Tokenizer(test);
        var tokenizerResult = tokenizer.tokenize();
        if (tokenizerResult.isError()) {
            System.out.println("Lex Error: " + tokenizerResult.getMessage());
            return;
        }
        var tokens = tokenizerResult.getTokens();
        tokens = tokens.stream().filter(token -> token.kind() != TokenKind.WHITESPACE).toList();

        var result = Parser.parseStatement(View.of(tokens));
        if (result.isError()) {
            System.out.println(result.getMessage());
            return;
        }

        // Print the AST
        Statement statement = result.getValue();
        NodeListener listener = new NodePrinter();
        NodeVisitor visitor = new NodeVisitor(listener);
        visitor.visit(statement);

    }
}