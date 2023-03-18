package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

import java.util.Optional;

public class Term implements Node {
    public Optional<Term> term = Optional.empty();
    public Optional<TokenKind> operator = Optional.empty();
    public Optional<Factor> factor = Optional.empty();

    Term(Term term, TokenKind operator, Factor factor) {
        this.term = Optional.of(term);
        this.operator = Optional.of(operator);
        this.factor = Optional.of(factor);
    }

    Term(Factor factor) {
        this.factor = Optional.of(factor);
    }

    public static ParserResult<Term> parse(View<Token> view) {
        var factor = Factor.parse(view.clone());
        if (factor.isError()) {
            return ParserResult.error(view, factor.getMessage());
        }

        var star = Parse.token(factor.getRemaining(), TokenKind.STAR);
        if (star.isOk()) {
            var term = Term.parse(star.getRemaining());
            if (term.isOk()) {
                var resultTerm = new Term(term.getValue(), TokenKind.STAR, factor.getValue());
                return ParserResult.ok(resultTerm, term.getRemaining());
            }
        }

        var slash = Parse.token(factor.getRemaining(), TokenKind.SLASH);
        if (slash.isOk()) {
            var term = Term.parse(slash.getRemaining());
            if (term.isOk()) {
                var resultTerm = new Term(term.getValue(), TokenKind.SLASH, factor.getValue());
                return ParserResult.ok(resultTerm, term.getRemaining());
            }
        }

        var resultTerm = new Term(factor.getValue());
        return ParserResult.ok(resultTerm, factor.getRemaining());
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        term.ifPresent(t -> t.accept(visitor));
        // TODO: add visitor for operator
        factor.ifPresent(f -> f.accept(visitor));
        visitor.exit(this);
    }
}

