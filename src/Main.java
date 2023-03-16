import parse.*;
import parse.syntax.Expression;
import parse.syntax.NodeListener;
import parse.syntax.NodeVisitor;

public class Main {
    public static void main(String[] args) {
        var test = "1 + 2 * 3 / (5 - 1) * 2";
        var tokenizer = new Tokenizer(test);
        var error = tokenizer.tokenize();
        if (error.isError()) {
            System.out.println("Lex Error: " + error.message());
            return;
        }

        var tokens = tokenizer.getTokens();
        tokens = tokens.filter(TokenKind.WHITESPACE);
        tokens.dump();

        var result = Parser.parseExpression(tokens);
        if (result.isError()) {
            System.out.println(result.message());
            return;
        }

        // Print the AST
        Expression expression = result.value();
        NodeListener listener = new NodePrinter();
        NodeVisitor visitor = new NodeVisitor(listener);
        visitor.visit(expression);

    }
}