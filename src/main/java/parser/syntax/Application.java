package parser.syntax;

import java.util.List;

public record Application(Identifier identifier, List<Expression> expressions) implements Node {
    public static Application of(Identifier identifier, List<Expression> expressions) {
        return new Application(identifier, expressions);
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        identifier.accept(visitor);
        for (var expression : expressions) {
            expression.accept(visitor);
        }
        visitor.exit(this);
    }
}

