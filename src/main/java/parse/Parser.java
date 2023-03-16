package parse;

import parse.syntax.Number;
import parse.syntax.*;

public class Parser {
    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Token> parseToken(View<Token> view, TokenKind kind) {
        var token = view.peek(0);
        if (token.isEmpty()) {
            return ParserResult.error(view, "Unexpected end of input");
        }
        if (token.get().kind() != kind) {
            return ParserResult.error(view, "Expected " + kind + " but got " + token.get().kind());
        }
        return ParserResult.ok(token.get(), view.pop());
    }
    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Number> parseNumber(View<Token> view) {
        var token = parseToken(view.clone(), TokenKind.NUMBER);
        if (token.isError()) {
            return ParserResult.error(view, token.getMessage());
        }
        var number = Number.of(Double.parseDouble(token.getValue().symbol()));
        return ParserResult.ok(number, token.getRemaining());
    }
    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Identifier> parseIdentifier(View<Token> view) {
        var token = parseToken(view.clone(), TokenKind.IDENTIFIER);
        if (token.isError()) {
            return ParserResult.error(view, token.getMessage());
        }
        var identifier = Identifier.of(token.getValue().symbol());
        return ParserResult.ok(identifier, token.getRemaining());
    }
    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Factor> parseFactor(View<Token> view) {
        // F := '(' E ')'
        //    | NUMBER
        //    | IDENTIFIER

        var number = parseNumber(view.clone());
        if (number.isOk()) {
            var factor = Factor.of(number.getValue());
            return ParserResult.ok(factor, number.getRemaining());
        }

        var identifier = parseIdentifier(view.clone());
        if (identifier.isOk()) {
            var factor = Factor.of(identifier.getValue());
            return ParserResult.ok(factor, identifier.getRemaining());
        }

        var leftParen = parseToken(view.clone(), TokenKind.LPAREN);
        if (leftParen.isOk()) {
            var expression = parseExpression(leftParen.getRemaining());
            if (expression.isOk()) {
                var rightParen = parseToken(expression.getRemaining(), TokenKind.RPAREN);
                if (rightParen.isOk()) {
                    var factor = Factor.of(expression.getValue());
                    return ParserResult.ok(factor, rightParen.getRemaining());
                }
            }
        }

        return ParserResult.error(view, "Expected factor or Number or Identifier");
    }
    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Term> parseTerm(View<Token> view) {
        // T := T '*' F
        //    | T '/' F
        //    | F

        var factor = parseFactor(view.clone());
        if (factor.isError()) {
            return ParserResult.error(view, factor.getMessage());
        }

        var star = parseToken(factor.getRemaining(), TokenKind.STAR);
        if (star.isOk()) {
            var term = parseTerm(star.getRemaining());
            if (term.isOk()) {
                var resultTerm = Term.of(term.getValue(), TokenKind.STAR, factor.getValue());
                return ParserResult.ok(resultTerm, term.getRemaining());
            }
        }

        var slash = parseToken(factor.getRemaining(), TokenKind.SLASH);
        if (slash.isOk()) {
            var term = parseTerm(slash.getRemaining());
            if (term.isOk()) {
                var resultTerm = Term.of(term.getValue(), TokenKind.SLASH, factor.getValue());
                return ParserResult.ok(resultTerm, term.getRemaining());
            }
        }

        return ParserResult.ok(Term.of(factor.getValue()), factor.getRemaining());
    }
    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Expression> parseExpression(View<Token> view) {
        // E := E '+' T
        //    | E '-' T
        //    | T

        var term = parseTerm(view.clone());
        if (term.isError()) {
            return ParserResult.error(view, term.getMessage());
        }

        var plus = parseToken(term.getRemaining(), TokenKind.PLUS);
        if (plus.isOk()) {
            var expression = parseExpression(plus.getRemaining());
            if (expression.isOk()) {
                var resultExpr = Expression.of(expression.getValue(), TokenKind.PLUS, term.getValue());
                return ParserResult.ok(resultExpr, expression.getRemaining());
            }
        }

        var minus = parseToken(term.getRemaining(), TokenKind.MINUS);
        if (minus.isOk()) {
            var expression = parseExpression(minus.getRemaining());
            if (expression.isOk()) {
                var resultExpr = Expression.of(expression.getValue(), TokenKind.MINUS, term.getValue());
                return ParserResult.ok(resultExpr, expression.getRemaining());
            }
        }

        return ParserResult.ok(Expression.of(term.getValue()), term.getRemaining());
    }
    // -----------------------------------------------------------------------------------------------------------------
}
