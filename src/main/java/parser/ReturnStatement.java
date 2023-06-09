package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

import java.util.Optional;

public class ReturnStatement implements Node {
    // -----------------------------------------------------------------------------------------------------------------
    Optional<Expression> expression = Optional.empty();
    // -----------------------------------------------------------------------------------------------------------------

    public ReturnStatement(Optional<Expression> expression) {
        this.expression = expression;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        expression.ifPresent(e -> e.accept(visitor));
        visitor.exit(this);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<ReturnStatement> parse(View<Token> view) {
        var returnToken = Parse.token(view.clone(), TokenKind.RETURN);
        if (returnToken.isError()) {
            return ParserResult.error(view, returnToken.getMessage());
        }

        var expression = Parse.optional(returnToken.getRemaining(), Expression::parse);

        var semicolon = Parse.token(expression.getRemaining(), TokenKind.SEMICOLON);
        if (semicolon.isError()) {
            return ParserResult.error(view, semicolon.getMessage());
        }

        return ParserResult.ok(new ReturnStatement(expression.getValue()), semicolon.getRemaining());
    }
    // -----------------------------------------------------------------------------------------------------------------
}
