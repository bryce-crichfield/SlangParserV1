package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

public class Number implements Node {
    public double value;

    Number(double value) {
        this.value = value;
    }

    public static ParserResult<Number> parse(View<Token> view) {
        var token = Parser.token(view.clone(), TokenKind.NUMBER);
        if (token.isError()) {
            return ParserResult.error(view, token.getMessage());
        }
        var number = new Number(Double.parseDouble(token.getValue().symbol()));
        return ParserResult.ok(number, token.getRemaining());
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

