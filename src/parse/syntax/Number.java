package parse.syntax;

public record Number(double value) implements Node {
    public static Number of(double value) {
        return new Number(value);
    }
}

