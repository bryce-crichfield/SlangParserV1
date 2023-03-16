package parser.syntax;

public record Factor(Expression expression, Number number, Identifier identifier) implements Node {
    public static Factor of(Expression expression) {
        return new Factor(expression, null, null);
    }

    public static Factor of(Number number) {
        return new Factor(null, number, null);
    }

    public static Factor of(Identifier identifier) {
        return new Factor(null, null, identifier);
    }
}

