package parse;

public class ParserResult<A> {
    private final boolean isSuccessful;
    private final A value;
    private final TokenView remaining;
    private final String message;

    private ParserResult(boolean isSuccessful, A value, TokenView remaining, String message) {
        this.isSuccessful = isSuccessful;
        this.value = value;
        this.remaining = remaining;
        this.message = message;
    }

    public static <A> ParserResult<A> ok(A value, TokenView remaining) {
        return new ParserResult<>(true, value, remaining, "");
    }

    public static <A> ParserResult<A> error(TokenView remaining, String message) {
        return new ParserResult<>(false, null, remaining, message);
    }

    public A getValue() {
        return value;
    }

    public TokenView getRemaining() {
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
