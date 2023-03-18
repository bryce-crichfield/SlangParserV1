package tokenizer;

public enum TokenKind {
    // Keywords
    LET, FN, IF, ELSE, DATA, RETURN, CONTINUE, BREAK,
    WHILE,
    // For Loop
    FOR, UNTIL, TO, BY,
    // Delimiters
    LPAREN, RPAREN, LBRACE, RBRACE,
    LBRACKET, RBRACKET,
    COMMA, SEMICOLON, DOT, COLON,

    // Operators
    PLUS, MINUS, STAR, SLASH,
    EQUALS,
    WHITESPACE,

    // Data TYPES
    IDENTIFIER, STRING, NUMBER, TRUE, FALSE,
}
