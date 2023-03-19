package tokenizer;

public record Token(TokenKind kind, String symbol, int position) implements Cloneable {
    @Override
    public Token clone() {
        return new Token(kind, symbol, position);
    }

    public static Token of(TokenKind kind, String symbol, int position) {
        return new Token(kind, symbol, position);
    }
}
