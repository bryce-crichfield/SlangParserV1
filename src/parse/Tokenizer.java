package parse;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class Tokenizer {
    private char[] input;
    private List<Token> output;
    private int index;

    public Tokenizer(String input) {
        this.input = input.toCharArray();
        this.index = 0;
        this.output = new java.util.ArrayList<>();
    }

    public TokenStream getTokens() {
        return new TokenStream(output);
    }

    private Boolean shouldAccept(Optional<Token> token) {
        if (token.isEmpty()) {
            return false;
        }

        return true;
    }

    private void accept(Token token) {
        output.add(token);
        index += token.symbol().length();
    }

    public TokenizerResult tokenize() {
        while (index < input.length) {
            if (tokenizeKeywords()) {
                continue;
            }

            Optional<Token> match;

            match = matchIdentifier();
            if (shouldAccept(match)) {
                accept(match.get());
                continue;
            }

            match = matchNumber();
            if (shouldAccept(match)) {
                accept(match.get());
                continue;
            }

            match = matchWhitespace();
            if (shouldAccept(match)) {
                accept(match.get());
                continue;
            }

            return TokenizerResult.error("Unexpected character: " + input[index]);
        }

        return TokenizerResult.ok();
    }

    private Boolean tokenizeKeywords() {
        var pairs = new java.util.HashMap<String, TokenKind>();

        pairs.put("let", TokenKind.LET);
        pairs.put("fn", TokenKind.FN);
        pairs.put("if", TokenKind.IF);
        pairs.put("else", TokenKind.ELSE);
        pairs.put("true", TokenKind.TRUE);
        pairs.put("false", TokenKind.FALSE);
        pairs.put("return", TokenKind.RETURN);
        pairs.put("for", TokenKind.FOR);
        pairs.put("while", TokenKind.WHILE);
        pairs.put("break", TokenKind.BREAK);
        pairs.put("continue", TokenKind.CONTINUE);
        pairs.put("struct", TokenKind.STRUCT);
        pairs.put("enum", TokenKind.ENUM);
        pairs.put("import", TokenKind.IMPORT);

        pairs.put("+", TokenKind.PLUS);
        pairs.put("-", TokenKind.MINUS);
        pairs.put("*", TokenKind.STAR);
        pairs.put("/", TokenKind.SLASH);

        pairs.put("(", TokenKind.LPAREN);
        pairs.put(")", TokenKind.RPAREN);

        for (var pair: pairs.entrySet()) {
            var match = matchKeyword(pair.getKey(), pair.getValue());
            if (match.isPresent()) {
                accept(match.get());
                return true;
            }
        }

        return false;
    }

    private Optional<Character> matchCharacter(Predicate<Character> predicate, int lookahead) {
        Function<Character, Optional<Character>> mapper = c -> {
            if (predicate.test(c)) {
                return Optional.of(c);
            }

            return Optional.empty();
        };

        if (index + lookahead >= input.length) {
            return Optional.empty();
        }

        var peeked = Optional.of(input[index + lookahead]);

        return peeked.flatMap(mapper);
    }

    private Optional<Token> matchKeyword(String keyword, TokenKind kind) {
        if (keyword.length() > input.length - index) {
            return Optional.empty();
        }

        for (int i = 0; i < keyword.length(); i++) {
            if (keyword.charAt(i) != input[index + i]) {
                return Optional.empty();
            }
        }

        var token = Token.of(kind, keyword, index);
        return Optional.of(token);
    }

    private Optional<Token> matchIdentifier() {
        Predicate<Character> isHeader = (Character c) -> Character.isAlphabetic(c) || c == '_';
        Predicate<Character> isBody = (Character c) -> Character.isAlphabetic(c) || Character.isDigit(c) || c == '_';

        var character = matchCharacter(isHeader, 0);
        if (character.isEmpty()) {
            return Optional.empty();
        }

        StringBuilder builder = new StringBuilder();
        while (character.isPresent()) {
            builder.append(character.get());
            character = matchCharacter(isBody, builder.length());
        }

        // Construct and Return Token
        var token = Token.of(TokenKind.IDENTIFIER, builder.toString(), index);
        return Optional.of(token);
    }

    private Optional<Token> matchWhitespace() {
        Predicate<Character> isWhitespace = (Character c) -> Character.isWhitespace(c);

        var character = matchCharacter(isWhitespace, 0);
        if (character.isEmpty()) {
            return Optional.empty();
        }

        StringBuilder builder = new StringBuilder();
        while (character.isPresent()) {
            builder.append(character.get());
            character = matchCharacter(isWhitespace, builder.length());
        }

        var token = Token.of(TokenKind.WHITESPACE, builder.toString(), index);
        return Optional.of(token);
    }

    private Optional<Token> matchNumber() {
        Predicate<Character> isDigit = (Character c) -> Character.isDigit(c);

        Optional<Character> character = matchCharacter(isDigit, 0);
        if (character.isEmpty()) {
            return Optional.empty();
        }

        StringBuilder builder = new StringBuilder();
        while (character.isPresent()) {
            builder.append(character.get());
            character = matchCharacter(isDigit, builder.length());
        }

        character = matchCharacter(c -> c == '.', builder.length());
        if (character.isPresent()) {
            builder.append(character.get());

            character = matchCharacter(isDigit, builder.length());
            if (character.isEmpty()) {
                return Optional.empty();
            }

            while (character.isPresent()) {
                builder.append(character.get());
                character = matchCharacter(isDigit, builder.length());
            }
        }

        var token = Token.of(TokenKind.NUMBER, builder.toString(), index);
        return Optional.of(token);
    }
}

