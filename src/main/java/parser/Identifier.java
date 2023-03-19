package parser;

import tokenizer.Token;
import tokenizer.TokenKind;
import util.View;

public class Identifier implements Node {
    // -----------------------------------------------------------------------------------------------------------------
    public String value;

    // -----------------------------------------------------------------------------------------------------------------
    Identifier(String value) {
        this.value = value;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static ParserResult<Identifier> parse(View<Token> view) {
        var token = Parse.token(view.clone(), TokenKind.IDENTIFIER);
        if (token.isError()) {
            return ParserResult.error(view, token.getMessage());
        }
        var identifier = new Identifier(token.getValue().symbol());
        return ParserResult.ok(identifier, token.getRemaining());
    }
    // -----------------------------------------------------------------------------------------------------------------
}

