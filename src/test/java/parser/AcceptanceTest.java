package parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import tokenizer.Token;
import tokenizer.Tokenizer;
import util.View;

import java.util.List;
import java.util.function.Function;

public class AcceptanceTest {

    <A> ParserResult<A> test(String input, Function<View<Token>, ParserResult<A>> parser) {
        var tokens = Tokenizer.tokenize(input);
        return parser.apply(tokens);
    }

    <A> void acceptanceTest(List<String> pass, List<String> fail, Function<View<Token>, ParserResult<A>> parser) {
        pass.forEach(testCase -> {
            var result = test(testCase, parser);
            if (result.isError()) {
                throw new RuntimeException("Test case '" + testCase + "' failed: " + result.getMessage());
            }

            // Check that the entire input was consumed
            if (!result.getRemaining().isEmpty()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Test case '").append(testCase).append("' failed: ");
                sb.append("Expected the entire input to be consumed, but there are still tokens left: \n");
                result.getRemaining().forEach(token -> sb.append(token).append("\n"));
                throw new RuntimeException(sb.toString());
            }
        });
        fail.forEach(testCase -> {
            var result = test(testCase, parser);
            if (!result.isError()) {
                throw new RuntimeException("Test case '" + testCase + "' should have failed");
            }
        });
    }

    @Test
    void testNumber() {
        var shouldPass = List.of("123", "0.123", "123.0");
        var shouldFail = List.of("123.", "");
        acceptanceTest(shouldPass, shouldFail, Number::parse);
    }

    @Test
    void testIdentifier() {
        var shouldPass = List.of("abc", "abc123", "abc_123");
        var shouldFail = List.of("123abc", "123", "1");
        acceptanceTest(shouldPass, shouldFail, Identifier::parse);
    }

    @Test
    void testCompositeIdentifier() {
        var shouldPass = List.of("a", "a.b", "a.a_2");
        var shouldFail = List.of("");
        acceptanceTest(shouldPass, shouldFail, CompositeIdentifier::parse);
    }

    @Test
    void testFactor() {
        // We can't know if the parser didn't just parse because ambiguous
        // Must determine in a stricter validation step
        var shouldPass = List.of("123", "abc", "a.b", "a.b.c", "(123)", "(abc)", "(a.b)", "(a.b.c)");
        var shouldFail = List.of("");
        acceptanceTest(shouldPass, shouldFail, Factor::parse);
    }

    @Test
    void testTerm() {
        // We can't know if the parser didn't just parse because ambiguous
        // Must determine in a stricter validation step
        var shouldPass = List.of("a * c", "a / c", "a * c / d", "a / c * d", "a * c / d * e");
        var shouldFail = List.of("");
        acceptanceTest(shouldPass, shouldFail, Term::parse);
    }

    @Test
    void testExpression() {
        // We can't know if the parser didn't just parse because ambiguous
        // Must determine in a stricter validation step
        var shouldPass = List.of("a + b", "a - b", "a + b - c", "a - b + c", "a + b - c + d");
        var shouldFail = List.of("");
        acceptanceTest(shouldPass, shouldFail, Expression::parse);
    }

    @Test
    void testAssignmentStatement() {
        // TODO
    }

    @Test
    void testDeclarationStatement() {
        // TODO
    }

    @Test
    void testForStatement() {
        // TODO
    }

    @Test
    void testIfStatement() {
        // TODO
    }
}
