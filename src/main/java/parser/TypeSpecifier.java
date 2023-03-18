package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

import static parser.Parser.token;

public class TypeSpecifier implements Node {
    Identifier type;

    public TypeSpecifier(Identifier type) {
        this.type = type;
    }

    public static ParserResult<TypeSpecifier> parse(View<Token> view) {
        var colon = token(view, TokenKind.COLON);
        if (colon.isError()) {
            return ParserResult.error(view, colon.getMessage());
        }

        var type = Identifier.parse(colon.getRemaining());
        if (type.isError()) {
            return ParserResult.error(view, type.getMessage());
        }

        return ParserResult.ok(new TypeSpecifier(type.getValue()), type.getRemaining());
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        type.accept(visitor);
        visitor.exit(this);
    }
}
