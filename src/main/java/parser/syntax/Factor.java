package parser.syntax;

public record Factor(Expression expression, Number number, Accessor accessor) implements Node {
    public static Factor of(Expression expression) {
        return new Factor(expression, null, null);
    }

    public static Factor of(Number number) {
        return new Factor(null, number, null);
    }

    public static Factor of(Accessor accessor) {
        return new Factor(null, null, accessor);
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        if (expression != null) {
            expression.accept(visitor);
        }
        if (number != null) {
            number.accept(visitor);
        }
        if (accessor != null) {
            accessor.accept(visitor);
        }
        visitor.exit(this);
    }
}

