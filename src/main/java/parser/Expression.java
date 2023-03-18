package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

import java.util.Optional;

public class Expression implements Node {
    public Optional<Expression> expression = Optional.empty();
    public Optional<TokenKind> operator = Optional.empty();
    public Optional<Term> term = Optional.empty();
    public Optional<FunctionCall> functionCall = Optional.empty();

    Expression(FunctionCall functionCall) {
        this.functionCall = Optional.of(functionCall);
    }

    public Expression(Expression expression, TokenKind operator, Term term) {
        this.expression = Optional.of(expression);
        this.operator = Optional.of(operator);
        this.term = Optional.of(term);
    }

    public Expression(Term term) {
        this.term = Optional.of(term);
    }

    public static ParserResult<Expression> parse(View<Token> view) {
        var functionCall = FunctionCall.parse(view.clone());
        if (functionCall.isOk()) {
            var expression = new Expression(functionCall.getValue());
            return ParserResult.ok(expression, functionCall.getRemaining());
        }

        var term = Term.parse(view.clone());
        if (term.isError()) {
            return ParserResult.error(view, term.getMessage());
        }

        var plus = Parse.token(term.getRemaining(), TokenKind.PLUS);
        if (plus.isOk()) {
            var expression = Expression.parse(plus.getRemaining());
            if (expression.isOk()) {
                var resultExpr = new Expression(expression.getValue(), TokenKind.PLUS, term.getValue());
                return ParserResult.ok(resultExpr, expression.getRemaining());
            }
        }

        var minus = Parse.token(term.getRemaining(), TokenKind.MINUS);
        if (minus.isOk()) {
            var expression = Expression.parse(minus.getRemaining());
            if (expression.isOk()) {
                var resultExpr = new Expression(expression.getValue(), TokenKind.MINUS, term.getValue());
                return ParserResult.ok(resultExpr, expression.getRemaining());
            }
        }

        var expression = new Expression(term.getValue());
        return ParserResult.ok(expression, term.getRemaining());
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        expression.ifPresent(e -> e.accept(visitor));
        term.ifPresent(t -> t.accept(visitor));
        functionCall.ifPresent(f -> f.accept(visitor));
        visitor.exit(this);
    }
}

