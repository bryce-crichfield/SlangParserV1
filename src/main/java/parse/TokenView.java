package parse;

import java.util.List;
import java.util.Optional;

// Provides a view of a token stream, with a cursor
// This is a mutable object, and it is implemented
// to reduce the number of allocations
public class TokenView implements Cloneable {
    private final int start, end;
    private final List<Token> tokens;

    public TokenView(List<Token> tokens, int start, int end) {
        this.tokens = tokens;
        this.start = start;
        this.end = end;
    }

    public static TokenView of(List<Token> tokens) {
        return new TokenView(tokens, 0, tokens.size());
    }

    Optional<Token> peek(int lookahead) {
        if (lookahead + start >= end) {
            return Optional.empty();
        }

        return Optional.of(tokens.get(lookahead + start));
    }

    TokenView pop() {
        return new TokenView(tokens, start + 1, end);
    }

    TokenView take(int count) {
        return new TokenView(tokens, start + count, end);
    }

    @Override
    public TokenView clone() {
        return new TokenView(tokens, start, end);
    }
}
