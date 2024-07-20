package dk.brics.automaton;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class BasicOperationsTest {

    @Test
    public void testConcatenateEmptyList() {
        List<Automaton> emptyList = new ArrayList<>();
        Automaton result = BasicOperations.concatenate(emptyList);
        assertTrue(result.isEmptyString());
    }

    @Test
    public void testConcatenateAllSingletons() {
        Automaton a1 = BasicAutomata.makeString("a");
        Automaton a2 = BasicAutomata.makeString("b");
        List<Automaton> list = Arrays.asList(a1, a2);
        Automaton result = BasicOperations.concatenate(list);
        assertTrue(result.isSingleton());
        assertEquals("ab", result.getSingleton());
    }
    @Test
    public void testMinusSingletonA2RunsA1Singleton() {
        Automaton a1 = BasicAutomata.makeString("a");
        Automaton a2 = mock(Automaton.class);
        when(a2.run("a")).thenReturn(true);
        Automaton result = BasicOperations.minus(a1, a2);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testMinusSingletonA2DoesNotRunA1Singleton() {
        Automaton a1 = BasicAutomata.makeString("a");
        Automaton a2 = mock(Automaton.class);
        when(a2.run("a")).thenReturn(false);
        Automaton result = BasicOperations.minus(a1, a2);
        assertFalse(result.isEmpty());
        assertTrue(result.isSingleton());
        assertEquals("a", result.getSingleton());
    }
    @Test
    public void testConcatenateWithEmptyAutomaton() {
        Automaton a1 = BasicAutomata.makeString("a");
        Automaton empty = BasicAutomata.makeEmpty();
        List<Automaton> list = Arrays.asList(a1, empty);
        Automaton result = BasicOperations.concatenate(list);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testConcatenateMixedAutomatons() {
        Automaton a1 = BasicAutomata.makeString("a");
        Automaton a2 = BasicAutomata.makeAnyString();
        List<Automaton> list = Arrays.asList(a1, a2);
        Automaton result = BasicOperations.concatenate(list);
        assertFalse(result.isEmpty());
        assertFalse(result.isSingleton());
    }

    @Test
    public void testRepeatWithMinZero() {
        Automaton a = BasicAutomata.makeString("a");
        Automaton result = BasicOperations.repeat(a, 0);
        assertFalse(result.isEmptyString());
    }

    @Test
    public void testRepeatMinGreaterThanMax() {
        Automaton a = BasicAutomata.makeString("a");
        Automaton result = BasicOperations.repeat(a, 3, 2);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testRepeatWithMinOne() {
        Automaton a = BasicAutomata.makeString("a");
        Automaton result = BasicOperations.repeat(a, 1);
        assertFalse(result.isEmpty());
        assertTrue(result.run("a"));
    }

    @Test
    public void testRepeatWithMinAndMax() {
        Automaton a = BasicAutomata.makeString("a");
        Automaton result = BasicOperations.repeat(a, 1, 3);
        assertFalse(result.isEmpty());
        assertTrue(result.run("a"));
        assertTrue(result.run("aa"));
        assertTrue(result.run("aaa"));
        assertFalse(result.run("aaaa"));
    }

    @Test
    public void testMinus() {
        Automaton a1 = BasicAutomata.makeString("a");
        Automaton a2 = BasicAutomata.makeString("b");
        Automaton result = BasicOperations.minus(a1, a2);
        assertFalse(result.isEmpty());
        assertTrue(result.run("a"));
        assertFalse(result.run("b"));
    }

    @Test
    public void testMinusSameAutomaton() {
        Automaton a1 = BasicAutomata.makeString("a");
        Automaton result = BasicOperations.minus(a1, a1);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testMinusWithEmpty() {
        Automaton a1 = BasicAutomata.makeString("a");
        Automaton a2 = BasicAutomata.makeEmpty();
        Automaton result = BasicOperations.minus(a1, a2);
        assertFalse(result.isEmpty());
        assertTrue(result.run("a"));
    }

    @Test
    public void testIntersection() {
        Automaton a1 = BasicAutomata.makeString("a");
        Automaton a2 = BasicAutomata.makeString("a");
        Automaton result = BasicOperations.intersection(a1, a2);
        assertFalse(result.isEmpty());
        assertTrue(result.run("a"));
    }

    @Test
    public void testIntersectionDifferentAutomatons() {
        Automaton a1 = BasicAutomata.makeString("a");
        Automaton a2 = BasicAutomata.makeString("b");
        Automaton result = BasicOperations.intersection(a1, a2);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testIntersectionWithEmpty() {
        Automaton a1 = BasicAutomata.makeString("a");
        Automaton a2 = BasicAutomata.makeEmpty();
        Automaton result = BasicOperations.intersection(a1, a2);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSubsetOfDifferentAutomatons() {
        Automaton a1 = BasicAutomata.makeString("a");
        Automaton a2 = BasicAutomata.makeString("a");
        assertTrue(BasicOperations.subsetOf(a1, a2));
    }

    @Test
    public void testSubsetOfNonSubset() {
        Automaton a1 = BasicAutomata.makeString("a");
        Automaton a2 = BasicAutomata.makeString("b");
        assertFalse(BasicOperations.subsetOf(a1, a2));
    }

    @Test
    public void testSubsetOfEmptyAutomaton() {
        Automaton a1 = BasicAutomata.makeEmpty();
        Automaton a2 = BasicAutomata.makeString("a");
        assertTrue(BasicOperations.subsetOf(a1, a2));
    }

    @Test
    public void testSubsetOfEmptyAutomatonWithNonEmpty() {
        Automaton a1 = BasicAutomata.makeString("a");
        Automaton a2 = BasicAutomata.makeEmpty();
        assertFalse(BasicOperations.subsetOf(a1, a2));
    }
    
    @Test
    public void testSubsetOfSingletonVsNonSingleton() {
        Automaton a1 = BasicAutomata.makeString("a");
        Automaton a2 = BasicAutomata.makeAnyChar();
        assertTrue(BasicOperations.subsetOf(a1, a2));
    }

    @Test
    public void testSubsetOfWithDifferentAcceptStates() {
        Automaton a1 = new Automaton();
        State s1 = new State();
        s1.setAccept(true);
        a1.setInitialState(s1);

        Automaton a2 = new Automaton();
        State s2 = new State();
        a2.setInitialState(s2);

        assertFalse(BasicOperations.subsetOf(a1, a2));
    }
    @Test
    public void testIntersectionA1EqualsA2() {
        Automaton a1 = mock(Automaton.class);
        when(a1.isSingleton()).thenReturn(false);
        when(a1.getStates()).thenReturn(new HashSet<State>());
        Automaton result = BasicOperations.intersection(a1, a1);
        assertNull(result);
        assertNotSame(a1, result);
    }
    @Test
    public void testSubsetOfSameAutomaton() {
        Automaton a1 = BasicAutomata.makeString("a");
        assertTrue(BasicOperations.subsetOf(a1, a1));
    }

    @Test
    public void testSubsetOfSingletonsEqual() {
        Automaton a1 = BasicAutomata.makeString("a");
        Automaton a2 = BasicAutomata.makeString("a");
        assertTrue(BasicOperations.subsetOf(a1, a2));
    }

    @Test
    public void testSubsetOfSingletonsNotEqual() {
        Automaton a1 = BasicAutomata.makeString("a");
        Automaton a2 = BasicAutomata.makeString("b");
        assertFalse(BasicOperations.subsetOf(a1, a2));
    }

    @Test
    public void testSubsetOfSingletonAndNonSingleton() {
        Automaton a1 = BasicAutomata.makeString("a");
        Automaton a2 = BasicAutomata.makeAnyString();
        assertTrue(BasicOperations.subsetOf(a1, a2));
    }

    @Test
    public void testSubsetOfNonSingletonsWithDifferentAcceptStates() {
        Automaton a1 = new Automaton();
        State s1 = new State();
        s1.setAccept(true);
        a1.setInitialState(s1);

        Automaton a2 = new Automaton();
        State s2 = new State();
        a2.setInitialState(s2);

        assertFalse(BasicOperations.subsetOf(a1, a2));
    }

    @Test
    public void testSubsetOfNonSingletonsWithTransitions() {
        Automaton a1 = new Automaton();
        State s1 = new State();
        State s1Dest = new State();
        s1Dest.setAccept(true);
        s1.addTransition(new Transition('a', 'b', s1Dest));
        a1.setInitialState(s1);

        Automaton a2 = new Automaton();
        State s2 = new State();
        State s2Dest = new State();
        s2Dest.setAccept(true);
        s2.addTransition(new Transition('a', 'b', s2Dest));
        a2.setInitialState(s2);

        assertTrue(BasicOperations.subsetOf(a1, a2));
    }

    @Test
    public void testSubsetOfNonSingletonsWithDifferentTransitions() {
        Automaton a1 = new Automaton();
        State s1 = new State();
        State s1Dest = new State();
        s1Dest.setAccept(true);
        s1.addTransition(new Transition('a', 'b', s1Dest));
        a1.setInitialState(s1);

        Automaton a2 = new Automaton();
        State s2 = new State();
        State s2Dest = new State();
        s2Dest.setAccept(true);
        s2.addTransition(new Transition('c', 'd', s2Dest));
        a2.setInitialState(s2);

        assertFalse(BasicOperations.subsetOf(a1, a2));
    }

    
    @Test
    public void testSubsetOfComplexAutomatons() {
        Automaton a1 = new Automaton();
        State s1 = new State();
        State s1Dest = new State();
        s1Dest.setAccept(true);
        s1.addTransition(new Transition('a', 'b', s1Dest));
        a1.setInitialState(s1);

        Automaton a2 = new Automaton();
        State s2 = new State();
        State s2Dest = new State();
        s2Dest.setAccept(true);
        s2.addTransition(new Transition('a', 'b', s2Dest));
        s2.addTransition(new Transition('b', 'c', new State()));
        a2.setInitialState(s2);

        assertFalse(BasicOperations.subsetOf(a1, a2));
    }

}
