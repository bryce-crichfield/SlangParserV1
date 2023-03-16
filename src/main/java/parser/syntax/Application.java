package parser.syntax;
import java.util.List;
public record Application(List<Expression> expressions) implements Node {
    public static Application of(List<Expression> expressions) {
        return new Application(expressions);
    }
}

