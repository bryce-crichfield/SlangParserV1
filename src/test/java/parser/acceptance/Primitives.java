package parser.acceptance;

import org.junit.jupiter.api.Test;
import parser.CompositeIdentifier;
import parser.Identifier;
import parser.Number;
import parser.TypeSpecifier;

import java.util.List;
import java.util.logging.Logger;

public class Primitives implements AcceptanceTest {
    private static final Logger LOGGER = Logger.getLogger(Primitives.class.getName());

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Test
    void testNumbers() {
        var cases = List.of("123", "123.451", "03.24");
        assertAllOk(cases, Number::parse);
    }

    @Test
    void testIdentifiers() {
        var cases = List.of("abc", "abc123", "abc_123");
        assertAllOk(cases, Identifier::parse);
    }

    @Test
    void testTypeSpecifiers() {
        var cases = List.of(": int", ": num");
        assertAllOk(cases, TypeSpecifier::parse);
    }

    @Test
    void testCompositeIdentifier() {
        var cases = List.of("abc.def", "abc.def.ghi");
        assertAllOk(cases, CompositeIdentifier::parse);
    }
}
