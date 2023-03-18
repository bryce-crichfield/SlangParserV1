package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

import java.util.List;
import java.util.Optional;

public class CompositeIdentifier implements Node {
    public List<Identifier> identifiers;

    CompositeIdentifier(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    public static ParserResult<CompositeIdentifier> parse(View<Token> view) {
        var identifiers = Parse.oneOrMoreSeparatedBy(view, Identifier::parse, Optional.of(TokenKind.DOT));
        if (identifiers.isError()) {
            return ParserResult.error(view, "Expected a composite identifier");
        }

        var compositeIdentifier = new CompositeIdentifier(identifiers.getValue());
        return ParserResult.ok(compositeIdentifier, identifiers.getRemaining());
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        for (var identifier : identifiers) {
            identifier.accept(visitor);
        }
        visitor.exit(this);
    }
}
