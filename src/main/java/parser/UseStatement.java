package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

import java.util.List;
import java.util.Optional;

public class UseStatement implements Node {
    // -----------------------------------------------------------------------------------------------------------------
    public List<CompositeIdentifier> identifiers;
    // -----------------------------------------------------------------------------------------------------------------
    public UseStatement(List<CompositeIdentifier> identifiers) {
        this.identifiers = identifiers;
    }
    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        identifiers.forEach(i -> i.accept(visitor));
        visitor.exit(this);
    }
    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<UseStatement> parse(View<Token> view) {
        var useToken = Parse.token(view.clone(), TokenKind.USE);
        if (useToken.isError()) {
            var message = "Expected 'use' keyword but found " + useToken.getRemaining().peek(0);
            return ParserResult.error(useToken.getRemaining(), message);
        }

        var lParenToken = Parse.token(useToken.getRemaining(), TokenKind.LPAREN);
        if (lParenToken.isError()) {
            var message = "Expected '(' but found " + lParenToken.getRemaining().peek(0);
            return ParserResult.error(lParenToken.getRemaining(), message);
        }

        var comma = Optional.of(TokenKind.COMMA);
        var identifiers = Parse.oneOrMoreSeparatedBy(lParenToken.getRemaining(), CompositeIdentifier::parse, comma);
        if (identifiers.isError()) {
            var message = "Expected identifier but found " + identifiers.getRemaining().peek(0);
            return ParserResult.error(identifiers.getRemaining(), message);
        }

        var rParenToken = Parse.token(identifiers.getRemaining(), TokenKind.RPAREN);
        if (rParenToken.isError()) {
            var message = "Expected ')' but found " + rParenToken.getRemaining().peek(0);
            return ParserResult.error(rParenToken.getRemaining(), message);
        }

        var block = Block.parse(rParenToken.getRemaining());
        if (block.isError()) {
            var message = "Expected block but found " + block.getRemaining().peek(0);
            return ParserResult.error(block.getRemaining(), message);
        }

        var useStatement = new UseStatement(identifiers.getValue());
        return ParserResult.ok(useStatement, block.getRemaining());
    }
    // -----------------------------------------------------------------------------------------------------------------
}
