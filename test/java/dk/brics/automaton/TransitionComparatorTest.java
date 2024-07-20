package dk.brics.automaton;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Comparator;
import org.junit.jupiter.api.Test;
public class TransitionComparatorTest {
    @Test
    public void testCompare_ToFirst_True_T1LessThanT2() {
        State state1 = new State();
        state1.number = 1;
        State state2 = new State();
        state2.number = 2;

        Transition t1 = new Transition('a', 'c', state1);
        Transition t2 = new Transition('a', 'c', state2);

        Comparator<Transition> comparator = new TransitionComparator(true);
        assertEquals(-1, comparator.compare(t1, t2));
    }

    @Test
    public void testCompare_ToFirst_True_T1GreaterThanT2() {
        State state1 = new State();
        state1.number = 2;
        State state2 = new State();
        state2.number = 1;

        Transition t1 = new Transition('a', 'c', state1);
        Transition t2 = new Transition('a', 'c', state2);

        Comparator<Transition> comparator = new TransitionComparator(true);
        assertEquals(1, comparator.compare(t1, t2));
    }

    @Test
    public void testCompare_ToFirst_True_SameTo_MinDifferent() {
        State state = new State();

        Transition t1 = new Transition('a', 'c', state);
        Transition t2 = new Transition('b', 'c', state);

        Comparator<Transition> comparator = new TransitionComparator(true);
        assertEquals(-1, comparator.compare(t1, t2));
    }

    @Test
    public void testCompare_ToFirst_True_SameTo_MinSame_MaxDifferent() {
        State state = new State();

        Transition t1 = new Transition('a', 'd', state);
        Transition t2 = new Transition('a', 'c', state);

        Comparator<Transition> comparator = new TransitionComparator(true);
        assertEquals(-1, comparator.compare(t1, t2));
    }

    @Test
    public void testCompare_ToFirst_False_T1LessThanT2() {
        State state1 = new State();
        state1.number = 1;
        State state2 = new State();
        state2.number = 2;

        Transition t1 = new Transition('a', 'c', state1);
        Transition t2 = new Transition('a', 'c', state2);

        Comparator<Transition> comparator = new TransitionComparator(false);
        assertEquals(-1, comparator.compare(t1, t2));
    }

    @Test
    public void testCompare_ToFirst_False_T1GreaterThanT2() {
        State state1 = new State();
        state1.number = 2;
        State state2 = new State();
        state2.number = 1;

        Transition t1 = new Transition('a', 'c', state1);
        Transition t2 = new Transition('a', 'c', state2);

        Comparator<Transition> comparator = new TransitionComparator(false);
        assertEquals(1, comparator.compare(t1, t2));
    }

    @Test
    public void testCompare_ToFirst_False_SameTo_MinDifferent() {
        State state = new State();

        Transition t1 = new Transition('a', 'c', state);
        Transition t2 = new Transition('b', 'c', state);

        Comparator<Transition> comparator = new TransitionComparator(false);
        assertEquals(-1, comparator.compare(t1, t2));
    }

    @Test
    public void testCompare_ToFirst_False_SameTo_MinSame_MaxDifferent() {
        State state = new State();

        Transition t1 = new Transition('a', 'd', state);
        Transition t2 = new Transition('a', 'c', state);

        Comparator<Transition> comparator = new TransitionComparator(false);
        assertEquals(-1, comparator.compare(t1, t2));
    }

    @Test
    public void testCompare_ToFirst_True_T1ToNull() {
        State state = new State();

        Transition t1 = new Transition('a', 'c', null);
        Transition t2 = new Transition('a', 'c', state);

        Comparator<Transition> comparator = new TransitionComparator(true);
        assertEquals(-1, comparator.compare(t1, t2));
    }

    @Test
    public void testCompare_ToFirst_True_T2ToNull() {
        State state = new State();

        Transition t1 = new Transition('a', 'c', state);
        Transition t2 = new Transition('a', 'c', null);

        Comparator<Transition> comparator = new TransitionComparator(true);
        assertEquals(1, comparator.compare(t1, t2));
    }

    @Test
    public void testCompare_ToFirst_False_T1ToNull() {
        State state = new State();

        Transition t1 = new Transition('a', 'c', null);
        Transition t2 = new Transition('a', 'c', state);

        Comparator<Transition> comparator = new TransitionComparator(false);
        assertEquals(-1, comparator.compare(t1, t2));
    }

    @Test
    public void testCompare_ToFirst_False_T2ToNull() {
        State state = new State();

        Transition t1 = new Transition('a', 'c', state);
        Transition t2 = new Transition('a', 'c', null);

        Comparator<Transition> comparator = new TransitionComparator(false);
        assertEquals(1, comparator.compare(t1, t2));
    }

    @Test
    public void testCompare_ToFirst_True_AllSame() {
        State state = new State();

        Transition t1 = new Transition('a', 'c', state);
        Transition t2 = new Transition('a', 'c', state);

        Comparator<Transition> comparator = new TransitionComparator(true);
        assertEquals(0, comparator.compare(t1, t2));
    }

    @Test
    public void testCompare_ToFirst_False_AllSame() {
        State state = new State();

        Transition t1 = new Transition('a', 'c', state);
        Transition t2 = new Transition('a', 'c', state);

        Comparator<Transition> comparator = new TransitionComparator(false);
        assertEquals(0, comparator.compare(t1, t2));
    }
}


