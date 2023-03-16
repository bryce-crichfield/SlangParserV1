package parse.syntax;

import parse.TokenKind;

public record Term(Term term, TokenKind operator, Factor factor) implements Node {
    public static Term of(Term term, TokenKind operator, Factor factor) {
        return new Term(term, operator, factor);
    }

    public static Term of(Factor factor) {
        return new Term(null, null, factor);
    }
}

