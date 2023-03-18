package parser.syntax;

import java.util.List;

public record Accessor(List<Identifier> identifiers) implements Node {
    public static Accessor of(List<Identifier> identifiers) {
        return new Accessor(identifiers);
    }

    public static Accessor of(Identifier identifier) {
        return new Accessor(List.of(identifier));
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        for (var identifier : identifiers) {
            identifier.accept(visitor);
        }
        visitor.exit(this);
    }
}
