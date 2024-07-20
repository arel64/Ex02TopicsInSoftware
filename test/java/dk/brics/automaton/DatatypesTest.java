package dk.brics.automaton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
public class DatatypesTest {

    private Automaton automaton;

    @BeforeEach
    void setUp() {
        automaton = mock(Automaton.class);
    }

    @Test
    void testGet() {
        String name = "whitespace";
        Automaton result = Datatypes.get(name);
        assertNotNull(result);
    }

    @Test
    void testIsUnicodeBlockName() {
        assertTrue(Datatypes.isUnicodeBlockName("BasicLatin"));
        assertFalse(Datatypes.isUnicodeBlockName("NonExistentBlock"));
    }

    @Test
    void testIsUnicodeCategoryName() {
        assertTrue(Datatypes.isUnicodeCategoryName("Lu"));
        assertFalse(Datatypes.isUnicodeCategoryName("NonExistentCategory"));
    }

    @Test
    void testIsXMLName() {
        assertTrue(Datatypes.isXMLName("NCName"));
        assertFalse(Datatypes.isXMLName("NonExistentXMLName"));
    }
    @Disabled
    @Test
    void testExists() throws IOException {
        String name = "whitespace";
        assertTrue(Datatypes.exists(name));
        String nonExistentName = "NonExistentAutomaton";
        assertFalse(Datatypes.exists(nonExistentName));
    }

    @Test
    void testBuildAll() {
        Datatypes.main(new String[]{});
        automaton = Datatypes.get("whitespace");
        assertNotNull(automaton);
    }

   
    @Test
    void testGetWhitespaceAutomaton() {
        Automaton result = Datatypes.getWhitespaceAutomaton();
        assertNotNull(result);
    }

    @Test
    void testUnicodeBlockAutomata() {
        Automaton result = Datatypes.get("BasicLatin");
        assertNotNull(result);
        result = Datatypes.get("Latin-1Supplement");
        assertNotNull(result);
    }

    @Test
    void testUnicodeCategoryAutomata() {
        Automaton result = Datatypes.get("Lu");
        assertNotNull(result);
        result = Datatypes.get("Ll");
        assertNotNull(result);
    }

    @Test
    void testXMLNameAutomata() {
        Automaton result = Datatypes.get("NCName");
        assertNotNull(result);
        result = Datatypes.get("QName");
        assertNotNull(result);
    }

    @Test
    void testURINameAutomata() {
        Automaton result = Datatypes.get("URI");
        assertNotNull(result);
    }

    @Test
    void testXSDNameAutomata() {
        Automaton result = Datatypes.get("boolean");
        assertNotNull(result);
        result = Datatypes.get("decimal");
        assertNotNull(result);
    }

    @Test
    void testGetAutomaton() {
        String name = "whitespace";
        Automaton a = Datatypes.get(name);
        assertNotNull(a);
    }
    @Disabled
    @Test
    void testGetAutomatonNotExists() {
        String name = "nonExistent";
        Automaton a = Datatypes.get(name);
        assertNull(a);
    }

    @Test
    void testPutFrom() throws Exception {
        Map<String, Automaton> map = new HashMap<>();
        automaton = mock(Automaton.class);
        map.put("test", automaton);
        
        Method putFromMethod = Datatypes.class.getDeclaredMethod("putFrom", String.class, Map.class);
        putFromMethod.setAccessible(true);
        putFromMethod.invoke(null, "test", map);

        assertEquals(automaton, Datatypes.get("test"));
    }

    @Test
    void testStore() throws Exception {
        automaton = mock(Automaton.class);
        when(automaton.getNumberOfStates()).thenReturn(1);
        when(automaton.getNumberOfTransitions()).thenReturn(2);
        
        Method storeMethod = Datatypes.class.getDeclaredMethod("store", String.class, Automaton.class);
        storeMethod.setAccessible(true);
        storeMethod.invoke(null, "testStore", automaton);

        assertTrue(Datatypes.exists("testStore"));
    }

    @Test
    void testBuildMap() throws Exception {
        String[] exps = {"test", "[a-z]"};
        
        Method buildMapMethod = Datatypes.class.getDeclaredMethod("buildMap", String[].class);
        buildMapMethod.setAccessible(true);
        Map<String, Automaton> result = (Map<String, Automaton>) buildMapMethod.invoke(null, (Object) exps);

        assertNotNull(result);
        assertTrue(result.containsKey("test"));
    }

    @Test
    void testPutWith() throws Exception {
        String[] exps = {"test", "[a-z]"};
        Map<String, Automaton> use = new HashMap<>();
        use.put("a", Automaton.makeChar('a'));

        Method putWithMethod = Datatypes.class.getDeclaredMethod("putWith", String[].class, Map.class);
        putWithMethod.setAccessible(true);
        putWithMethod.invoke(null, exps, use);

        assertNotNull(Datatypes.get("test"));
    }

    @Test
    void testMakeCodePoint() throws Exception {
        Method makeCodePointMethod = Datatypes.class.getDeclaredMethod("makeCodePoint", int.class);
        makeCodePointMethod.setAccessible(true);
        Automaton result = (Automaton) makeCodePointMethod.invoke(null, 0x10000);

        assertNotNull(result);
    }
    @Test
    void testMakeCharRangeAutomaton() {
        automaton = Automaton.makeCharRange('a', 'z');
        assertNotNull(automaton);
    }

    @Test
    void testMakeCharAutomaton() {
        automaton = Automaton.makeChar('a');
        assertNotNull(automaton);
    }

    @Test
    void testMakeStringAutomaton() {
        automaton = Automaton.makeString("abc");
        assertNotNull(automaton);
    }
}