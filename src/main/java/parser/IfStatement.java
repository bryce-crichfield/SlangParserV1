package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IfStatement implements Node {
    // -----------------------------------------------------------------------------------------------------------------
    public Expression condition;
    public Block thenBlock;
    public List<Block> elseIfBlocks = new ArrayList<>();
    public Optional<Block> elseBlock = Optional.empty();

    // -----------------------------------------------------------------------------------------------------------------
    IfStatement(Expression condition, Block thenBlock, List<Block> elseIfBlocks, Optional<Block> elseBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseIfBlocks = elseIfBlocks;
        this.elseBlock = elseBlock;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        condition.accept(visitor);
        thenBlock.accept(visitor);
        elseIfBlocks.forEach(block -> block.accept(visitor));
        elseBlock.ifPresent(block -> block.accept(visitor));
        visitor.exit(this);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<IfStatement> parse(View<Token> view) {
        var ifToken = Parse.token(view.clone(), TokenKind.IF);
        if (ifToken.isError()) {
            return ParserResult.error(view, ifToken.getMessage());
        }

        var leftParen = Parse.token(ifToken.getRemaining(), TokenKind.LPAREN);
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

        var thenBlock = Block.parse(rightParen.getRemaining());
        if (thenBlock.isError()) {
            return ParserResult.error(view, thenBlock.getMessage());
        }

        // Parse Zero Or More Else If Blocks
        var elseIfs = Parse.zeroOrMore(thenBlock.getRemaining(), IfStatement::parseElseIfBlock);
        var elseBlock = Parse.optional(elseIfs.getRemaining(), IfStatement::parseElseBlock);
        var ifStatement = new IfStatement(
                expression.getValue(),
                thenBlock.getValue(),
                elseIfs.getValue(),
                elseBlock.getValue()
        );
        return ParserResult.ok(ifStatement, elseBlock.getRemaining());
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static ParserResult<Block> parseElseIfBlock(View<Token> tokens) {
        var elseToken = Parse.token(tokens.clone(), TokenKind.ELSE);
        if (elseToken.isError()) {
            return ParserResult.error(tokens, elseToken.getMessage());
        }

        var ifToken = Parse.token(elseToken.getRemaining(), TokenKind.IF);
        if (ifToken.isError()) {
            return ParserResult.error(tokens, ifToken.getMessage());
        }

        var block = Block.parse(ifToken.getRemaining());
        if (block.isError()) {
            return ParserResult.error(tokens, block.getMessage());
        }

        return ParserResult.ok(block.getValue(), block.getRemaining());
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static ParserResult<Block> parseElseBlock(View<Token> tokens) {
        var elseToken = Parse.token(tokens.clone(), TokenKind.ELSE);
        if (elseToken.isError()) {
            return ParserResult.error(tokens, elseToken.getMessage());
        }

        var elseBlock = Block.parse(elseToken.getRemaining());
        if (elseBlock.isError()) {
            return ParserResult.error(tokens, elseBlock.getMessage());
        }

        return ParserResult.ok(elseBlock.getValue(), elseBlock.getRemaining());
    }
    // -----------------------------------------------------------------------------------------------------------------
}
