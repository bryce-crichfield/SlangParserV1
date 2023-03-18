package parser.syntax;

public record Parameter(Identifier identifier, Identifier type) implements Node {
    public static Parameter of(Identifier identifier, Identifier type) {
        return new Parameter(identifier, type);
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        identifier.accept(visitor);
        type.accept(visitor);
        visitor.exit(this);
    }
}

