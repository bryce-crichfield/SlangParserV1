package parse.syntax;

import parse.TokenKind;

public record Expression(Expression expression, TokenKind operator, Term term) implements Node {
    public static Expression of(Expression expression, TokenKind operator, Term term) {
        return new Expression(expression, operator, term);
    }

    public static Expression of(Term term) {
        return new Expression(null, null, term);
    }
}

