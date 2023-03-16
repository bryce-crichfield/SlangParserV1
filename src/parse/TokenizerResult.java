package parse;

public record TokenizerResult(boolean success, String message) {
    public static TokenizerResult ok() {
        return new TokenizerResult(true, "");
    }

    public static TokenizerResult error(String message) {
        return new TokenizerResult(false, message);
    }

    public Boolean isOk() {
        return success;
    }

    public Boolean isError() {
        return !success;
    }
}
