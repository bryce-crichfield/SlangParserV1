package parser.syntax;

import java.util.List;

public record DataDeclaration(Identifier identifier, List<DeclarationStatement> declarations) implements Node {
    public static DataDeclaration of(Identifier identifier, List<DeclarationStatement> declarations) {
        return new DataDeclaration(identifier, declarations);
    }
}

