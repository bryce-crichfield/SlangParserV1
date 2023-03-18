import parser.syntax.Number;
import parser.syntax.*;
import tokenizer.TokenKind;

public class NodePrinter extends NodeVisitorAdapter {
    private int count = 0;

    private void indent() {
        count += 3;
    }

    private void dedent() {
        count -= 3;
    }

    private void println(String message) {
        for (int i = 0; i < count; i++) {
            System.out.print(".");
        }
        System.out.println(message);
    }

    public void defaultEnter(Node node) {
        println(node.getClass().getSimpleName());
        indent();
    }

    public void defaultExit(Node node) {
        dedent();
    }
}

