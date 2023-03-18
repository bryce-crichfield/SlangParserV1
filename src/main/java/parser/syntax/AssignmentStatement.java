package parser.syntax;

public record AssignmentStatement(Accessor accessor, Expression expression) implements Node {
    public static AssignmentStatement of(Accessor accessor, Expression expression) {
        return new AssignmentStatement(accessor, expression);
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        accessor.accept(visitor);
        expression.accept(visitor);
        visitor.exit(this);
    }
}

