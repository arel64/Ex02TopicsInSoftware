package dk.brics.automaton;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class MinimizationOperationsTest {
    
    @Test
    public void testStatesAgreeWhenTransitionsDoNotOverlap() throws Exception {
        State s1 = new State();
        State s2 = new State();

        Transition t1 = new Transition('a', 'c', s1);
        Transition t2 = new Transition('d', 'f', s2);

        Transition[][] transitions = {
            { t1 },
            { t2 }
        };

        boolean[][] mark = new boolean[2][2];

        Method method = MinimizationOperations.class.getDeclaredMethod("statesAgree", Transition[][].class, boolean[][].class, int.class, int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(null, transitions, mark, 0, 1);

        assertTrue(result);
    }

    @Test
    public void testStatesAgreeWhenTransitionsOverlapAndMarked() throws Exception {
        State s1 = new State();
        s1.number = 0;
        State s2 = new State();
        s2.number = 1;
        State s3 = new State();
        s3.number = 2;

        Transition t1 = new Transition('a', 'e', s3); 
        Transition t2 = new Transition('c', 'g', s3);

        Transition[][] transitions = {
            { t1 },
            { t2 }
        };

        boolean[][] mark = new boolean[3][3];
        mark[2][2] = true;

        Method method = MinimizationOperations.class.getDeclaredMethod("statesAgree", Transition[][].class, boolean[][].class, int.class, int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(null, transitions, mark, 0, 1);

        assertFalse(result);
    }
    @Test
    public void testStatesAgreeWhenTransitionsOverlapAndNotMarked() throws Exception {
       
        State s1 = new State();
        s1.number = 2;

        Transition t1 = new Transition('a', 'e', s1);
        Transition t2 = new Transition('c', 'g', s1);

        Transition[][] transitions = {
            { t1 },
            { t2 }
        };

        boolean[][] mark = new boolean[3][3];

        Method method = MinimizationOperations.class.getDeclaredMethod("statesAgree", Transition[][].class, boolean[][].class, int.class, int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(null, transitions, mark, 0, 1);

        assertTrue(result);
    }

    @Test
    public void testStatesAgreeWithMultipleTransitionsAndNoOverlap() throws Exception {
        State s1 = new State();
        State s2 = new State();

        Transition t1a = new Transition('a', 'c', s1);
        Transition t1b = new Transition('e', 'g', s1);
        Transition t2a = new Transition('h', 'j', s2);
        Transition t2b = new Transition('l', 'n', s2);

        Transition[][] transitions = {
            { t1a, t1b },
            { t2a, t2b }
        };

        boolean[][] mark = new boolean[2][2];

        Method method = MinimizationOperations.class.getDeclaredMethod("statesAgree", Transition[][].class, boolean[][].class, int.class, int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(null, transitions, mark, 0, 1);

        assertTrue(result);
    }

    @Test
    public void testStatesAgreeWithMultipleTransitionsAndOverlap() throws Exception {
        State s1 = new State();
        State s2 = new State();
        State s3 = new State();
        s3.number = 2;

        Transition t1a = new Transition('a', 'c', s3);
        Transition t1b = new Transition('e', 'g', s1);
        Transition t2a = new Transition('b', 'd', s3);
        Transition t2b = new Transition('f', 'h', s2);

        Transition[][] transitions = {
            { t1a, t1b },
            { t2a, t2b }
        };

        boolean[][] mark = new boolean[3][3];
        mark[2][2] = true;

        Method method = MinimizationOperations.class.getDeclaredMethod("statesAgree", Transition[][].class, boolean[][].class, int.class, int.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(null, transitions, mark, 0, 1);

        assertFalse(result);
    }
      @Test
    public void testMinimizeHuffman() {
        Automaton automaton = new Automaton();
        State s1 = new State();
        State s2 = new State();
        State s3 = new State();
        State s4 = new State();
        
        s1.addTransition(new Transition('a', s2));
        s2.addTransition(new Transition('b', s3));
        s3.addTransition(new Transition('c', s4));
        s4.setAccept(true);

        automaton.setInitialState(s1);

        assertEquals(s1.getTransitions().size(), 1);
        assertEquals(s2.getTransitions().size(), 1);
        assertEquals(s3.getTransitions().size(), 1);
        assertEquals(s4.getTransitions().size(), 0);

        MinimizationOperations.minimizeHuffman(automaton);

        Set<State> states = automaton.getStates();
        assertEquals(states.size(), 4);

        assertTrue(states.contains(automaton.getInitialState()));

        State initialState = automaton.getInitialState();
        assertEquals(initialState.getTransitions().size(), 1);
        State nextState = initialState.getTransitions().iterator().next().to;
        assertEquals(nextState.getTransitions().size(), 1);
        State finalState = nextState.getTransitions().iterator().next().to;
        assertEquals(finalState.getTransitions().size(), 1);
        assertTrue(finalState.getTransitions().iterator().next().to.isAccept());
    }

    @Test
    public void testMinimizeHuffmanWithDifferentAcceptanceStates() {
        Automaton automaton = new Automaton();
        State s1 = new State();
        State s2 = new State();
        State s3 = new State();
        State s4 = new State();

        s1.addTransition(new Transition('a', s2));
        s2.addTransition(new Transition('b', s3));
        s3.addTransition(new Transition('c', s4));
        s2.setAccept(true);
        s4.setAccept(true);

        automaton.setInitialState(s1);

        MinimizationOperations.minimizeHuffman(automaton);

        Set<State> states = automaton.getStates();
        assertEquals(states.size(), 4);

        State initialState = automaton.getInitialState();
        assertEquals(initialState.getTransitions().size(), 1);
        State nextState = initialState.getTransitions().iterator().next().to;
        assertTrue(nextState.isAccept());
        assertEquals(nextState.getTransitions().size(), 1);
        State finalState = nextState.getTransitions().iterator().next().to;
        assertEquals(finalState.getTransitions().size(), 1);
        assertTrue(finalState.getTransitions().iterator().next().to.isAccept());
    }
    @Test
    public void testMinimizeSingletonAutomaton() {
        Automaton a = Automaton.makeString("singleton");
        MinimizationOperations.minimize(a);
        assertTrue(a.isSingleton());
    }

    @Test
    public void testMinimizeHuffmanAutomaton() {
        Automaton.setMinimization(Automaton.MINIMIZE_HUFFMAN);

        State s1 = new State();
        State s2 = new State();
        s1.setAccept(false);
        s2.setAccept(true);
        s1.addTransition(new Transition('a', s2));
        Automaton a = new Automaton();
        a.setInitialState(s1);

        MinimizationOperations.minimize(a);

        assertNotNull(a.getInitialState());
    }

    @Test
    public void testMinimizeBrzozowskiAutomaton() {
        Automaton.setMinimization(Automaton.MINIMIZE_BRZOZOWSKI);

        State s1 = new State();
        State s2 = new State();
        s1.setAccept(false);
        s2.setAccept(true);
        s1.addTransition(new Transition('a', s2));
        Automaton a = new Automaton();
        a.setInitialState(s1);

        MinimizationOperations.minimize(a);

        assertNotNull(a.getInitialState());
        assertEquals(a.getStates().size(), 2);
    }

    @Test
    public void testMinimizeValmariAutomaton() {
        Automaton.setMinimization(Automaton.MINIMIZE_VALMARI);

        State s1 = new State();
        State s2 = new State();
        s1.setAccept(false);
        s2.setAccept(true);
        s1.addTransition(new Transition('a', s2));
        Automaton a = new Automaton();
        a.setInitialState(s1);

        MinimizationOperations.minimize(a);

        assertNotNull(a.getInitialState());
        assertEquals(a.getStates().size(), 2);
    }

    @Test
    public void testMinimizeHopcroftAutomaton() {
        Automaton.setMinimization(Automaton.MINIMIZE_HOPCROFT);

        State s1 = new State();
        State s2 = new State();
        s1.setAccept(false);
        s2.setAccept(true);
        s1.addTransition(new Transition('a', s2));
        Automaton a = new Automaton();
        a.setInitialState(s1);

        MinimizationOperations.minimize(a);

        assertNotNull(a.getInitialState());
        assertEquals(a.getStates().size(), 2);
    }

    @Test
    public void testMinimizeAutomatonWithMultipleStates() {
        Automaton.setMinimization(Automaton.MINIMIZE_HOPCROFT);

        State s1 = new State();
        State s2 = new State();
        State s3 = new State();
        s1.setAccept(false);
        s2.setAccept(false);
        s3.setAccept(true);
        s1.addTransition(new Transition('a', s2));
        s2.addTransition(new Transition('b', s3));
        Automaton a = new Automaton();
        a.setInitialState(s1);

        MinimizationOperations.minimize(a);

        assertNotNull(a.getInitialState());
        assertEquals(a.getStates().size(), 3);
    }
    @Test
    public void testCompareLessThanFirstField() {
        MinimizationOperations.IntPair[] labels = {
            new MinimizationOperations.IntPair(1, 2),
            new MinimizationOperations.IntPair(3, 4)
        };
        MinimizationOperations.LabelComparator comparator = new MinimizationOperations.LabelComparator(labels);

        assertTrue(comparator.compare(0, 1) < 0);
    }

    @Test
    public void testCompareGreaterThanFirstField() {
        MinimizationOperations.IntPair[] labels = {
            new MinimizationOperations.IntPair(3, 4),
            new MinimizationOperations.IntPair(1, 2)
        };
        MinimizationOperations.LabelComparator comparator = new MinimizationOperations.LabelComparator(labels);

        assertTrue(comparator.compare(0, 1) > 0);
    }

    @Test
    public void testCompareLessThanSecondField() {
        MinimizationOperations.IntPair[] labels = {
            new MinimizationOperations.IntPair(1, 4),
            new MinimizationOperations.IntPair(1, 5)
        };
        MinimizationOperations.LabelComparator comparator = new MinimizationOperations.LabelComparator(labels);

        assertTrue(comparator.compare(0, 1) < 0);
    }

    @Test
    public void testCompareGreaterThanSecondField() {
        MinimizationOperations.IntPair[] labels = {
            new MinimizationOperations.IntPair(1, 5),
            new MinimizationOperations.IntPair(1, 4)
        };
        MinimizationOperations.LabelComparator comparator = new MinimizationOperations.LabelComparator(labels);

        assertTrue(comparator.compare(0, 1) > 0);
    }

    @Test
    public void testCompareEqual() {
        MinimizationOperations.IntPair[] labels = {
            new MinimizationOperations.IntPair(1, 2),
            new MinimizationOperations.IntPair(1, 2)
        };
        MinimizationOperations.LabelComparator comparator = new MinimizationOperations.LabelComparator(labels);

        assertEquals(0, comparator.compare(0, 1));
    }
    
}
