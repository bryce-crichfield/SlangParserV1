package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

import java.util.ArrayList;
import java.util.List;

public class Block implements Node {
    public List<Statement> statements;

    public Block(List<Statement> statements) {
        this.statements = statements;
    }

    public static ParserResult<Block> parse(View<Token> view) {
        var leftBrace = Parser.token(view.clone(), TokenKind.LBRACE);
        if (leftBrace.isError()) {
            return ParserResult.error(view, leftBrace.getMessage());
        }

        var statements = Parser.zeroOrMore(leftBrace.getRemaining(), Statement::parse);
        var statementList = new ArrayList<Statement>();
        if (statements.isOk()) {
            statementList.addAll(statements.getValue());
        }

        var rightBrace = Parser.token(statements.getRemaining(), TokenKind.RBRACE);
        if (rightBrace.isError()) {
            return ParserResult.error(view, rightBrace.getMessage());
        }

        var block = new Block(statementList);
        return ParserResult.ok(block, rightBrace.getRemaining());
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        for (var statement : statements) {
            statement.accept(visitor);
        }
        visitor.exit(this);
    }
}

