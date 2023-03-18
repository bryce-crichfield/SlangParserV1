package parser.syntax;

import parser.ParserResult;
import tokenizer.Token;
import util.View;

public record Number(double value) implements Node {
    public static Number of(double value) {
        return new Number(value);
    }

    public void accept(NodeVisitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}

