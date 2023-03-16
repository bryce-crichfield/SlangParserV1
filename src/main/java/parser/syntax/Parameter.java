package parser.syntax;

public record Parameter(Identifier identifier, Identifier type) implements Node {
    public static Parameter of(Identifier identifier, Identifier type) {
        return new Parameter(identifier, type);
    }
}

