package dk.brics.automaton;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DatatypesAutomatonProviderTest {

    private DatatypesAutomatonProvider provider;

    @BeforeEach
    public void setUp() {
        provider = new DatatypesAutomatonProvider();
    }

    @Test
    public void testDefaultConstructor() {
        DatatypesAutomatonProvider defaultProvider = new DatatypesAutomatonProvider();
        assertNotNull(defaultProvider);
    }

    @Test
    public void testParameterizedConstructor() {
        DatatypesAutomatonProvider customProvider = new DatatypesAutomatonProvider(true, false, true);
        assertNotNull(customProvider);
    }

    @Test
    public void testGetAutomatonWithUnicodeBlockName() {
        provider = new DatatypesAutomatonProvider(true, false, false);
        Automaton automaton = provider.getAutomaton("BasicLatin");

        assertNotNull(automaton);
        assertTrue(automaton.run("A"));
        assertFalse(automaton.run("Ā"));
    }

    @Test
    public void testGetAutomatonWithUnicodeCategoryName() {
        provider = new DatatypesAutomatonProvider(false, true, false);
        Automaton automaton = provider.getAutomaton("Lu");

        assertNotNull(automaton);
        assertTrue(automaton.run("A"));
        assertFalse(automaton.run("a"));
    }

    @Test
    public void testGetAutomatonWithXMLName() {
        provider = new DatatypesAutomatonProvider(false, false, true);
        Automaton automaton = provider.getAutomaton("NCName");

        assertNotNull(automaton);
        assertTrue(automaton.run("validName"));
        assertFalse(automaton.run("invalid name"));
    }

    @Test
    public void testGetAutomatonWithInvalidName() {
        Automaton automaton = provider.getAutomaton("InvalidName");

        assertNull(automaton);
    }

    @Test
    public void testGetAutomatonWithAllFeaturesDisabled() {
        provider = new DatatypesAutomatonProvider(false, false, false);
        Automaton automaton = provider.getAutomaton("NCName");

        assertNull(automaton);
    }

    @Test
    public void testGetAutomatonWithAllFeaturesEnabled() {
        provider = new DatatypesAutomatonProvider(true, true, true);

        // Test Unicode Block Name
        Automaton automaton = provider.getAutomaton("BasicLatin");
        assertNotNull(automaton);
        assertTrue(automaton.run("A"));
        assertFalse(automaton.run("Ā"));

        // Test Unicode Category Name
        automaton = provider.getAutomaton("Lu");
        assertNotNull(automaton);
        assertTrue(automaton.run("A"));
        assertFalse(automaton.run("a"));

        // Test XML Name
        automaton = provider.getAutomaton("NCName");
        assertNotNull(automaton);
        assertTrue(automaton.run("validName"));
        assertFalse(automaton.run("invalid name"));
    }

    @Test
    public void testGetAutomatonWithUnicodeBlockNameDisabled() {
        provider = new DatatypesAutomatonProvider(false, true, true);
        Automaton automaton = provider.getAutomaton("BasicLatin");

        assertNull(automaton);
    }

    @Test
    public void testGetAutomatonWithUnicodeCategoryNameDisabled() {
        provider = new DatatypesAutomatonProvider(true, false, true);
        Automaton automaton = provider.getAutomaton("Lu");

        assertNull(automaton);
    }

    @Test
    public void testGetAutomatonWithXMLNameDisabled() {
        provider = new DatatypesAutomatonProvider(true, true, false);
        Automaton automaton = provider.getAutomaton("NCName");

        assertNull(automaton);
    }
}
