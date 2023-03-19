package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

import java.util.Optional;

public class Factor implements Node {
    // -----------------------------------------------------------------------------------------------------------------
    public Optional<Expression> expression = Optional.empty();
    public Optional<CompositeIdentifier> compositeIdentifier = Optional.empty();
    public Optional<Number> number = Optional.empty();

    // -----------------------------------------------------------------------------------------------------------------
    public Factor(Expression expression) {
        this.expression = Optional.of(expression);
    }

    public Factor(CompositeIdentifier compositeIdentifier) {
        this.compositeIdentifier = Optional.of(compositeIdentifier);
    }

    public Factor(Number number) {
        this.number = Optional.of(number);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        expression.ifPresent(e -> e.accept(visitor));
        compositeIdentifier.ifPresent(i -> i.accept(visitor));
        number.ifPresent(n -> n.accept(visitor));
        visitor.exit(this);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Factor> parse(View<Token> view) {
        var number = Number.parse(view.clone());
        if (number.isOk()) {
            var factor = new Factor(number.getValue());
            return ParserResult.ok(factor, number.getRemaining());
        }

        var compositeIdentifier = CompositeIdentifier.parse(view.clone());
        if (compositeIdentifier.isOk()) {
            var factor = new Factor(compositeIdentifier.getValue());
            return ParserResult.ok(factor, compositeIdentifier.getRemaining());
        }

        var leftParen = Parse.token(view.clone(), TokenKind.LPAREN);
        if (leftParen.isOk()) {
            var expression = Expression.parse(leftParen.getRemaining());
            if (expression.isOk()) {
                var rightParen = Parse.token(expression.getRemaining(), TokenKind.RPAREN);
                if (rightParen.isOk()) {
                    var factor = new Factor(expression.getValue());
                    return ParserResult.ok(factor, rightParen.getRemaining());
                }
            }
        }

        var message = String.format("Expected factor, got %s", view.peek(0));
        return ParserResult.error(view, message);
    }
    // -----------------------------------------------------------------------------------------------------------------
}

