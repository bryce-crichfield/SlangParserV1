package tokenizer;

public enum TokenKind {
    // Keywords
    LET, FN, IF, ELSE,
    // Delimiters
    LPAREN, RPAREN, LBRACE, RBRACE,
    LBRACKET, RBRACKET,
    COMMA, SEMICOLON, DOT, COLON,

    // Operators
    PLUS, MINUS, STAR, SLASH,
    EQUALS,
    WHITESPACE,

    // Data
    IDENTIFIER, STRING, NUMBER, TRUE, FALSE, RETURN, FOR, WHILE, BREAK, CONTINUE, STRUCT, ENUM, IMPORT,
}
