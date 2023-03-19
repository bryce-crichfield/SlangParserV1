package parser;

import tokenizer.Token;
import util.View;

public class TypedIdentifier implements Node {
    // -----------------------------------------------------------------------------------------------------------------
    public Identifier identifier;
    public TypeSpecifier type;

    // -----------------------------------------------------------------------------------------------------------------
    TypedIdentifier(Identifier identifier, TypeSpecifier type) {
        this.identifier = identifier;
        this.type = type;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        identifier.accept(visitor);
        type.accept(visitor);
        visitor.exit(this);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<TypedIdentifier> parse(View<Token> view) {
        var identifier = Identifier.parse(view.clone());
        if (identifier.isError()) {
            return ParserResult.error(view, identifier.getMessage());
        }

        var type = TypeSpecifier.parse(identifier.getRemaining());
        if (type.isError()) {
            return ParserResult.error(view, type.getMessage());
        }

        return ParserResult.ok(new TypedIdentifier(identifier.getValue(), type.getValue()), type.getRemaining());
    }
    // -----------------------------------------------------------------------------------------------------------------
}
