package util;

import java.util.Optional;

public record Either<A, B>(Optional<A> a, Optional<B> b) {
    public static <A, B> Either<A, B> left(A a) {
        return new Either<>(Optional.of(a), Optional.empty());
    }

    public static <A, B> Either<A, B> right(B b) {
        return new Either<>(Optional.empty(), Optional.of(b));
    }

    public static <A, B> Either<A, B> neither() {
        return new Either<>(Optional.empty(), Optional.empty());
    }

    public boolean isLeft() {
        return a.isPresent();
    }

    public boolean isRight() {
        return b.isPresent();
    }

    public boolean isNeither() {
        return !isLeft() && !isRight();
    }

    public A getLeft() {
        return a.get();
    }

    public B getRight() {
        return b.get();
    }
}
