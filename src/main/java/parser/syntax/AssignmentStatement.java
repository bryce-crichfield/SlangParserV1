package parser.syntax;

public record AssignmentStatement(Identifier identifier, Expression expression) implements Node {
    public static AssignmentStatement of(Identifier identifier, Expression expression) {
        return new AssignmentStatement(identifier, expression);
    }
}

