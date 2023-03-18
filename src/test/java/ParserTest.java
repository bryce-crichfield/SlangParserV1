import parser.Parser;
import parser.ParserResult;
import parser.Expression;
import tokenizer.Token;
import org.junit.jupiter.api.Test;
import tokenizer.TokenKind;
import util.View;

import java.util.List;

public class ParserTest {
    @Test
    void shouldParseNumber() {
        var test = Token.of(TokenKind.NUMBER, "1", 0);
        var tokens = List.of(test);
        var view = View.of(tokens);

        ParserResult<Expression> result = Parser.parseExpression(view);

        assert result.isOk();
    }
}
