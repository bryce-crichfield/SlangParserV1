package parser;

import analysis.NodePrinter;
import org.junit.jupiter.api.Test;
import tokenizer.Tokenizer;
import util.FileUtils;

public class General {
    @Test
    void test() {
        var input = FileUtils.read("/home/bryce/Desktop/SlangCompiler/src/test/java/parser/general.txt");
        if (input.isEmpty()) {
            System.out.println("File not found");
            return;
        }

        var tokens = Tokenizer.tokenize(input.get());
        var tree = Program.parse(tokens);
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
