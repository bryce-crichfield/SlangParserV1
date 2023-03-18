package parser.acceptance;

import org.junit.jupiter.api.Test;
import parser.FunctionDeclaration;
import parser.Parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Toplevel implements AcceptanceTest {
    private static final Logger LOGGER = Logger.getLogger(Expressions.class.getName());

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Test
    void testParameters() {
        var cases = List.of("abc: int, def: num");
        assertAllOk(cases, Parameters::parse);
    }

    @Test
    void testFunctionDefinition() {
        var cases = new ArrayList<String>();
        cases.add("def f(): int { return 1; }");
        cases.add("def f(abc: int, def: num): int { return 1; }");
        cases.add("def f(abc: int, def: num) { return abc + def; }");
        cases.add("def f() { abc = 1; def = 2; }");
        assertAllOk(cases, FunctionDeclaration::parse);
    }

    @Test
    void testDataDeclaration() {
        var cases = new ArrayList<String>();
        cases.add("data A { x: num, y: num }");
        assertAllOk(cases, FunctionDeclaration::parse);
    }
}
