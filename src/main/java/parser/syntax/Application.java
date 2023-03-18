package parser.syntax;

import java.util.List;

public record Application(Identifier identifier, List<Expression> expressions) implements Node {
    public static Application of(Identifier identifier, List<Expression> expressions) {
        return new Application(identifier, expressions);
    }
}

