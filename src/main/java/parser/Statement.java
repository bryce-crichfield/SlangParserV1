package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

import java.util.Optional;

public class Statement implements Node {
    // -----------------------------------------------------------------------------------------------------------------
    public Optional<IfStatement> ifStatement = Optional.empty();
    public Optional<WhileStatement> whileStatement = Optional.empty();
    public Optional<AssignmentStatement> assignmentStatement = Optional.empty();
    public Optional<DeclarationStatement> declarationStatement = Optional.empty();
    public Optional<Expression> expression = Optional.empty();
    public Optional<ReturnStatement> returnStatement = Optional.empty();
    public Optional<ForStatement> forStatement = Optional.empty();
    public Optional<UseStatement> useStatement = Optional.empty();
    public Optional<ErrorStatement> errorStatement = Optional.empty();

    // -----------------------------------------------------------------------------------------------------------------
    Statement(IfStatement ifStatement) {
        this.ifStatement = Optional.of(ifStatement);
    }

    Statement(WhileStatement whileStatement) {
        this.whileStatement = Optional.of(whileStatement);
    }

    Statement(AssignmentStatement assignmentStatement) {
        this.assignmentStatement = Optional.of(assignmentStatement);
    }

    Statement(DeclarationStatement declarationStatement) {
        this.declarationStatement = Optional.of(declarationStatement);
    }

    Statement(Expression expression) {
        this.expression = Optional.of(expression);
    }

    Statement(ReturnStatement returnStatement) {
        this.returnStatement = Optional.of(returnStatement);
    }

    Statement(ForStatement forStatement) {
        this.forStatement = Optional.of(forStatement);
    }

    Statement(UseStatement useStatement) {
        this.useStatement = Optional.of(useStatement);
    }

    Statement(ErrorStatement errorStatement) {
        this.errorStatement = Optional.of(errorStatement);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        ifStatement.ifPresent(s -> s.accept(visitor));
        whileStatement.ifPresent(s -> s.accept(visitor));
        assignmentStatement.ifPresent(s -> s.accept(visitor));
        declarationStatement.ifPresent(s -> s.accept(visitor));
        forStatement.ifPresent(s -> s.accept(visitor));
        expression.ifPresent(e -> e.accept(visitor));
        visitor.exit(this);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        // TODO: We could replace with a delegation to a ToStringVisitor
        if (ifStatement.isPresent()) {
            return ifStatement.toString();
        }
        if (whileStatement.isPresent()) {
            return whileStatement.toString();
        }
        if (assignmentStatement.isPresent()) {
            return assignmentStatement.toString();
        }
        if (declarationStatement.isPresent()) {
            return declarationStatement.toString();
        }
        if (forStatement.isPresent()) {
            return forStatement.toString();
        }
        if (expression.isPresent()) {
            return expression.toString();
        }
        return "Statement";
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Statement> parse(View<Token> view) {
        var ifStatement = IfStatement.parse(view.clone());
        if (ifStatement.isOk()) {
            var resultStatement = new Statement(ifStatement.getValue());
            return ParserResult.ok(resultStatement, ifStatement.getRemaining());
        }

        var whileStatement = WhileStatement.parse(view.clone());
        if (whileStatement.isOk()) {
            var resultStatement = new Statement(whileStatement.getValue());
            return ParserResult.ok(resultStatement, whileStatement.getRemaining());
        }

        var assignmentStatement = AssignmentStatement.parse(view.clone());
        if (assignmentStatement.isOk()) {
            var resultStatement = new Statement(assignmentStatement.getValue());
            return ParserResult.ok(resultStatement, assignmentStatement.getRemaining());
        }

        var declarationStatement = DeclarationStatement.parse(view.clone());
        if (declarationStatement.isOk()) {
            var resultStatement = new Statement(declarationStatement.getValue());
            return ParserResult.ok(resultStatement, declarationStatement.getRemaining());
        }

        var useStatement = UseStatement.parse(view.clone());
        if (useStatement.isOk()) {
            var resultStatement = new Statement(useStatement.getValue());
            return ParserResult.ok(resultStatement, useStatement.getRemaining());
        }

        var errorStatement = ErrorStatement.parse(view.clone());
        if (errorStatement.isOk()) {
            var resultStatement = new Statement(errorStatement.getValue());
            return ParserResult.ok(resultStatement, errorStatement.getRemaining());
        }

        var returnStatement = ReturnStatement.parse(view.clone());
        if (returnStatement.isOk()) {
            var resultStatement = new Statement(returnStatement.getValue());
            return ParserResult.ok(resultStatement, returnStatement.getRemaining());
        }

        var forStatement = ForStatement.parse(view.clone());
        if (forStatement.isOk()) {
            var resultStatement = new Statement(forStatement.getValue());
            return ParserResult.ok(resultStatement, forStatement.getRemaining());
        }

        var expression = Expression.parse(view.clone());
        if (expression.isOk()) {
            var semicolon = Parse.token(expression.getRemaining(), TokenKind.SEMICOLON);
            if (semicolon.isOk()) {
                var resultStatement = new Statement(expression.getValue());
                return ParserResult.ok(resultStatement, semicolon.getRemaining());
            }
        }

        return ParserResult.error(view, "Expected statement");
    }
    // -----------------------------------------------------------------------------------------------------------------
}
