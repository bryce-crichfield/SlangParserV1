package parser.syntax;

import java.util.List;

public record FunctionDeclaration(Identifier identifier, List<Parameter> parameters, Identifier type, Block block) implements Node {
    public static FunctionDeclaration of(Identifier identifier, List<Parameter> parameters, Identifier type, Block block) {
        return new FunctionDeclaration(identifier, parameters, type, block);
    }
}