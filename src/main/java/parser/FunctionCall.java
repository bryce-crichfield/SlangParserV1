package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FunctionCall implements Node {
    public Identifier identifier;
    public List<Expression> expressions;

    FunctionCall(Identifier identifier, List<Expression> expressions) {
        this.identifier = identifier;
        this.expressions = expressions;
    }

    public static ParserResult<FunctionCall> parse(View<Token> view) {
        var identifier = Identifier.parse(view.clone());
        if (identifier.isError()) {
            return ParserResult.error(view, identifier.getMessage());
        }

        var leftParen = Parser.token(identifier.getRemaining(), TokenKind.LPAREN);
        if (leftParen.isError()) {
            return ParserResult.error(view, leftParen.getMessage());
        }

        var expressionList = Parser.oneOrMoreSeparatedBy(
                leftParen.getRemaining(),
                Expression::parse,
                Optional.of(TokenKind.COMMA)
        );
        var arguments = new ArrayList<Expression>();
        if (expressionList.isOk()) {
            arguments.addAll(expressionList.getValue());
        }

        var rightParen = Parser.token(expressionList.getRemaining(), TokenKind.RPAREN);
        if (rightParen.isError()) {
            return ParserResult.error(view, rightParen.getMessage());
        }

        var application = new FunctionCall(identifier.getValue(), arguments);
        return ParserResult.ok(application, rightParen.getRemaining());
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        identifier.accept(visitor);
        for (var expression : expressions) {
            expression.accept(visitor);
        }
        visitor.exit(this);
    }
}

