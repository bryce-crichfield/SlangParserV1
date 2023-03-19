package parser.acceptance;

import org.junit.jupiter.api.Test;
import parser.Expression;
import parser.Factor;
import parser.Term;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Expressions implements AcceptanceTest {
    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger LOGGER = Logger.getLogger(Expressions.class.getName());

    @Override
    public Logger getLogger() {
        return LOGGER;
    }
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void testFactors() {
        var cases = List.of("123", "abc", "(123)", "(abc)");
        assertAllOk(cases, Factor::parse);
    }
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void testTerms() {
        var cases = List.of("123", "abc", "123 * abc", "abc * 123", "abc * abc", "abc / abc");
        assertAllOk(cases, Term::parse);
    }
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void testFunctionCalls() {
        var cases = List.of("f()", "f(1)", "f(1, 2)", "f(1, 2, 3)");
        assertAllOk(cases, Expression::parse);
    }
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void testExpressions() {
        var cases = new ArrayList<String>();
        cases.add("1 + 2");
        cases.add("1 + 2 + 3");
        cases.add("1 + 2 * 3");
        cases.add("1 * 2 + 3");
        cases.add("1 * 2 * 3");
        cases.add("1 * 2 / 3");
        cases.add("1 / 2 * 3");
        cases.add("1 / 2 / 3");
        cases.add("1 + 2 * 3 + 4");
        cases.add("1 * 2 + 3 * 4");
        cases.add("1 == 2 + 3");
        cases.add("1 + 2 == 3");
        cases.add("1 + 2 == 3 + 4");
        cases.add("1 + 2 == 3 * 4");
        cases.add("1 * 2 == 3 + 4");
        cases.add("1 * 2 == (3 & 4)");
        cases.add("1 * 2 == (3 | 4)");
        cases.add("1 * 2 == (3 ** 4)");
        assertAllOk(cases, Expression::parse);
    }
    // -----------------------------------------------------------------------------------------------------------------
}
