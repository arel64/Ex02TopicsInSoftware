package dk.brics.automaton;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SpecialOperationsTest {
    
    private Automaton automaton;
    private State initialState;
    private State acceptState;

    @BeforeEach
    public void setUp() {
        automaton = new Automaton();
        initialState = new State();
        acceptState = new State();
        acceptState.setAccept(true);
        automaton.setInitialState(initialState);
    }
    @Disabled
    @Test
    public void testReverse() {
        initialState.addTransition(new Transition('a', acceptState));
        acceptState.addTransition(new Transition('b', initialState));

        Set<State> acceptStates = SpecialOperations.reverse(automaton);
        assertTrue(acceptStates.contains(acceptState));
        assertTrue(initialState.isAccept());
        assertFalse(acceptState.isAccept());

        State newInitial = automaton.getInitialState();
        assertNotEquals(initialState, newInitial);
        assertTrue(newInitial.getTransitions().contains(new Transition('b', acceptState)));
    }

    @Test
    public void testOverlap() {
        Automaton a1 = BasicAutomata.makeString("ab");
        Automaton a2 = BasicAutomata.makeString("bc");
        Automaton overlapAutomaton = SpecialOperations.overlap(a1, a2);

        assertFalse(overlapAutomaton.run("ab"));
        assertFalse(overlapAutomaton.run("bc"));
    }

    @Test
    public void testSingleChars() {
        initialState.addTransition(new Transition('a', acceptState));
        initialState.addTransition(new Transition('b', acceptState));
        initialState.addTransition(new Transition('c', acceptState));

        Automaton singleCharsAutomaton = SpecialOperations.singleChars(automaton);

        assertTrue(singleCharsAutomaton.run("a"));
        assertTrue(singleCharsAutomaton.run("b"));
        assertTrue(singleCharsAutomaton.run("c"));
        assertFalse(singleCharsAutomaton.run("d"));
    }

    @Test
    public void testTrim() {
        initialState.addTransition(new Transition('a', acceptState));
        acceptState.addTransition(new Transition(' ', acceptState));
        
        Automaton trimmedAutomaton = SpecialOperations.trim(automaton, " ", ' ');

        assertTrue(trimmedAutomaton.run(" a"));
        assertTrue(trimmedAutomaton.run("a "));
        assertTrue(trimmedAutomaton.run(" a "));
        assertFalse(trimmedAutomaton.run("b"));
    }

    @Test
    public void testCompress() {
        initialState.addTransition(new Transition('a', acceptState));
        acceptState.addTransition(new Transition(' ', acceptState));
        
        Automaton compressedAutomaton = SpecialOperations.compress(automaton, " ", ' ');

        assertTrue(compressedAutomaton.run("a"));
        assertTrue(compressedAutomaton.run("a "));
        assertTrue(compressedAutomaton.run("a  "));
        assertFalse(compressedAutomaton.run("b"));
    }

    @Test
    public void testSubstWithMap() {
        Map<Character, Set<Character>> map = new HashMap<>();
        map.put('a', new HashSet<>(Arrays.asList('x', 'y', 'z')));
        
        initialState.addTransition(new Transition('a', acceptState));
        
        Automaton substAutomaton = SpecialOperations.subst(automaton, map);

        assertTrue(substAutomaton.run("x"));
        assertTrue(substAutomaton.run("y"));
        assertTrue(substAutomaton.run("z"));
        assertFalse(substAutomaton.run("a"));
    }

    @Test
    public void testSubstWithCharAndString() {
        initialState.addTransition(new Transition('a', acceptState));
        
        Automaton substAutomaton = SpecialOperations.subst(automaton, 'a', "xyz");

        assertTrue(substAutomaton.run("xyz"));
        assertFalse(substAutomaton.run("a"));
        assertFalse(substAutomaton.run("xy"));
    }

    @Test
    public void testHomomorph() {
        initialState.addTransition(new Transition('a', acceptState));
        char[] source = {'a'};
        char[] dest = {'b'};

        Automaton homomorphAutomaton = SpecialOperations.homomorph(automaton, source, dest);

        assertTrue(homomorphAutomaton.run("b"));
        assertFalse(homomorphAutomaton.run("a"));
    }
    @Disabled
    @Test
    public void testProjectChars() {
        initialState.addTransition(new Transition('a', acceptState));
        initialState.addTransition(new Transition('b', acceptState));
        Set<Character> chars = new HashSet<>(Collections.singletonList('a'));

        Automaton projectedAutomaton = SpecialOperations.projectChars(automaton, chars);

        assertTrue(projectedAutomaton.run("a"));
        assertFalse(projectedAutomaton.run("b"));
    }

    @Test
    public void testIsFinite() {
        assertTrue(SpecialOperations.isFinite(BasicAutomata.makeString("a")));
        assertFalse(SpecialOperations.isFinite(BasicAutomata.makeAnyString()));
    }

    @Test
    public void testGetStrings() {
        initialState.addTransition(new Transition('a', acceptState));
        initialState.addTransition(new Transition('b', acceptState));

        Set<String> strings = SpecialOperations.getStrings(automaton, 1);

        assertTrue(strings.contains("a"));
        assertTrue(strings.contains("b"));
        assertFalse(strings.contains("c"));
    }

    @Test
    public void testGetFiniteStrings() {
        initialState.addTransition(new Transition('a', acceptState));
        initialState.addTransition(new Transition('b', acceptState));

        Set<String> strings = SpecialOperations.getFiniteStrings(automaton);

        assertTrue(strings.contains("a"));
        assertTrue(strings.contains("b"));
        assertFalse(strings.contains("c"));
    }

    @Test
    public void testGetCommonPrefix() {
        Automaton a = BasicAutomata.makeString("abc");
        assertEquals("abc", SpecialOperations.getCommonPrefix(a));

        Automaton b = BasicAutomata.makeStringUnion("abc", "abcd");
        assertEquals("abc", SpecialOperations.getCommonPrefix(b));
    }

    @Test
    public void testPrefixClose() {
        initialState.addTransition(new Transition('a', acceptState));

        SpecialOperations.prefixClose(automaton);

        assertTrue(initialState.isAccept());
        assertTrue(automaton.run("a"));
    }
    @Disabled
    @Test
    public void testHexCases() {
        Automaton a = BasicAutomata.makeCharSet("abcABC");
        Automaton hexAutomaton = SpecialOperations.hexCases(a);

        assertTrue(hexAutomaton.run("a"));
        assertTrue(hexAutomaton.run("A"));
        assertTrue(hexAutomaton.run("b"));
        assertTrue(hexAutomaton.run("B"));
        assertTrue(hexAutomaton.run("c"));
        assertTrue(hexAutomaton.run("C"));
    }

    @Test
    public void testReplaceWhitespace() {
        initialState.addTransition(new Transition(' ', acceptState));
        
        Automaton wsAutomaton = SpecialOperations.replaceWhitespace(automaton);

        assertTrue(wsAutomaton.run(" "));
        assertTrue(wsAutomaton.run("\t"));
        assertTrue(wsAutomaton.run("\n"));
        assertTrue(wsAutomaton.run("\r"));
    }
    @Test
    public void testSingleCharsWithSingletonAutomaton() {
        Automaton a = BasicAutomata.makeString("abc");
        Automaton result = SpecialOperations.singleChars(a);

        assertFalse(result.isSingleton());
        assertTrue(result.isDeterministic());
        assertFalse(result.isEmpty());

        assertEquals(2, result.getNumberOfStates());
        assertTrue(result.run("a"));
        assertTrue(result.run("b"));
        assertTrue(result.run("c"));
        assertFalse(result.run("ab"));
        assertFalse(result.run("ac"));
        assertFalse(result.run("bc"));
    }
    @Test
    public void testSubstWithEmptyMap() {
        Automaton a = BasicAutomata.makeString("abc");
        Map<Character, Set<Character>> map = new HashMap<>();
        Automaton result = SpecialOperations.subst(a, map);
        assertEquals(a, result);
    }
    
  
}
