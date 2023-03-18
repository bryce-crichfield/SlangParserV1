package parser;

import data.Tuple;
import data.View;
import parser.syntax.Number;
import parser.syntax.*;
import tokenizer.Token;
import tokenizer.TokenKind;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Parser {
    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Token> parseToken(View<Token> view, TokenKind kind) {
        Optional<Token> token = view.peek(0);
        if (token.isEmpty()) {
            return ParserResult.error(view, "Unexpected end of input");
        }
        if (token.get().kind() != kind) {
            return ParserResult.error(view, "Expected " + kind + " but got " + token.get().kind());
        }
        return ParserResult.ok(token.get(), view.pop());
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static <A> ParserResult<List<A>> parseZeroOrMoreSeparatedBy(View<Token> view,
            Function<View<Token>, ParserResult<A>> parser,
            Optional<TokenKind> separator
    ) {
        ArrayList<A> accumulator = new ArrayList<>();
        View<Token> remaining = view.clone();

        while (true) {
            ParserResult<A> result = parser.apply(remaining);
            if (result.isError()) {
                return ParserResult.ok(accumulator, remaining);
            }
            accumulator.add(result.getValue());
            remaining = result.getRemaining();

            if (separator.isPresent()) {
                ParserResult<Token> separatorResult = parseToken(remaining, separator.get());
                if (separatorResult.isError()) {
                    return ParserResult.ok(accumulator, remaining);
                }
                remaining = separatorResult.getRemaining();
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static <A> ParserResult<List<A>> parseZeroOrMore(View<Token> view,
            Function<View<Token>, ParserResult<A>> parser
    ) {
        return parseZeroOrMoreSeparatedBy(view, parser, Optional.empty());
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static <A> ParserResult<List<A>> parseOneOrMoreSeparatedBy(View<Token> view,
            Function<View<Token>, ParserResult<A>> parser,
            Optional<TokenKind> separator
    ) {
        ArrayList<A> accumulator = new ArrayList<>();
        View<Token> remaining = view.clone();

        ParserResult<A> result = parser.apply(remaining);
        if (result.isError()) {
            return ParserResult.error(view, result.getMessage());
        }

        accumulator.add(result.getValue());

        while (separator.isPresent()) {
            ParserResult<Token> separatorResult = parseToken(result.getRemaining(), separator.get());
            if (separatorResult.isError()) {
                return ParserResult.ok(accumulator, result.getRemaining());
            }

            result = parser.apply(separatorResult.getRemaining());
            if (result.isError()) {
                return ParserResult.error(view, result.getMessage());
            }

            accumulator.add(result.getValue());
        }

        return ParserResult.ok(accumulator, result.getRemaining());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static <A> ParserResult<List<A>> parseOneOrMore(View<Token> view,
            Function<View<Token>, ParserResult<A>> parser
    ) {
        return parseOneOrMoreSeparatedBy(view, parser, Optional.empty());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Number> parseNumber(View<Token> view) {
        ParserResult<Token> token = parseToken(view.clone(), TokenKind.NUMBER);
        if (token.isError()) {
            return ParserResult.error(view, token.getMessage());
        }
        Number number = Number.of(Double.parseDouble(token.getValue().symbol()));
        return ParserResult.ok(number, token.getRemaining());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Identifier> parseIdentifier(View<Token> view) {
        ParserResult<Token> token = parseToken(view.clone(), TokenKind.IDENTIFIER);
        if (token.isError()) {
            return ParserResult.error(view, token.getMessage());
        }
        Identifier identifier = Identifier.of(token.getValue().symbol());
        return ParserResult.ok(identifier, token.getRemaining());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Tuple<Identifier, Identifier>> parseTypedIdentifier(View<Token> token) {
        ParserResult<Identifier> identifier = parseIdentifier(token.clone());
        if (identifier.isError()) {
            return ParserResult.error(token, identifier.getMessage());
        }

        ParserResult<Token> colon = parseToken(identifier.getRemaining(), TokenKind.COLON);
        if (colon.isError()) {
            return ParserResult.error(token, colon.getMessage());
        }

        ParserResult<Identifier> type = parseIdentifier(colon.getRemaining());
        if (type.isError()) {
            return ParserResult.error(token, type.getMessage());
        }

        Tuple<Identifier, Identifier> typedIdentifier = Tuple.of(identifier.getValue(), type.getValue());
        return ParserResult.ok(typedIdentifier, type.getRemaining());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Factor> parseFactor(View<Token> view) {
        ParserResult<Number> number = parseNumber(view.clone());
        if (number.isOk()) {
            Factor factor = Factor.of(number.getValue());
            return ParserResult.ok(factor, number.getRemaining());
        }

        ParserResult<Identifier> identifier = parseIdentifier(view.clone());
        if (identifier.isOk()) {
            Factor factor = Factor.of(identifier.getValue());
            return ParserResult.ok(factor, identifier.getRemaining());
        }

        ParserResult<Token> leftParen = parseToken(view.clone(), TokenKind.LPAREN);
        if (leftParen.isOk()) {
            ParserResult<Expression> expression = parseExpression(leftParen.getRemaining());
            if (expression.isOk()) {
                ParserResult<Token> rightParen = parseToken(expression.getRemaining(), TokenKind.RPAREN);
                if (rightParen.isOk()) {
                    Factor factor = Factor.of(expression.getValue());
                    return ParserResult.ok(factor, rightParen.getRemaining());
                }
            }
        }

        return ParserResult.error(view, "Expected factor or Number or Identifier");
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Term> parseTerm(View<Token> view) {
        ParserResult<Factor> factor = parseFactor(view.clone());
        if (factor.isError()) {
            return ParserResult.error(view, factor.getMessage());
        }

        ParserResult<Token> star = parseToken(factor.getRemaining(), TokenKind.STAR);
        if (star.isOk()) {
            ParserResult<Term> term = parseTerm(star.getRemaining());
            if (term.isOk()) {
                Term resultTerm = Term.of(term.getValue(), TokenKind.STAR, factor.getValue());
                return ParserResult.ok(resultTerm, term.getRemaining());
            }
        }

        ParserResult<Token> slash = parseToken(factor.getRemaining(), TokenKind.SLASH);
        if (slash.isOk()) {
            ParserResult<Term> term = parseTerm(slash.getRemaining());
            if (term.isOk()) {
                Term resultTerm = Term.of(term.getValue(), TokenKind.SLASH, factor.getValue());
                return ParserResult.ok(resultTerm, term.getRemaining());
            }
        }

        return ParserResult.ok(Term.of(factor.getValue()), factor.getRemaining());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Application> parseApplicationExpression(View<Token> view) {
        ParserResult<Identifier> identifier = parseIdentifier(view.clone());
        if (identifier.isError()) {
            return ParserResult.error(view, identifier.getMessage());
        }

        ParserResult<Token> leftParen = parseToken(identifier.getRemaining(), TokenKind.LPAREN);
        if (leftParen.isError()) {
            return ParserResult.error(view, leftParen.getMessage());
        }

        ParserResult<List<Expression>> expressionList = parseOneOrMoreSeparatedBy(leftParen.getRemaining(),
                Parser::parseExpression, Optional.of(TokenKind.COMMA));
        ArrayList<Expression> arguments = new ArrayList<>();
        if (expressionList.isOk()) {
            arguments.addAll(expressionList.getValue());
        }

        ParserResult<Token> rightParen = parseToken(expressionList.getRemaining(), TokenKind.RPAREN);
        if (rightParen.isError()) {
            return ParserResult.error(view, rightParen.getMessage());
        }

        Application application = Application.of(identifier.getValue(), arguments);
        return ParserResult.ok(application, rightParen.getRemaining());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Expression> parseExpression(View<Token> view) {
        ParserResult<Application> application = parseApplicationExpression(view.clone());
        if (application.isOk()) {
            Expression expression = Expression.of(application.getValue());
            return ParserResult.ok(expression, application.getRemaining());
        }

        ParserResult<Term> term = parseTerm(view.clone());
        if (term.isError()) {
            return ParserResult.error(view, term.getMessage());
        }

        ParserResult<Token> plus = parseToken(term.getRemaining(), TokenKind.PLUS);
        if (plus.isOk()) {
            ParserResult<Expression> expression = parseExpression(plus.getRemaining());
            if (expression.isOk()) {
                Expression resultExpr = Expression.of(expression.getValue(), TokenKind.PLUS, term.getValue());
                return ParserResult.ok(resultExpr, expression.getRemaining());
            }
        }

        ParserResult<Token> minus = parseToken(term.getRemaining(), TokenKind.MINUS);
        if (minus.isOk()) {
            ParserResult<Expression> expression = parseExpression(minus.getRemaining());
            if (expression.isOk()) {
                Expression resultExpr = Expression.of(expression.getValue(), TokenKind.MINUS, term.getValue());
                return ParserResult.ok(resultExpr, expression.getRemaining());
            }
        }

        return ParserResult.ok(Expression.of(term.getValue()), term.getRemaining());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Block> parseBlock(View<Token> view) {
        ParserResult<Token> leftBrace = parseToken(view.clone(), TokenKind.LBRACE);
        if (leftBrace.isError()) {
            return ParserResult.error(view, leftBrace.getMessage());
        }

        ParserResult<List<Statement>> statements = parseZeroOrMore(leftBrace.getRemaining(), Parser::parseStatement);
        ArrayList<Statement> statementList = new ArrayList<>();
        if (statements.isOk()) {
            statementList.addAll(statements.getValue());
        }

        ParserResult<Token> rightBrace = parseToken(statements.getRemaining(), TokenKind.RBRACE);
        if (rightBrace.isError()) {
            return ParserResult.error(view, rightBrace.getMessage());
        }

        Block block = Block.of(statementList);
        return ParserResult.ok(block, rightBrace.getRemaining());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<IfStatement> parseIfStatement(View<Token> view) {
        ParserResult<Token> ifToken = parseToken(view.clone(), TokenKind.IF);
        if (ifToken.isError()) {
            return ParserResult.error(view, ifToken.getMessage());
        }

        ParserResult<Token> leftParen = parseToken(ifToken.getRemaining(), TokenKind.LPAREN);
        if (leftParen.isError()) {
            return ParserResult.error(view, leftParen.getMessage());
        }

        ParserResult<Expression> expression = parseExpression(leftParen.getRemaining());
        if (expression.isError()) {
            return ParserResult.error(view, expression.getMessage());
        }

        ParserResult<Token> rightParen = parseToken(expression.getRemaining(), TokenKind.RPAREN);
        if (rightParen.isError()) {
            return ParserResult.error(view, rightParen.getMessage());
        }

        ParserResult<Block> thenBlock = parseBlock(rightParen.getRemaining());
        if (thenBlock.isError()) {
            return ParserResult.error(view, thenBlock.getMessage());
        }

        ParserResult<Token> elseToken = parseToken(thenBlock.getRemaining(), TokenKind.ELSE);
        if (elseToken.isOk()) {
            ParserResult<Block> elseBlock = parseBlock(elseToken.getRemaining());
            if (elseBlock.isError()) {
                return ParserResult.error(view, elseBlock.getMessage());
            }

            IfStatement ifStatement = IfStatement.of(expression.getValue(), thenBlock.getValue(), elseBlock.getValue());
            return ParserResult.ok(ifStatement, elseBlock.getRemaining());
        }

        IfStatement ifStatement = IfStatement.of(expression.getValue(), thenBlock.getValue());
        return ParserResult.ok(ifStatement, thenBlock.getRemaining());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<WhileStatement> parseWhileStatement(View<Token> view) {
        ParserResult<Token> whileToken = parseToken(view.clone(), TokenKind.WHILE);
        if (whileToken.isError()) {
            return ParserResult.error(view, whileToken.getMessage());
        }

        ParserResult<Token> leftParen = parseToken(whileToken.getRemaining(), TokenKind.LPAREN);
        if (leftParen.isError()) {
            return ParserResult.error(view, leftParen.getMessage());
        }

        ParserResult<Expression> expression = parseExpression(leftParen.getRemaining());
        if (expression.isError()) {
            return ParserResult.error(view, expression.getMessage());
        }

        ParserResult<Token> rightParen = parseToken(expression.getRemaining(), TokenKind.RPAREN);
        if (rightParen.isError()) {
            return ParserResult.error(view, rightParen.getMessage());
        }

        ParserResult<Block> block = parseBlock(rightParen.getRemaining());
        if (block.isError()) {
            return ParserResult.error(view, block.getMessage());
        }

        WhileStatement whileStatement = WhileStatement.of(expression.getValue(), block.getValue());
        return ParserResult.ok(whileStatement, block.getRemaining());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<AssignmentStatement> parseAssignmentStatement(View<Token> view) {
        ParserResult<Identifier> identifier = parseIdentifier(view.clone());
        if (identifier.isError()) {
            return ParserResult.error(view, identifier.getMessage());
        }

        ParserResult<Token> equals = parseToken(identifier.getRemaining(), TokenKind.EQUALS);
        if (equals.isError()) {
            return ParserResult.error(view, equals.getMessage());
        }

        ParserResult<Expression> expression = parseExpression(equals.getRemaining());
        if (expression.isError()) {
            return ParserResult.error(view, expression.getMessage());
        }

        ParserResult<Token> semicolon = parseToken(expression.getRemaining(), TokenKind.SEMICOLON);
        if (semicolon.isError()) {
            return ParserResult.error(view, semicolon.getMessage());
        }

        AssignmentStatement assignmentStatement = AssignmentStatement.of(identifier.getValue(), expression.getValue());
        return ParserResult.ok(assignmentStatement, semicolon.getRemaining());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<DeclarationStatement> parseDeclarationStatement(View<Token> view) {
        ParserResult<Token> letToken = parseToken(view.clone(), TokenKind.LET);
        if (letToken.isError()) {
            return ParserResult.error(view, letToken.getMessage());
        }

        ParserResult<Tuple<Identifier, Identifier>> typedIdentifier = parseTypedIdentifier(letToken.getRemaining());
        if (typedIdentifier.isError()) {
            return ParserResult.error(view, typedIdentifier.getMessage());
        }

        ParserResult<Token> equals = parseToken(typedIdentifier.getRemaining(), TokenKind.EQUALS);
        if (equals.isError()) {
            return ParserResult.error(view, equals.getMessage());
        }

        ParserResult<Expression> expression = parseExpression(equals.getRemaining());
        if (expression.isError()) {
            return ParserResult.error(view, expression.getMessage());
        }

        ParserResult<Token> semiColon = parseToken(expression.getRemaining(), TokenKind.SEMICOLON);
        if (semiColon.isError()) {
            return ParserResult.error(view, semiColon.getMessage());
        }

        Identifier id = typedIdentifier.getValue().first();
        Identifier type = typedIdentifier.getValue().second();
        DeclarationStatement declarationStatement = DeclarationStatement.of(id, type, expression.getValue());
        return ParserResult.ok(declarationStatement, semiColon.getRemaining());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Statement> parseStatement(View<Token> view) {
        ParserResult<IfStatement> ifStatement = parseIfStatement(view.clone());
        if (ifStatement.isOk()) {
            Statement resultStatement = Statement.of(ifStatement.getValue());
            return ParserResult.ok(resultStatement, ifStatement.getRemaining());
        }

        ParserResult<WhileStatement> whileStatement = parseWhileStatement(view.clone());
        if (whileStatement.isOk()) {
            Statement resultStatement = Statement.of(whileStatement.getValue());
            return ParserResult.ok(resultStatement, whileStatement.getRemaining());
        }

        ParserResult<AssignmentStatement> assignmentStatement = parseAssignmentStatement(view.clone());
        if (assignmentStatement.isOk()) {
            Statement resultStatement = Statement.of(assignmentStatement.getValue());
            return ParserResult.ok(resultStatement, assignmentStatement.getRemaining());
        }

        ParserResult<DeclarationStatement> declarationStatement = parseDeclarationStatement(view.clone());
        if (declarationStatement.isOk()) {
            Statement resultStatement = Statement.of(declarationStatement.getValue());
            return ParserResult.ok(resultStatement, declarationStatement.getRemaining());
        }

        ParserResult<Expression> expression = parseExpression(view.clone());
        if (expression.isOk()) {
            ParserResult<Token> semicolon = parseToken(expression.getRemaining(), TokenKind.SEMICOLON);
            if (semicolon.isOk()) {
                Statement resultStatement = Statement.of(expression.getValue());
                return ParserResult.ok(resultStatement, semicolon.getRemaining());
            }
        }

        return ParserResult.error(view, "Expected statement");
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<DataDeclaration> parseDataDeclaration(View<Token> token) {
        ParserResult<Token> dataToken = parseToken(token.clone(), TokenKind.DATA);
        if (dataToken.isError()) {
            return ParserResult.error(token, dataToken.getMessage());
        }

        ParserResult<Identifier> identifier = parseIdentifier(dataToken.getRemaining());
        if (identifier.isError()) {
            return ParserResult.error(token, identifier.getMessage());
        }

        ParserResult<Token> leftBrace = parseToken(identifier.getRemaining(), TokenKind.LBRACE);
        if (leftBrace.isError()) {
            return ParserResult.error(token, leftBrace.getMessage());
        }

        ParserResult<List<DeclarationStatement>> declarations = parseZeroOrMoreSeparatedBy(leftBrace.getRemaining(),
                Parser::parseDeclarationStatement, Optional.empty());
        ArrayList<DeclarationStatement> declarationStatements = new ArrayList<>();
        if (declarations.isOk()) {
            declarationStatements.addAll(declarations.getValue());
        }

        ParserResult<Token> rightBrace = parseToken(declarations.getRemaining(), TokenKind.RBRACE);
        if (rightBrace.isError()) {
            return ParserResult.error(token, rightBrace.getMessage());
        }

        DataDeclaration dataDeclaration = DataDeclaration.of(identifier.getValue(), declarationStatements);
        return ParserResult.ok(dataDeclaration, rightBrace.getRemaining());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<List<Parameter>> parseParameters(View<Token> token) {
        ParserResult<Token> lParenToken = parseToken(token.clone(), TokenKind.LPAREN);
        if (lParenToken.isError()) {
            return ParserResult.error(token, lParenToken.getMessage());
        }

        // Short circuit if there are no parameters
        ParserResult<Token> rParenToken = parseToken(lParenToken.getRemaining(), TokenKind.RPAREN);
        if (rParenToken.isOk()) {
            return ParserResult.ok(List.of(), rParenToken.getRemaining());
        }

        ParserResult<List<Tuple<Identifier, Identifier>>> typedIdentifiers = parseOneOrMoreSeparatedBy(
                lParenToken.getRemaining(), Parser::parseTypedIdentifier,
                Optional.of(TokenKind.COMMA));
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        if (typedIdentifiers.isOk()) {
            for (Tuple<Identifier, Identifier> typedIdentifier : typedIdentifiers.getValue()) {
                parameters.add(Parameter.of(typedIdentifier.first(), typedIdentifier.second()));
            }
        }

        rParenToken = parseToken(typedIdentifiers.getRemaining(), TokenKind.RPAREN);
        if (rParenToken.isError()) {
            return ParserResult.error(token, rParenToken.getMessage());
        }

        return ParserResult.ok(parameters, rParenToken.getRemaining());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<FunctionDeclaration> parseFunctionDeclaration(View<Token> token) {
        ParserResult<Token> fnToken = parseToken(token.clone(), TokenKind.FN);
        if (fnToken.isError()) {
            return ParserResult.error(token, fnToken.getMessage());
        }

        ParserResult<Identifier> identifier = parseIdentifier(fnToken.getRemaining());
        if (identifier.isError()) {
            return ParserResult.error(token, identifier.getMessage());
        }

        ParserResult<List<Parameter>> parameters = parseParameters(identifier.getRemaining());
        if (parameters.isError()) {
            return ParserResult.error(token, parameters.getMessage());
        }

        ParserResult<Token> colon = parseToken(parameters.getRemaining(), TokenKind.COLON);
        if (colon.isError()) {
            return ParserResult.error(token, colon.getMessage());
        }

        ParserResult<Identifier> type = parseIdentifier(colon.getRemaining());
        if (type.isError()) {
            return ParserResult.error(token, type.getMessage());
        }

        ParserResult<Block> block = parseBlock(type.getRemaining());
        if (block.isError()) {
            return ParserResult.error(token, block.getMessage());
        }

        FunctionDeclaration declaration = FunctionDeclaration.of(identifier.getValue(), parameters.getValue(),
                type.getValue(), block.getValue());
        return ParserResult.ok(declaration, block.getRemaining());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Program> parseProgram(View<Token> token) {
        View<Token> remaining = token.clone();

        ArrayList<FunctionDeclaration> functions = new ArrayList<FunctionDeclaration>();
        ArrayList<DataDeclaration> data = new ArrayList<DataDeclaration>();

        while (true) {
            ParserResult<FunctionDeclaration> function = parseFunctionDeclaration(remaining.clone());
            if (function.isOk()) {
                functions.add(function.getValue());
                remaining = function.getRemaining();
                continue;
            }

            ParserResult<DataDeclaration> dataDeclaration = parseDataDeclaration(remaining.clone());
            if (dataDeclaration.isOk()) {

                data.add(dataDeclaration.getValue());
                remaining = dataDeclaration.getRemaining();
                continue;
            }

            break;
        }

        if (!remaining.isEmpty()) {
            return ParserResult.error(token, "Unexpected end of program, unconsumed tokens: " + remaining);
        } else {
            Program program = Program.of(functions, data);
            return ParserResult.ok(program, remaining);
        }
    }
    // -----------------------------------------------------------------------------------------------------------------
}