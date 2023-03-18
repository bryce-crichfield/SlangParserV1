import parser.Parser;
import parser.ParserResult;
import parser.syntax.Expression;
import tokenizer.Token;
import org.junit.jupiter.api.Test;
import tokenizer.TokenKind;
import data.View;

import java.util.List;

public class ParserTest {
    @Test
    void shouldParseNumber() {
        Token test = Token.of(TokenKind.NUMBER, "1", 0);
        List<Token> tokens = List.of(test);
        View<Token> view = View.of(tokens);

        ParserResult<Expression> result = Parser.parseExpression(view);

        assert result.isOk();
    }
}
