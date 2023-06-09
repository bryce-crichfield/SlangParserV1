package util;

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

    public Optional<A> peek(int lookahead) {
        if (lookahead + start >= end) {
            return Optional.empty();
        }

        return Optional.of(data.get(lookahead + start));
    }

    public boolean isEmpty() {
        return start == end;
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

    public void forEach(java.util.function.Consumer<A> consumer) {
        for (var i = start; i < end; i++) {
            consumer.accept(data.get(i));
        }
    }

    public static <A> View<A> of(List<A> tokens) {
        return new View<>(tokens, 0, tokens.size());
    }

    public static <A> View<A> empty() {
        return new View<>(List.of(), 0, 0);
    }
}
