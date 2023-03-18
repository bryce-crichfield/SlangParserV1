package parser.acceptance;

import org.junit.jupiter.api.Test;
import parser.DataDeclaration;
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
        var cases = new ArrayList<String>();
        cases.add("(abc: int, def: int)");
        cases.add("(abc: int)");
        cases.add("()");
        assertAllOk(cases, Parameters::parse);
    }

    @Test
    void testFunctionDefinition() {
        var cases = new ArrayList<String>();
        cases.add("fn f(): int { return 1; }");
        cases.add("fn f(abc: int, def: num): int { return 1; }");
        cases.add("fn f(abc: int, def: num) { return abc + def; }");
        cases.add("fn f() { abc = 1; def = 2; }");
        assertAllOk(cases, FunctionDeclaration::parse);
    }

    @Test
    void testDataDeclaration() {
        var cases = new ArrayList<String>();
        cases.add("data A { }");
        cases.add("data A { abc: int }");
        cases.add("data A { x: num, y: num }");
        assertAllOk(cases, DataDeclaration::parse);
    }
}
