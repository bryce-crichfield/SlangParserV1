package parser.syntax;

import java.util.List;

public record FunctionDeclaration(Identifier identifier, List<Parameter> parameters, Identifier type,
                                  Block block) implements Node {
    public static FunctionDeclaration of(Identifier identifier, List<Parameter> parameters, Identifier type,
            Block block
    ) {
        return new FunctionDeclaration(identifier, parameters, type, block);
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        identifier.accept(visitor);
        for (var parameter : parameters) {
            parameter.accept(visitor);
        }
        type.accept(visitor);
        block.accept(visitor);
        visitor.exit(this);
    }
}