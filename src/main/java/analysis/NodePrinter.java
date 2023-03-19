package analysis;

import parser.Identifier;
import parser.Node;
import parser.NodeVisitorAdapter;
import parser.Number;

public class NodePrinter extends NodeVisitorAdapter {
    // -----------------------------------------------------------------------------------------------------------------
    private final StringBuilder sb = new StringBuilder();
    private int count = 0;
    // -----------------------------------------------------------------------------------------------------------------
    private void indent() {
        count += 3;
    }

    private void dedent() {
        count -= 3;
    }

    private void println(String message) {
        sb.append(".".repeat(Math.max(0, count)));
        sb.append(message).append("\n");
    }
    // -----------------------------------------------------------------------------------------------------------------
    public void defaultEnter(Node node) {
        println(node.getClass().getSimpleName());
        indent();
    }

    public void defaultExit(Node node) {
        dedent();
    }
    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void exit(Number number) {
        println("value: " + number.value);
        super.exit(number);
    }

    @Override
    public void exit(Identifier identifier) {
        println("name: " + identifier.value);
        super.exit(identifier);
    }
    // -----------------------------------------------------------------------------------------------------------------
    public String toString() {

        return sb.toString();
    }
    // -----------------------------------------------------------------------------------------------------------------
}

