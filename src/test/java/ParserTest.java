import org.junit.jupiter.api.Test;
import parse.TokenKind;
import parse.View;

import java.util.List;

public class ParserTest {
    @Test
    void shouldParseNumber() {
        var test = parse.Token.of(TokenKind.NUMBER, "1", 0);
        var tokens = List.of(test);
        var view = View.of(tokens);

        var result = parse.Parser.parseExpression(view);

        assert result.isOk();
    }
}
