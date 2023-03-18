import data.View;
import parser.NodeListener;
import parser.NodeVisitor;
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
        String test = "fn add(): int { call(x); let x: int = 2;}";

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
        NodeListener listener = new NodePrinter();
        NodeVisitor visitor = new NodeVisitor(listener);
        visitor.visit(tree);
    }
}