package tokenizer;

public enum TokenKind {
    // Keywords
    LET, FN, DATA,
    IF, ELSE,
    RETURN, CONTINUE, BREAK,
    WHILE,
    USE,
    ERROR, YIELD,
    FOR, UNTIL, TO, BY,


    // Delimiters
    LPAREN, RPAREN, LBRACE, RBRACE, LSQUARE, RSQUARE,
    COMMA, SEMICOLON, DOT, COLON,

    // Binary Operators
    PLUS, MINUS, STAR, SLASH, PERCENT, POW, AND, OR,
    EQ, NEQ, LT, GT, LTEQ, GTEQ,
    ASSIGN,
    // Unary Operators
    NOT, NEG,
    // Data Literals
    IDENTIFIER, STRING, NUMBER, TRUE, FALSE, NULL,
    // MISC.
    WHITESPACE,
}
