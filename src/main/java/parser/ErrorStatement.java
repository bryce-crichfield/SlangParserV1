package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

public class ErrorStatement implements Node {
    // -----------------------------------------------------------------------------------------------------------------
    public Identifier identifier;
    // -----------------------------------------------------------------------------------------------------------------
    public ErrorStatement(Identifier identifier) {
        this.identifier = identifier;
    }
    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        identifier.accept(visitor);
        visitor.exit(this);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<ErrorStatement> parse(View<Token> view) {
        var errorToken = Parse.token(view.clone(), TokenKind.ERROR);
        if (errorToken.isError()) {
            var message = "Expected 'error' keyword but found " + errorToken.getRemaining().peek(0);
            return ParserResult.error(errorToken.getRemaining(), message);
        }

        var lParenToken = Parse.token(errorToken.getRemaining(), TokenKind.LPAREN);
        if (lParenToken.isError()) {
            var message = "Expected '(' but found " + lParenToken.getRemaining().peek(0);
            return ParserResult.error(lParenToken.getRemaining(), message);
        }

        var identifier = Identifier.parse(lParenToken.getRemaining());
        if (identifier.isError()) {
            var message = "Expected identifier but found " + identifier.getRemaining().peek(0);
            return ParserResult.error(identifier.getRemaining(), message);
        }

        var rParenToken = Parse.token(identifier.getRemaining(), TokenKind.RPAREN);
        if (rParenToken.isError()) {
            var message = "Expected ')' but found " + rParenToken.getRemaining().peek(0);
            return ParserResult.error(rParenToken.getRemaining(), message);
        }

        var errorStatement = new ErrorStatement(identifier.getValue());
        return ParserResult.ok(errorStatement, rParenToken.getRemaining());
    }
    // -----------------------------------------------------------------------------------------------------------------
}
