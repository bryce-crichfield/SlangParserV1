package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Accessor implements Node {
    public List<Identifier> identifiers;

    Accessor(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    public static ParserResult<Accessor> parse(View<Token> view) {
        var identifier = Identifier.parse(view.clone());
        if (identifier.isError()) {
            return ParserResult.error(view, identifier.getMessage());
        }

        var identifiers = new ArrayList<Identifier>();
        identifiers.add(identifier.getValue());

        var extensions = Parser.zeroOrMoreSeparatedBy(
                identifier.getRemaining(),
                Identifier::parse,
                Optional.of(TokenKind.DOT)
        );
        identifiers.addAll(extensions.getValue());


        var accessor = new Accessor(identifiers);
        return ParserResult.ok(accessor, extensions.getRemaining());
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        for (var identifier : identifiers) {
            identifier.accept(visitor);
        }
        visitor.exit(this);
    }
}
