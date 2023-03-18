package parser.acceptance;

import org.junit.jupiter.api.Test;
import parser.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Statements implements AcceptanceTest {
    private static final Logger LOGGER = Logger.getLogger(Statements.class.getName());

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Test
    void testAssignments() {
        var cases = List.of("a = 1;", "a = 1 + 2;", "a = 1 + 2 * 3;");
        assertAllOk(cases, AssignmentStatement::parse);
    }

    @Test
    void testIfStatements() {
        ArrayList<String> cases = new ArrayList<>();
        cases.add("if (a) { b = 1; }");
        cases.add("if (a) { b = 1; } else { b = 2; }");
        cases.add("if (a) { b = 1; } else if (b) { b = 2; } else { b = 3; }");
        cases.add("if (a) { b = 1; } else if (b) { b = 2; } else if (c) { b = 3; }");
        cases.add("if (a) { b = 1; } else if (b) { b = 2; } else if (c) { b = 3; } else { b = 4; }");
        assertAllOk(cases, IfStatement::parse);
    }

    @Test
    void testForStatements() {
        ArrayList<String> cases = new ArrayList<>();
        cases.add("for (i: 0 until 10) { b = 1; }");
        cases.add("for (i: (0 + 3) to 10) { b = 1; }");
        cases.add("for (i: 0 until 10 by 1) { b = 1; }");
        cases.add("for (i: (0 + 3) to 10 by 2) { b = 1; }");
        assertAllOk(cases, ForStatement::parse);
    }

    @Test
    void testWhileStatements() {
        ArrayList<String> cases = new ArrayList<>();
        cases.add("while (a) { b = 1; }");
        cases.add("while (a + 1 - 3) { b = 1; }");
        assertAllOk(cases, WhileStatement::parse);
    }

    @Test
    void testDeclarationStatements() {
        ArrayList<String> cases = new ArrayList<>();
        cases.add("let a = 1;");
        cases.add("let a: int = 1;");
        cases.add("let a: int = 1 + 2;");
        cases.add("let a = 1 + 2 * 3;");
        assertAllOk(cases, DeclarationStatement::parse);

        cases.clear();
        cases.add("let a: int;");
        cases.add("let a;");
        assertAllError(cases, DeclarationStatement::parse);
    }

    @Test
    void testReturnStatements() {
        ArrayList<String> cases = new ArrayList<>();
        cases.add("return;");
        cases.add("return 1;");
        cases.add("return 1 + 2;");
        cases.add("return 1 + 2 * 3;");
        assertAllOk(cases, ReturnStatement::parse);
    }
}

