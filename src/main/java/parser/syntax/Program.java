package parser.syntax;

import java.util.List;

public record Program(List<FunctionDeclaration> functions, List<DataDeclaration> dataTypes) implements Node {
    public static Program of(List<FunctionDeclaration> functions, List<DataDeclaration> dataTypes) {
        return new Program(functions, dataTypes);
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        for (var function : functions) {
            function.accept(visitor);
        }

        for (var dataType : dataTypes) {
            dataType.accept(visitor);
        }
        visitor.exit(this);
    }
}
