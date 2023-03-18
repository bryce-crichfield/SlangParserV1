package parser.syntax;

import java.util.List;

public record Block(List<Statement> statements) implements Node {
    public static Block of(List<Statement> statements) {
        return new Block(statements);
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        for (var statement : statements) {
            statement.accept(visitor);
        }
        visitor.exit(this);
    }
}

