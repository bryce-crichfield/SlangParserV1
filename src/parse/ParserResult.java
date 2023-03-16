package parse;

public record ParserResult<A>(boolean success, A value, TokenStream remaining, String message) {
    public static <A> ParserResult<A> ok(A value, TokenStream remaining) {
        return new ParserResult<>(true, value, remaining, "");
    }

    public static <A> ParserResult<A> error(TokenStream remaining, String message) {
        return new ParserResult<>(false, null, remaining, message);
    }

    public Boolean isOk() {
        return success;
    }

    public Boolean isError() {
        return !success;
    }
}
