package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

public class WhileStatement implements Node {
    public Expression condition;
    public Block block;

    WhileStatement(Expression condition, Block block) {
        this.condition = condition;
        this.block = block;
    }

    public static ParserResult<WhileStatement> parse(View<Token> view) {
        var whileToken = Parse.token(view.clone(), TokenKind.WHILE);
        if (whileToken.isError()) {
            return ParserResult.error(view, whileToken.getMessage());
        }

        var leftParen = Parse.token(whileToken.getRemaining(), TokenKind.LPAREN);
        if (leftParen.isError()) {
            return ParserResult.error(view, leftParen.getMessage());
        }

        var expression = Expression.parse(leftParen.getRemaining());
        if (expression.isError()) {
            return ParserResult.error(view, expression.getMessage());
        }

        var rightParen = Parse.token(expression.getRemaining(), TokenKind.RPAREN);
        if (rightParen.isError()) {
            return ParserResult.error(view, rightParen.getMessage());
        }

        var block = Block.parse(rightParen.getRemaining());
        if (block.isError()) {
            return ParserResult.error(view, block.getMessage());
        }

        var whileStatement = new WhileStatement(expression.getValue(), block.getValue());
        return ParserResult.ok(whileStatement, block.getRemaining());
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        condition.accept(visitor);
        block.accept(visitor);
        visitor.exit(this);
    }
}

