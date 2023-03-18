package parser.acceptance;

import org.junit.jupiter.api.Test;
import parser.AssignmentStatement;
import parser.IfStatement;

import java.util.List;
import java.util.logging.Logger;

public class Statements implements AcceptanceTest {
    private static final Logger LOGGER = Logger.getLogger(Statements.class.getName());

    @Override
    public Logger getLogger() {
        return null;
    }

    @Test
    void testAssignments() {
        var cases = List.of("a = 1;", "a = 1 + 2;", "a = 1 + 2 * 3;");
        executeAll(cases, AssignmentStatement::parse);
    }

    @Test
    void testIfStatements() {
        var cases = List.of("if (a + 1) { a = 2; }", "if (a + 1) { a = 2; } else { a = 3; }");
        executeAll(cases, IfStatement::parse);
    }
}

