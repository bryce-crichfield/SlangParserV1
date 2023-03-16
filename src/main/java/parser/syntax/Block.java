package parser.syntax;

import java.util.List;

public record Block(List<Statement> statements) implements Node {
    public static Block of(List<Statement> statements) {
        return new Block(statements);
    }
}

