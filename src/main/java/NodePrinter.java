import parser.Identifier;
import parser.Node;
import parser.NodeVisitorAdapter;
import parser.Number;

public class NodePrinter extends NodeVisitorAdapter {
    private int count = 0;

    private void indent() {
        count += 3;
    }

    private void dedent() {
        count -= 3;
    }

    private void println(String message) {
        for (var i = 0; i < count; i++) {
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
}

