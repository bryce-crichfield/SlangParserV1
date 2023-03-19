package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

public class Number implements Node {
    // -----------------------------------------------------------------------------------------------------------------
    public double value;

    // -----------------------------------------------------------------------------------------------------------------
    public Number(double value) {
        this.value = value;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Number> parse(View<Token> view) {
        var token = Parse.token(view.clone(), TokenKind.NUMBER);
        if (token.isError()) {
            return ParserResult.error(view, token.getMessage());
        }
        var number = new Number(Double.parseDouble(token.getValue().symbol()));
        return ParserResult.ok(number, token.getRemaining());
    }
    // -----------------------------------------------------------------------------------------------------------------
}

