import parse.*;
import parse.syntax.Expression;
import parse.syntax.NodeListener;
import parse.syntax.NodeVisitor;

public class Main {
    public static void main(String[] args) {
        var test = "1 + 2 * 3 / (5 - 1) * 2";

        var tokenizer = new DefaultTokenizer(test);
        var tokenizerResult = tokenizer.tokenize();
        if (tokenizerResult.isError()) {
            System.out.println("Lex Error: " + tokenizerResult.getMessage());
            return;
        }
        var tokens = tokenizerResult.getTokens();
        tokens = tokens.stream().filter(token -> token.kind() != TokenKind.WHITESPACE).toList();

        var result = Parser.parseExpression(TokenView.of(tokens));
        if (result.isError()) {
            System.out.println(result.getMessage());
            return;
        }

        // Print the AST
        Expression expression = result.getValue();
        NodeListener listener = new NodePrinter();
        NodeVisitor visitor = new NodeVisitor(listener);
        visitor.visit(expression);

    }
}