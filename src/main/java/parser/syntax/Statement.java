package parser.syntax;

public record Statement(
        IfStatement ifStatement,
        WhileStatement whileStatement,
        AssignmentStatement assignmentStatement,
        DeclarationStatement declarationStatement,
        Expression expression) implements Node
{


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
}
