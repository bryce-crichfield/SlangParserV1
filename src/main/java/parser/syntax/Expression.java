package parser.syntax;

import tokenizer.TokenKind;

public record Expression(Expression expression, TokenKind operator, Term term,
                         Application application) implements Node {
    public static Expression of(Expression expression, TokenKind operator, Term term) {
        return new Expression(expression, operator, term, null);
    }

    public static Expression of(Term term) {
        return new Expression(null, null, term, null);
    }

    public static Expression of(Application application) {
        return new Expression(null, null, null, application);
    }
}

