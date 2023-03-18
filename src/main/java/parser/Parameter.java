package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Parameter implements Node {
    public Identifier identifier;
    public Identifier type;

    Parameter(Identifier identifier, Identifier type) {
        this.identifier = identifier;
        this.type = type;
    }

    public static ParserResult<List<Parameter>> parse(View<Token> token) {
        var lParenToken = Parser.token(token.clone(), TokenKind.LPAREN);
        if (lParenToken.isError()) {
            return ParserResult.error(token, lParenToken.getMessage());
        }

        // Short circuit if there are no parameters
        var rParenToken = Parser.token(lParenToken.getRemaining(), TokenKind.RPAREN);
        if (rParenToken.isOk()) {
            return ParserResult.ok(List.of(), rParenToken.getRemaining());
        }

        var typedIdentifiers = Parser.oneOrMoreSeparatedBy(
                lParenToken.getRemaining(),
                Parser::parseTypedIdentifier,
                Optional.of(TokenKind.COMMA)
        );
        var parameters = new ArrayList<Parameter>();
        if (typedIdentifiers.isOk()) {
            for (var typedIdentifier : typedIdentifiers.getValue()) {
                var parameter = new Parameter(typedIdentifier.first(), typedIdentifier.second());
                parameters.add(parameter);
            }
        }

        rParenToken = Parser.token(typedIdentifiers.getRemaining(), TokenKind.RPAREN);
        if (rParenToken.isError()) {
            return ParserResult.error(token, rParenToken.getMessage());
        }

        return ParserResult.ok(parameters, rParenToken.getRemaining());
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        identifier.accept(visitor);
        type.accept(visitor);
        visitor.exit(this);
    }
}

