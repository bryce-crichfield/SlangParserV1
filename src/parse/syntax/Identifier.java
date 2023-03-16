package parse.syntax;

public record Identifier(String name) implements Node {
    public static Identifier of(String name) {
        return new Identifier(name);
    }

}

