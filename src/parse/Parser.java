package parse;

import parse.syntax.Number;
import parse.syntax.*;

public class Parser {
    public static ParserResult<Token> parseToken(TokenStream stream, TokenKind kind) {
        var token = stream.peek(0);
        if (token.isEmpty()) {
            return ParserResult.error(stream, "Unexpected end of input");
        }
        if (token.get().kind() != kind) {
            return ParserResult.error(stream, "Expected " + kind + " but got " + token.get().kind());
        }
        return ParserResult.ok(token.get(), stream.pop(1));
    }

    public static ParserResult<Number> parseNumber(TokenStream stream) {
        var token = parseToken(stream.clone(), TokenKind.NUMBER);
        if (token.isError()) {
            return ParserResult.error(stream, token.message());
        }
        var number = Number.of(Double.parseDouble(token.value().symbol()));
        return ParserResult.ok(number, token.remaining());
    }

    public static ParserResult<Identifier> parseIdentifier(TokenStream stream) {
        var token = parseToken(stream.clone(), TokenKind.IDENTIFIER);
        if (token.isError()) {
            return ParserResult.error(stream, token.message());
        }
        var identifier = Identifier.of(token.value().symbol());
        return ParserResult.ok(identifier, token.remaining());
    }

    public static ParserResult<Factor> parseFactor(TokenStream tokens) {
        // F := '(' E ')'
        //    | NUMBER
        //    | IDENTIFIER

        var number = parseNumber(tokens.clone());
        if (number.isOk()) {
            return ParserResult.ok(Factor.of(number.value()), number.remaining());
        }

        var identifier = parseIdentifier(tokens.clone());
        if (identifier.isOk()) {
            return ParserResult.ok(Factor.of(identifier.value()), identifier.remaining());
        }

        var leftParen = parseToken(tokens.clone(), TokenKind.LPAREN);
        if (leftParen.isOk()) {
            var expression = parseExpression(leftParen.remaining());
            if (expression.isOk()) {
                var rightParen = parseToken(expression.remaining(), TokenKind.RPAREN);
                if (rightParen.isOk()) {
                    return ParserResult.ok(Factor.of(expression.value()), rightParen.remaining());
                }
            }
        }

        return ParserResult.error(tokens, "Expected factor or Number or Identifier");
    }

    public static ParserResult<Term> parseTerm(TokenStream tokens) {
        // T := T '*' F
        //    | T '/' F
        //    | F

        var factor = parseFactor(tokens.clone());
        if (factor.isError()) {
            return ParserResult.error(tokens, factor.message());
        }

        var star = parseToken(factor.remaining().clone(), TokenKind.STAR);
        if (star.isOk()) {
            var term = parseTerm(star.remaining());
            if (term.isOk()) {
                return ParserResult.ok(Term.of(term.value(), TokenKind.STAR, factor.value()), term.remaining());
            }
        }

        var slash = parseToken(factor.remaining().clone(), TokenKind.SLASH);
        if (slash.isOk()) {
            var term = parseTerm(slash.remaining());
            if (term.isOk()) {
                return ParserResult.ok(Term.of(term.value(), TokenKind.SLASH, factor.value()), term.remaining());
            }
        }

        return ParserResult.ok(Term.of(factor.value()), factor.remaining());
    }

    public static ParserResult<Expression> parseExpression(TokenStream tokens) {
        // E := E '+' T
        //    | E '-' T
        //    | T

        var term = parseTerm(tokens.clone());
        if (term.isError()) {
            return ParserResult.error(tokens, term.message());
        }

        var plus = parseToken(term.remaining().clone(), TokenKind.PLUS);
        if (plus.isOk()) {
            var expression = parseExpression(plus.remaining());
            if (expression.isOk()) {
                return ParserResult.ok(Expression.of(expression.value(), TokenKind.PLUS, term.value()), expression.remaining());
            }
        }

        var minus = parseToken(term.remaining().clone(), TokenKind.MINUS);
        if (minus.isOk()) {
            var expression = parseExpression(minus.remaining());
            if (expression.isOk()) {
                return ParserResult.ok(Expression.of(expression.value(), TokenKind.MINUS, term.value()), expression.remaining());
            }
        }

        return ParserResult.ok(Expression.of(term.value()), term.remaining());
    }
}
