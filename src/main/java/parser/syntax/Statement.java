package parser.syntax;

public record Statement(
        IfStatement ifStatement,
        WhileStatement whileStatement,
        AssignmentStatement assignmentStatement,
        DeclarationStatement declarationStatement,
        Expression expression) implements Node {


    public static Statement of(IfStatement ifStatement) {
        return new Statement(ifStatement, null, null, null, null);
    }

    public static Statement of(WhileStatement whileStatement) {
        return new Statement(null, whileStatement, null, null, null);
    }

    public static Statement of(AssignmentStatement assignmentStatement) {
        return new Statement(null, null, assignmentStatement, null, null);
    }

    public static Statement of(DeclarationStatement declarationStatement) {
        return new Statement(null, null, null, declarationStatement, null);
    }

    public static Statement of(Expression expression) {
        return new Statement(null, null, null, null, expression);
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        if (ifStatement != null) {
            ifStatement.accept(visitor);
        }
        if (whileStatement != null) {
            whileStatement.accept(visitor);
        }
        if (assignmentStatement != null) {
            assignmentStatement.accept(visitor);
        }
        if (declarationStatement != null) {
            declarationStatement.accept(visitor);
        }
        if (expression != null) {
            expression.accept(visitor);
        }
        visitor.exit(this);
    }
}
