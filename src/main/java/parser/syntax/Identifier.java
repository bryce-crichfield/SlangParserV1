package parser.syntax;

public record Identifier(String name) implements Node {
    public static Identifier of(String name) {
        return new Identifier(name);
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }

    public String toString() {
        return name;
    }
}

