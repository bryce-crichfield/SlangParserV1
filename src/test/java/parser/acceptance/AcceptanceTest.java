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

    default <A> void assertAllOk(List<String> testCases, Function<View<Token>, ParserResult<A>> parser) {
        for (var testCase : testCases) {
            var tokens = Tokenizer.tokenize(testCase);
            var result = parser.apply(tokens);
            if (result.isError()) {
                getLogger().info("Failed to accept input: " + testCase + " with error: " + result.getMessage());
                if (!result.getRemaining().isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    result.getRemaining().forEach(token -> sb.append(token.toString()).append("\n"));
                    getLogger().info("Unconsumed input: " + sb);
                }
            }
            assert result.isOk();
        }
    }

    default <A> void assertAllError(List<String> testCases, Function<View<Token>, ParserResult<A>> parser) {
        for (var testCase : testCases) {
            var tokens = Tokenizer.tokenize(testCase);
            var result = parser.apply(tokens);
            if (result.isOk()) {
                getLogger().info("Expected test case: " + testCase + " to fail");
                if (!result.getRemaining().isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    result.getRemaining().forEach(token -> sb.append(token.toString()));
                    getLogger().info("Unconsumed input: " + sb);
                }
            }
            assert result.isError();
        }
    }
}
