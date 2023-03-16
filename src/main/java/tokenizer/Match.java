package tokenizer;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

interface Match {
    // -----------------------------------------------------------------------------------------------------------------
    static Optional<Character> character(Predicate<Character> predicate, final char[] input, int start, int offset) {
        Function<Character, Optional<Character>> mapper = c -> {
            if (predicate.test(c)) {
                return Optional.of(c);
            }

            return Optional.empty();
        };

        if (start + offset >= input.length) {
            return Optional.empty();
        }

        var peeked = Optional.of(input[start + offset]);

        return peeked.flatMap(mapper);
    }
    // -----------------------------------------------------------------------------------------------------------------
    static Optional<Token> keyword(String keyword, TokenKind kind, final char[] input, int start) {
        if (keyword.length() > input.length - start) {
            return Optional.empty();
        }

        for (int i = 0; i < keyword.length(); i++) {
            if (keyword.charAt(i) != input[start + i]) {
                return Optional.empty();
            }
        }

        var token = Token.of(kind, keyword, start);
        return Optional.of(token);
    }
    // -----------------------------------------------------------------------------------------------------------------
    static Optional<Token> identifier(final char[] input, int start) {
        Predicate<Character> isHeader = (Character c) -> Character.isAlphabetic(c) || c == '_';
        Predicate<Character> isBody = (Character c) -> Character.isAlphabetic(c) || Character.isDigit(c) || c == '_';

        var character = Match.character(isHeader, input, start, 0);
        if (character.isEmpty()) {
            return Optional.empty();
        }

        StringBuilder builder = new StringBuilder();
        while (character.isPresent()) {
            builder.append(character.get());
            character = Match.character(isBody, input, start, builder.length());
        }

        // Construct and Return Token
        var token = Token.of(TokenKind.IDENTIFIER, builder.toString(), start);
        return Optional.of(token);
    }
    // -----------------------------------------------------------------------------------------------------------------
    static Optional<Token> whitespace(final char[] input, int start) {
        Predicate<Character> isWhitespace = Character::isWhitespace;

        var character = Match.character(isWhitespace, input, start, 0);
        if (character.isEmpty()) {
            return Optional.empty();
        }

        StringBuilder builder = new StringBuilder();
        while (character.isPresent()) {
            builder.append(character.get());
            character = Match.character(isWhitespace, input, start, builder.length());
        }

        var token = Token.of(TokenKind.WHITESPACE, builder.toString(), start);
        return Optional.of(token);
    }
    // -----------------------------------------------------------------------------------------------------------------
    static Optional<Token> number(final char[] input, int start) {
        Predicate<Character> isDigit = Character::isDigit;

        Optional<Character> character = Match.character(isDigit, input, start, 0);
        if (character.isEmpty()) {
            return Optional.empty();
        }

        StringBuilder builder = new StringBuilder();
        while (character.isPresent()) {
            builder.append(character.get());
            character = Match.character(isDigit, input, start, builder.length());
        }

        character = Match.character(c -> c == '.', input, start, builder.length());
        if (character.isPresent()) {
            builder.append(character.get());

            character = Match.character(isDigit, input, start, builder.length());
            if (character.isEmpty()) {
                return Optional.empty();
            }

            while (character.isPresent()) {
                builder.append(character.get());
                character = Match.character(isDigit, input, start, builder.length());
            }
        }

        var token = Token.of(TokenKind.NUMBER, builder.toString(), start);
        return Optional.of(token);
    }
    // -----------------------------------------------------------------------------------------------------------------
}
