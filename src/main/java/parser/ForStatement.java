package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

import java.util.Optional;

public class ForStatement implements Node {
    Identifier indexer;
    Expression start;
    Expression end;
    Optional<Expression> step;
    Block block;

    ForStatement(Identifier indexer, Expression start, Expression end, Optional<Expression> step, Block block) {
        this.indexer = indexer;
        this.start = start;
        this.end = end;
        this.step = step;
        this.block = block;
    }

    public static ParserResult<ForStatement> parse(View<Token> view) {
        var forToken = Parse.token(view.clone(), TokenKind.FOR);
        if (forToken.isError()) {
            return ParserResult.error(view, forToken.getMessage());
        }

        var lParen = Parse.token(forToken.getRemaining(), TokenKind.LPAREN);
        if (lParen.isError()) {
            return ParserResult.error(view, lParen.getMessage());
        }

        var indexer = Identifier.parse(lParen.getRemaining());
        if (indexer.isError()) {
            return ParserResult.error(view, indexer.getMessage());
        }

        var colon = Parse.token(indexer.getRemaining(), TokenKind.COLON);
        if (colon.isError()) {
            return ParserResult.error(view, colon.getMessage());
        }

        var start = Expression.parse(colon.getRemaining());
        if (start.isError()) {
            return ParserResult.error(view, start.getMessage());
        }

        var condition = parseCondition(start.getRemaining());
        if (condition.isError()) {
            return ParserResult.error(view, condition.getMessage());
        }

        var end = Expression.parse(condition.getRemaining());
        if (end.isError()) {
            return ParserResult.error(view, end.getMessage());
        }

        var step = Parse.optional(end.getRemaining(), Expression::parse);

        var rParen = Parse.token(step.getRemaining(), TokenKind.RPAREN);
        if (rParen.isError()) {
            return ParserResult.error(view, rParen.getMessage());
        }

        var body = Block.parse(rParen.getRemaining());
        if (body.isError()) {
            return ParserResult.error(view, body.getMessage());
        }

        var result = new ForStatement(indexer.getValue(), start.getValue(), end.getValue(), step.getValue(),
                                      body.getValue()
        );
        return ParserResult.ok(result, body.getRemaining());
    }

    private static ParserResult<TokenKind> parseCondition(View<Token> token) {
        var until = Parse.token(token.clone(), TokenKind.UNTIL);
        if (until.isOk()) {
            return ParserResult.ok(TokenKind.UNTIL, until.getRemaining());
        }

        var to = Parse.token(token.clone(), TokenKind.TO);
        if (to.isOk()) {
            return ParserResult.ok(TokenKind.TO, to.getRemaining());
        }

        return ParserResult.error(token, "Expected 'until' or 'to'");
    }

    private static ParserResult<Expression> parseStep(View<Token> token) {
        var by = Parse.token(token.clone(), TokenKind.BY);
        if (by.isError()) {
            return ParserResult.error(token, by.getMessage());
        }

        var step = Expression.parse(by.getRemaining());
        if (step.isError()) {
            return ParserResult.error(token, step.getMessage());
        }

        return ParserResult.ok(step.getValue(), step.getRemaining());
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        indexer.accept(visitor);
        start.accept(visitor);
        end.accept(visitor);
        step.ifPresent(s -> s.accept(visitor));
        block.accept(visitor);
        visitor.exit(this);
    }
}
