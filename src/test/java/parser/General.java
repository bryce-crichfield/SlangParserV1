package parser;

import analysis.NodePrinter;
import org.junit.jupiter.api.Test;
import tokenizer.Tokenizer;

public class General {
    @Test
    void test() {
        var input = "fn add(a: num, b: num): num { return a + b; }";
        var tokens = Tokenizer.tokenize(input);
        var tree = FunctionDeclaration.parse(tokens);
        var printer = new NodePrinter();
        if (tree.isError()) {
            System.out.println("Error: " + tree.getMessage());
        } else {
            tree.getValue().accept(printer);
            System.out.println(printer);
            if (!tree.getRemaining().isEmpty()) {
                System.out.println("Remaining tokens:");
                tree.getRemaining().forEach(System.out::println);
            }
        }
    }
}
