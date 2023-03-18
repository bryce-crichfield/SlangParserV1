import parser.NodeListener;
import parser.syntax.Number;
import parser.syntax.*;
import tokenizer.TokenKind;

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
    public void enter(Program program) {
        println("Program");
        indent();
    }

    @Override
    public void exit(Program program) {
        dedent();
    }

    @Override
    public void enter(FunctionDeclaration function) {
        println("FunctionDeclaration");
        indent();
    }

    @Override
    public void exit(FunctionDeclaration function) {
        dedent();
    }

    @Override
    public void enter(Parameter parameter) {
        println("Parameter");
        indent();
    }

    @Override
    public void exit(Parameter parameter) {
        dedent();
    }

    @Override
    public void enter(DataDeclaration dataType) {
        println("DataDeclaration");
        indent();
    }

    @Override
    public void exit(DataDeclaration dataType) {
        dedent();
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
    public void enter(WhileStatement whileStatement) {
        println("WhileStatement");
        indent();
    }

    @Override
    public void exit(WhileStatement whileStatement) {
        dedent();
    }

    @Override
    public void enter(AssignmentStatement assignmentStatement) {
        println("AssignmentStatement");
        indent();
    }

    @Override
    public void exit(AssignmentStatement assignmentStatement) {
        dedent();
    }

    @Override
    public void enter(DeclarationStatement declarationStatement) {
        println("DeclarationStatement");
        indent();
    }

    @Override
    public void exit(DeclarationStatement declarationStatement) {
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
    public void enter(Application application) {
        println("Application");
        indent();
    }

    @Override
    public void exit(Application application) {
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

