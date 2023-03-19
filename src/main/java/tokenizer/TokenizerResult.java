package tokenizer;

import java.util.List;

public class TokenizerResult {
    // -----------------------------------------------------------------------------------------------------------------
    private final boolean isSuccessful;
    private final List<Token> tokens;
    private final String message;
    // -----------------------------------------------------------------------------------------------------------------

    private TokenizerResult(boolean isSuccessful, List<Token> tokens, String message) {
        this.isSuccessful = isSuccessful;
        this.tokens = tokens;
        this.message = message;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public Boolean isOk() {
        return isSuccessful;
    }

    public Boolean isError() {
        return !isSuccessful;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public String getMessage() {
        return message;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static TokenizerResult ok(List<Token> tokens) {
        return new TokenizerResult(true, tokens, "");
    }

    public static TokenizerResult error(String message) {
        return new TokenizerResult(false, null, message);
    }
    // -----------------------------------------------------------------------------------------------------------------

}
