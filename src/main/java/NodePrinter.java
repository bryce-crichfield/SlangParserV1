import tokenizer.TokenKind;
import parser.syntax.*;
import parser.syntax.Number;

public class NodePrinter implements NodeListener {
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

    @Override
    public void enter(Statement statement) {
        println("Statement");
        indent();
    }

    @Override
    public void exit(Statement statement) {
        dedent();
    }

    @Override
    public void enter(IfStatement ifStatement) {
        println("IfStatement");
        indent();
    }

    @Override
    public void exit(IfStatement ifStatement) {
        dedent();
    }

    @Override
    public void enter(Block block) {
        println("Block");
        indent();
    }

    @Override
    public void exit(Block block) {
        dedent();
    }

    @Override
    public void enter(Expression expression) {
        println("Expr");
        indent();
    }

    @Override
    public void exit(Expression expression) {
        dedent();
    }

    @Override
    public void enter(Term term) {
        println("Term");
        indent();
    }

    @Override
    public void exit(Term term) {
        dedent();
    }

    @Override
    public void enter(Factor factor) {
        println("Factor");
        indent();
    }

    @Override
    public void exit(Factor factor) {
        dedent();
    }

    @Override
    public void enter(Number number) {
        println("Number");
        indent();
    }

    @Override
    public void exit(Number number) {
        dedent();
    }

    @Override
    public void enter(Identifier identifier) {
        println("Identifier");
        indent();
    }

    @Override
    public void exit(Identifier identifier) {
        dedent();
    }

    @Override
    public void enter(TokenKind tokenKind) {
        println(tokenKind.toString());
        indent();
    }

    @Override
    public void exit(TokenKind tokenKind) {
        dedent();
    }
}

