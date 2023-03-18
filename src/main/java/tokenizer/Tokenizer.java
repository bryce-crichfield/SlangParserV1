package tokenizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Tokenizer {
    private final char[] input;
    private final List<Token> output;
    private int index;

    // -----------------------------------------------------------------------------------------------------------------
    public Tokenizer(String input) {
        this.input = input.toCharArray();
        this.index = 0;
        this.output = new java.util.ArrayList<>();
    }

    // -----------------------------------------------------------------------------------------------------------------
    public TokenizerResult tokenize() {
        while (index < input.length) {
            if (tokenizeKeywords()) {
                continue;
            }

            Optional<Token> match;

            match = Match.identifier(input, index);
            if (match.isPresent()) {
                output.add(match.get());
                index += match.get().symbol().length();
                continue;
            }

            match = Match.number(input, index);
            if (match.isPresent()) {
                output.add(match.get());
                index += match.get().symbol().length();
                continue;
            }

            match = Match.whitespace(input, index);
            if (match.isPresent()) {
                output.add(match.get());
                index += match.get().symbol().length();
                continue;
            }

            return TokenizerResult.error("Unexpected character: " + input[index]);
        }

        return TokenizerResult.ok(output);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private Boolean tokenizeKeywords() {
        HashMap<String, TokenKind> pairs = new java.util.HashMap<String, TokenKind>();

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
        pairs.put("util", TokenKind.DATA);

        pairs.put("+", TokenKind.PLUS);
        pairs.put("-", TokenKind.MINUS);
        pairs.put("*", TokenKind.STAR);
        pairs.put("/", TokenKind.SLASH);

        pairs.put("(", TokenKind.LPAREN);
        pairs.put(")", TokenKind.RPAREN);
        pairs.put("{", TokenKind.LBRACE);
        pairs.put("}", TokenKind.RBRACE);
        pairs.put(";", TokenKind.SEMICOLON);
        pairs.put(":", TokenKind.COLON);
        pairs.put(",", TokenKind.COMMA);

        pairs.put("=", TokenKind.EQUALS);
        for (Map.Entry<String, TokenKind> pair : pairs.entrySet()) {
            Optional<Token> match = Match.keyword(pair.getKey(), pair.getValue(), input, index);
            if (match.isPresent()) {
                output.add(match.get());
                index += match.get().symbol().length();
                return true;
            }
        }

        return false;
    }
    // -----------------------------------------------------------------------------------------------------------------
}

