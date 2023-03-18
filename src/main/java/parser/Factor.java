package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

import java.util.Optional;

public class Factor implements Node {
    public Optional<Expression> expression = Optional.empty();
    public Optional<Accessor> accessor = Optional.empty();
    public Optional<Number> number = Optional.empty();

    Factor(Expression expression) {
        this.expression = Optional.of(expression);
    }

    Factor(Accessor accessor) {
        this.accessor = Optional.of(accessor);
    }

    Factor(Number number) {
        this.number = Optional.of(number);
    }

    public static ParserResult<Factor> parse(View<Token> view) {
        var number = Number.parse(view.clone());
        if (number.isOk()) {
            var factor = new Factor(number.getValue());
            return ParserResult.ok(factor, number.getRemaining());
        }

        var accessor = Accessor.parse(view.clone());
        if (accessor.isOk()) {
            var factor = new Factor(accessor.getValue());
            return ParserResult.ok(factor, accessor.getRemaining());
        }

        var leftParen = Parser.token(view.clone(), TokenKind.LPAREN);
        if (leftParen.isOk()) {
            var expression = Expression.parse(leftParen.getRemaining());
            if (expression.isOk()) {
                var rightParen = Parser.token(expression.getRemaining(), TokenKind.RPAREN);
                if (rightParen.isOk()) {
                    var factor = new Factor(expression.getValue());
                    return ParserResult.ok(factor, rightParen.getRemaining());
                }
            }
        }

        var message = String.format("Expected factor, got %s", view.peek(0));
        return ParserResult.error(view, message);
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        expression.ifPresent(e -> e.accept(visitor));
        accessor.ifPresent(a -> a.accept(visitor));
        number.ifPresent(n -> n.accept(visitor));
        visitor.exit(this);
    }
}

