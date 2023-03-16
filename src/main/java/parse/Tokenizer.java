package parse;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Tokenizer {
    TokenizerResult tokenize();
    // -----------------------------------------------------------------------------------------------------------------
    static Optional<Character> matchCharacter(Predicate<Character> predicate, char[] input, int start, int offset) {
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
    static Optional<Token> matchKeyword(String keyword, TokenKind kind, char[] input, int start) {
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
    static Optional<Token> matchIdentifier(char[] input, int start) {
        Predicate<Character> isHeader = (Character c) -> Character.isAlphabetic(c) || c == '_';
        Predicate<Character> isBody = (Character c) -> Character.isAlphabetic(c) || Character.isDigit(c) || c == '_';

        var character = matchCharacter(isHeader, input, start, 0);
        if (character.isEmpty()) {
            return Optional.empty();
        }

        StringBuilder builder = new StringBuilder();
        while (character.isPresent()) {
            builder.append(character.get());
            character = matchCharacter(isBody, input, start, builder.length());
        }

        // Construct and Return Token
        var token = Token.of(TokenKind.IDENTIFIER, builder.toString(), start);
        return Optional.of(token);
    }
    // -----------------------------------------------------------------------------------------------------------------
    static Optional<Token> matchWhitespace(char[] input, int start) {
        Predicate<Character> isWhitespace = Character::isWhitespace;

        var character = matchCharacter(isWhitespace, input, start, 0);
        if (character.isEmpty()) {
            return Optional.empty();
        }

        StringBuilder builder = new StringBuilder();
        while (character.isPresent()) {
            builder.append(character.get());
            character = matchCharacter(isWhitespace, input, start, builder.length());
        }

        var token = Token.of(TokenKind.WHITESPACE, builder.toString(), start);
        return Optional.of(token);
    }
    // -----------------------------------------------------------------------------------------------------------------
    static Optional<Token> matchNumber(char[] input, int start) {
        Predicate<Character> isDigit = Character::isDigit;

        Optional<Character> character = matchCharacter(isDigit, input, start, 0);
        if (character.isEmpty()) {
            return Optional.empty();
        }

        StringBuilder builder = new StringBuilder();
        while (character.isPresent()) {
            builder.append(character.get());
            character = matchCharacter(isDigit, input, start, builder.length());
        }

        character = matchCharacter(c -> c == '.', input, start, builder.length());
        if (character.isPresent()) {
            builder.append(character.get());

            character = matchCharacter(isDigit, input, start, builder.length());
            if (character.isEmpty()) {
                return Optional.empty();
            }

            while (character.isPresent()) {
                builder.append(character.get());
                character = matchCharacter(isDigit, input, start, builder.length());
            }
        }

        var token = Token.of(TokenKind.NUMBER, builder.toString(), start);
        return Optional.of(token);
    }
    // -----------------------------------------------------------------------------------------------------------------
}
