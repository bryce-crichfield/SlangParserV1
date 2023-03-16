package parser.syntax;

public record DeclarationStatement(Identifier identifier, Identifier type, Expression expression) implements Node {
    public static DeclarationStatement of(Identifier identifier, Identifier type, Expression expression) {
        return new DeclarationStatement(identifier, type, expression);
    }
}

