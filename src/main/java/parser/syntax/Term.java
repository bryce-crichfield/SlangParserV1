package parser.syntax;

import tokenizer.TokenKind;

public record Term(Term term, TokenKind operator, Factor factor) implements Node {
    public static Term of(Term term, TokenKind operator, Factor factor) {
        return new Term(term, operator, factor);
    }

    public static Term of(Factor factor) {
        return new Term(null, null, factor);
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        if (term != null) {
            term.accept(visitor);
        }
        if (factor != null) {
            factor.accept(visitor);
        }
        visitor.exit(this);
    }
}

