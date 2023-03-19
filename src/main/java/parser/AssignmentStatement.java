package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

public class AssignmentStatement implements Node {
    // -----------------------------------------------------------------------------------------------------------------
    public CompositeIdentifier compositeIdentifier;
    public Expression expression;

    // -----------------------------------------------------------------------------------------------------------------
    public AssignmentStatement(CompositeIdentifier compositeIdentifier, Expression expression) {
        this.compositeIdentifier = compositeIdentifier;
        this.expression = expression;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        compositeIdentifier.accept(visitor);
        expression.accept(visitor);
        visitor.exit(this);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<AssignmentStatement> parse(View<Token> view) {
        var compositeIdentifier = CompositeIdentifier.parse(view.clone());
        if (compositeIdentifier.isError()) {
            return ParserResult.error(view, compositeIdentifier.getMessage());
        }

        var equals = Parse.token(compositeIdentifier.getRemaining(), TokenKind.ASSIGN);
        if (equals.isError()) {
            return ParserResult.error(view, equals.getMessage());
        }

        var expression = Expression.parse(equals.getRemaining());
        if (expression.isError()) {
            return ParserResult.error(view, expression.getMessage());
        }

        var semicolon = Parse.token(expression.getRemaining(), TokenKind.SEMICOLON);
        if (semicolon.isError()) {
            return ParserResult.error(view, semicolon.getMessage());
        }

        var assignmentStatement = new AssignmentStatement(compositeIdentifier.getValue(), expression.getValue());
        return ParserResult.ok(assignmentStatement, semicolon.getRemaining());
    }
    // -----------------------------------------------------------------------------------------------------------------
}

