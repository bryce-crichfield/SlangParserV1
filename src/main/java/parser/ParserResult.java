package parser;

import tokenizer.Token;
import util.View;

public class ParserResult<A> {
    private final boolean isSuccessful;
    private final A value;
    private final View<Token> remaining;
    private final String message;

    private ParserResult(boolean isSuccessful, A value, View<Token> remaining, String message) {
        this.isSuccessful = isSuccessful;
        this.value = value;
        this.remaining = remaining;
        this.message = message;
    }

    public static <A> ParserResult<A> ok(A value, View<Token> remaining) {
        return new ParserResult<>(true, value, remaining, "");
    }

    public static <A> ParserResult<A> error(View<Token> remaining, String message) {
        return new ParserResult<>(false, null, remaining, message);
    }

    public A getValue() {
        return value;
    }

    public View<Token> getRemaining() {
        return remaining.clone();
    }

    public String getMessage() {
        return message;
    }

    public Boolean isOk() {
        return isSuccessful;
    }

    public Boolean isError() {
        return !isSuccessful;
    }
}
