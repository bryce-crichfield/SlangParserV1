package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

import java.util.List;
import java.util.Optional;

public class Parameters implements Node {
    // -----------------------------------------------------------------------------------------------------------------
    public List<TypedIdentifier> typedIdentifiers;

    // -----------------------------------------------------------------------------------------------------------------
    Parameters(List<TypedIdentifier> typedIdentifiers) {
        this.typedIdentifiers = typedIdentifiers;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        typedIdentifiers.forEach(t -> t.accept(visitor));
        visitor.exit(this);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Parameters> parse(View<Token> token) {
        var lParenToken = Parse.token(token.clone(), TokenKind.LPAREN);
        if (lParenToken.isError()) {
            return ParserResult.error(token, lParenToken.getMessage());
        }

        var typedIdentifiers = Parse.zeroOrMoreSeparatedBy(
                lParenToken.getRemaining(),
                TypedIdentifier::parse,
                Optional.of(TokenKind.COMMA)
        );

        var rParenToken = Parse.token(typedIdentifiers.getRemaining(), TokenKind.RPAREN);
        if (rParenToken.isError()) {
            return ParserResult.error(token, rParenToken.getMessage());
        }

        return ParserResult.ok(new Parameters(typedIdentifiers.getValue()), rParenToken.getRemaining());
    }
    // -----------------------------------------------------------------------------------------------------------------
}

