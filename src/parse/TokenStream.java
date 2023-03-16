package parse;

import java.util.List;
import java.util.Optional;

// Basically just an immutable list of tokens, with copy, push/pop methods
public class TokenStream implements Cloneable {
    private List<Token> tokens;

    public TokenStream(List<Token> tokens) {
        this.tokens = new java.util.ArrayList<>();

        for (Token token : tokens) {
            this.tokens.add(token.clone());
        }
    }

    @Override
    public TokenStream clone() {
        return new TokenStream(tokens);
    }

    Optional<Token> peek(int lookahead) {
        if (lookahead >= tokens.size()) {
            return Optional.empty();
        }

        return Optional.of(tokens.get(lookahead));
    }

    TokenStream pop(int count) {
        List<Token> remaining = new java.util.ArrayList<>();

        for (int i = count; i < tokens.size(); i++) {
            remaining.add(tokens.get(i));
        }

        return new TokenStream(remaining);
    }

    public TokenStream filter(TokenKind kind) {
        List<Token> remaining = new java.util.ArrayList<>();

        for (Token token : tokens) {
            if (token.kind() != kind) {
                remaining.add(token);
            }
        }

        return new TokenStream(remaining);
    }

    public void dump() {
        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}
