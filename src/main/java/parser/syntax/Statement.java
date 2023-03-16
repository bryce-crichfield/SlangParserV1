package parser.syntax;

public record Statement(IfStatement ifStatement, Expression expression) {
    public static Statement of(IfStatement ifStatement) {
        return new Statement(ifStatement, null);
    }

    public static Statement of(Expression expression) {
        return new Statement(null, expression);
    }
}
