import tokenizer.Token;
import org.junit.jupiter.api.Test;
import tokenizer.TokenKind;
import data.View;

import java.util.List;

public class ParserTest {
    @Test
    void shouldParseNumber() {
        var test = Token.of(TokenKind.NUMBER, "1", 0);
        var tokens = List.of(test);
        var view = View.of(tokens);

        var result = parser.Parser.parseExpression(view);

        assert result.isOk();
    }
}
