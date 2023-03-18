package parser.acceptance;

import parser.ParserResult;
import tokenizer.Token;
import tokenizer.Tokenizer;
import util.View;

import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

public interface AcceptanceTest {
    Logger getLogger();

    private <A> void execute(AcceptanceCase<A> testCase) {
        var tokens = Tokenizer.tokenize(testCase.input());
        var result = testCase.parser().apply(tokens);
        if (result.isError()) {
            getLogger().info("Failed to accept input: " + testCase.input() + " with error: " + result.getMessage());
            if (!result.getRemaining().isEmpty()) {
                getLogger().info("Unconsumed input: " + result.getRemaining());
            }
        }
        assert result.isOk();
    }

    public default <A> void executeAll(List<String> testCases, Function<View<Token>, ParserResult<A>> parser) {
        for (var testCase : testCases) {
            var acceptanceCase = new AcceptanceCase<>(testCase, parser);
            this.execute(acceptanceCase);
        }
    }
}
