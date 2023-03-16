package data;

import java.util.List;
import java.util.Optional;

/**
 * Represents an immutable section of some underlying List.
 * Works from a start index to an end index within the underlying List.
 */
public class View<A> {
    private final int start, end;
    private final List<A> data;

    public View(List<A> data, int start, int end) {
        this.data = data;
        this.start = start;
        this.end = end;
    }
    
    public static <A> View<A> of(List<A> tokens) {
        return new View<>(tokens, 0, tokens.size());
    }

    public Optional<A> peek(int lookahead) {
        if (lookahead + start >= end) {
            return Optional.empty();
        }

        return Optional.of(data.get(lookahead + start));
    }

    public View<A> pop() {
        return new View<>(data, start + 1, end);
    }

    public View<A> take(int count) {
        return new View<>(data, start + count, end);
    }

    @Override
    public View<A> clone() {
        return new View<>(data, start, end);
    }
}
