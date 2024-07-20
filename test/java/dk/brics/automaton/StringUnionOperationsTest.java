package dk.brics.automaton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class StringUnionOperationsTest {

    private StringUnionOperations stringUnionOperations;
    private StringUnionOperations.State state;
    private StringUnionOperations.State anotherState;

    @BeforeEach
    public void setUp() {
        stringUnionOperations = new StringUnionOperations();
        state = new StringUnionOperations.State();
        anotherState = new StringUnionOperations.State();
    }

    @Test
    public void testCompareLexicographicOrder() {
        CharSequence s1 = "abc";
        CharSequence s2 = "abd";
        assertTrue(StringUnionOperations.LEXICOGRAPHIC_ORDER.compare(s1, s2) < 0);
        assertTrue(StringUnionOperations.LEXICOGRAPHIC_ORDER.compare(s2, s1) > 0);
        assertEquals(0, StringUnionOperations.LEXICOGRAPHIC_ORDER.compare(s1, "abc"));
    }

    @Test
    public void testStateGetState() {
        state.newState('a');
        assertNotNull(state.getState('a'));
        assertNull(state.getState('b'));
    }

    @Test
    public void testStateGetTransitionLabels() {
        state.newState('a');
        assertArrayEquals(new char[]{'a'}, state.getTransitionLabels());
    }

    @Test
    public void testStateGetStates() {
        state.newState('a');
        assertEquals(1, state.getStates().length);
    }

    @Test
    public void testStateEquals() {
        state.newState('a');
        anotherState.newState('a');
        assertFalse(state.equals(anotherState));

        anotherState.newState('b');
        assertFalse(state.equals(anotherState));
    }
    @Test
    public void testStateEqualsFinal() {
        state.newState('a');
        assertTrue(state.equals(state));
    }
    @Test
    public void testStateHasChildren() {
        assertFalse(state.hasChildren());
        state.newState('a');
        assertTrue(state.hasChildren());
    }

    @Test
    public void testStateIsFinal() {
        state.is_final = true;
        assertTrue(state.isFinal());

        state.is_final = false;
        assertFalse(state.isFinal());
    }

    @Test
    public void testStateHashCode() {
        StringUnionOperations.State state1 = new StringUnionOperations.State();
        StringUnionOperations.State state2 = new StringUnionOperations.State();

        // Both have the same code as they are LITERALY the same element
        assertEquals(state1.hashCode(), state2.hashCode());

        
        state1.newState('a');
        state2.newState('a');
        assertNotEquals(state1.hashCode(), state2.hashCode());
        
        state2.newState('b');
        assertNotEquals(state1.hashCode(), state2.hashCode());

        state1.is_final = true;
        assertNotEquals(state1.hashCode(), state2.hashCode());
    }

    @Test
    public void testStateNewState() {
        StringUnionOperations.State newState = state.newState('a');
        assertNotNull(newState);
        assertEquals('a', state.labels[0]);
    }

    @Test
    public void testStateLastChild() {
        state.newState('a');
        assertEquals(state.getState('a'), state.lastChild());
    }

    @Test
    public void testStateLastChildWithLabel() {
        state.newState('a');
        assertEquals(state.getState('a'), state.lastChild('a'));
        assertNull(state.lastChild('b'));
    }

    @Test
    public void testStateReplaceLastChild() {
        StringUnionOperations.State newState = new StringUnionOperations.State();
        state.newState('a');
        state.replaceLastChild(newState);
        assertEquals(newState, state.lastChild());
    }

    @Test
    public void testComplete() {
        stringUnionOperations.add("abc");
        StringUnionOperations.State rootState = stringUnionOperations.complete();
        assertNotNull(rootState);
    }

    @Test
    public void testCompleteWhenRegisterIsNull() {
        // Case 1: When `register` is null, an IllegalStateException should be thrown
        stringUnionOperations.add("abc");
        stringUnionOperations.complete();
        assertThrows(IllegalStateException.class, () -> stringUnionOperations.complete());
    }

    @Test
    public void testCompleteWhenRootHasChildren() {
        // Case 2: When `root` has children
        stringUnionOperations.add("abc");
        StringUnionOperations.State rootStateWithChildren = stringUnionOperations.complete();
        assertNotNull(rootStateWithChildren);
        assertTrue(rootStateWithChildren.hasChildren());
    }

    @Test
    public void testCompleteWhenRootHasNoChildren() {
        // Case 3: When `root` does not have children
        StringUnionOperations.State rootStateWithoutChildren = stringUnionOperations.complete();
        assertNotNull(rootStateWithoutChildren);
        assertFalse(rootStateWithoutChildren.hasChildren());
    }
    @Test
    public void testBuild() {
        CharSequence[] input = {"abc", "abcd", "abce"};
        assertNotNull(StringUnionOperations.build(input));
    }

    @Test
    public void testReplaceOrRegister() {
        stringUnionOperations.add("abc");
        StringUnionOperations.State rootState = stringUnionOperations.complete();
        assertNotNull(rootState);
    }
    @Test
    public void testAddWithSingleCharacter() {
        stringUnionOperations.add("a");
        assertNotNull(stringUnionOperations.complete());
    }
    
    @Test
    public void testAddWithMultipleCharacters() {
        stringUnionOperations.add("abc");
        assertNotNull(stringUnionOperations.complete());
    }

    @Test
    public void testAddWithCommonPrefix() {
        stringUnionOperations.add("abc");
        stringUnionOperations.add("abcd");
        assertNotNull(stringUnionOperations.complete());
    }

    @Test()
    public void testAddWithEmptyString() {
        assertThrows(AssertionError.class, () -> {
            stringUnionOperations.add("");
        });
    }

    @Test()
    public void testAddWithUnsortedInput() {
        assertThrows(AssertionError.class, () -> {
            stringUnionOperations.add("b");
            stringUnionOperations.add("a");
        });
    }

    @Test
    public void testAddWithSortedInput() {
        stringUnionOperations.add("a");
        stringUnionOperations.add("b");
        stringUnionOperations.add("c");
        assertNotNull(stringUnionOperations.complete());
    }

    @Test
    public void testAddWithMultipleCommonPrefixes() {
        stringUnionOperations.add("abc");
        stringUnionOperations.add("abx");
        stringUnionOperations.add("abxy");
        stringUnionOperations.add("abxyz");
        assertNotNull(stringUnionOperations.complete());
    }

    @Test
    public void testAddWithSingleCharacterMultipleTimes() {
        stringUnionOperations.add("a");
        stringUnionOperations.add("a");
        stringUnionOperations.add("a");
        assertNotNull(stringUnionOperations.complete());
    }

    @Test
    public void testAddWithCommonSuffix() {
        stringUnionOperations.add("abc");
        stringUnionOperations.add("xbc");
        assertNotNull(stringUnionOperations.complete());
    }    
    @Test()
    public void testAddAfterComplete() {
        assertThrows(AssertionError.class,()->{
            stringUnionOperations.add("a");
            stringUnionOperations.complete();
            stringUnionOperations.add("b");
        });
        
    }
}
