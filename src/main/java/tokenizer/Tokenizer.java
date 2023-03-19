package tokenizer;

import util.View;

import java.util.List;
import java.util.Optional;

public class Tokenizer {
    // -----------------------------------------------------------------------------------------------------------------
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
    public static View<Token> tokenize(String input) {
        var tokenizer = new Tokenizer(input);
        var tokenizerResult = tokenizer.tokenize();
        if (tokenizerResult.isError()) {
            System.out.println(tokenizerResult.getMessage());
            return View.empty();
        }
        var tokens = tokenizerResult.getTokens();
        tokens = tokens.stream().filter(token -> token.kind() != TokenKind.WHITESPACE).toList();

        return View.of(tokens);
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
        var pairs = new java.util.HashMap<String, TokenKind>();
        // KeyWords
        pairs.put("let", TokenKind.LET);
        pairs.put("fn", TokenKind.FN);
        pairs.put("data", TokenKind.DATA);
        pairs.put("if", TokenKind.IF);
        pairs.put("else", TokenKind.ELSE);
        pairs.put("while", TokenKind.WHILE);
        pairs.put("for", TokenKind.FOR);
        pairs.put("until", TokenKind.UNTIL);
        pairs.put("to", TokenKind.TO);
        pairs.put("by", TokenKind.BY);
        pairs.put("return", TokenKind.RETURN);
        pairs.put("break", TokenKind.BREAK);
        pairs.put("continue", TokenKind.CONTINUE);
        pairs.put("error", TokenKind.ERROR);
        pairs.put("yield", TokenKind.YIELD);
        pairs.put("use", TokenKind.USE);
        // Literals
        pairs.put("true", TokenKind.TRUE);
        pairs.put("false", TokenKind.FALSE);
        pairs.put("null", TokenKind.NULL);
        // Two Symbol Operators
        pairs.put("==", TokenKind.EQ);
        pairs.put("!=", TokenKind.NEQ);
        pairs.put("<=", TokenKind.LTEQ);
        pairs.put(">=", TokenKind.GTEQ);
        pairs.put("**", TokenKind.POW);
        // One Symbol Operators
        pairs.put("=", TokenKind.ASSIGN);
        pairs.put("<", TokenKind.LT);
        pairs.put(">", TokenKind.GT);
        pairs.put("+", TokenKind.PLUS);
        pairs.put("-", TokenKind.MINUS);
        pairs.put("*", TokenKind.STAR);
        pairs.put("/", TokenKind.SLASH);
        pairs.put("%", TokenKind.PERCENT);
        pairs.put("&", TokenKind.AND);
        pairs.put("|", TokenKind.OR);
        // Delimiters
        pairs.put("(", TokenKind.LPAREN);
        pairs.put(")", TokenKind.RPAREN);
        pairs.put("{", TokenKind.LBRACE);
        pairs.put("}", TokenKind.RBRACE);
        pairs.put("[", TokenKind.LSQUARE);
        pairs.put("]", TokenKind.RSQUARE);
        pairs.put(";", TokenKind.SEMICOLON);
        pairs.put(":", TokenKind.COLON);
        pairs.put(",", TokenKind.COMMA);
        pairs.put(".", TokenKind.DOT);

        for (var pair : pairs.entrySet()) {
            var match = Match.keyword(pair.getKey(), pair.getValue(), input, index);
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

