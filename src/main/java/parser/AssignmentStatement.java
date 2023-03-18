package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

public class AssignmentStatement implements Node {
    public Accessor accessor;
    public Expression expression;

    AssignmentStatement(Accessor accessor, Expression expression) {
        this.accessor = accessor;
        this.expression = expression;
    }

    public static ParserResult<AssignmentStatement> parse(View<Token> view) {
        var accessor = Accessor.parse(view.clone());
        if (accessor.isError()) {
            return ParserResult.error(view, accessor.getMessage());
        }

        var equals = Parser.token(accessor.getRemaining(), TokenKind.EQUALS);
        if (equals.isError()) {
            return ParserResult.error(view, equals.getMessage());
        }

        var expression = Expression.parse(equals.getRemaining());
        if (expression.isError()) {
            return ParserResult.error(view, expression.getMessage());
        }

        var semicolon = Parser.token(expression.getRemaining(), TokenKind.SEMICOLON);
        if (semicolon.isError()) {
            return ParserResult.error(view, semicolon.getMessage());
        }

        var assignmentStatement = new AssignmentStatement(accessor.getValue(), expression.getValue());
        return ParserResult.ok(assignmentStatement, semicolon.getRemaining());
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        accessor.accept(visitor);
        expression.accept(visitor);
        visitor.exit(this);
    }
}

