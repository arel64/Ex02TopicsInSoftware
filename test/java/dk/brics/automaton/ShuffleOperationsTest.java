package dk.brics.automaton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import dk.brics.automaton.ShuffleOperations.ShuffleConfiguration;

public class ShuffleOperationsTest {

    @Test
    public void testShuffleBasic() {
        Automaton a1 = new Automaton();
        Automaton a2 = new Automaton();
        
        State s1a = new State();
        State s1b = new State();
        State s2a = new State();
        State s2b = new State();
        
        s1b.setAccept(true);
        s2b.setAccept(true);
        
        a1.setInitialState(s1a);
        a2.setInitialState(s2a);
        
        s1a.addTransition(new Transition('a', s1b));
        s2a.addTransition(new Transition('b', s2b));
        
        Automaton result = ShuffleOperations.shuffle(a1, a2);
        
        assertNotNull(result);
        assertFalse(result.isDeterministic());
        assertTrue(result.run("ab"));
        assertTrue(result.run("ba"));
    }
    @Test
    public void testShuffleEmptyAutomaton() {
        Automaton a1 = new Automaton();
        Automaton a2 = new Automaton();
        
        Automaton result = ShuffleOperations.shuffle(a1, a2);
        
        assertNotNull(result);
        assertFalse(result.run("a"));
        assertFalse(result.run("b"));
    }

  

    @Test
    public void testShuffleSubsetOfSingleAutomaton() {
        Automaton a1 = new Automaton();
        State s1a = new State();
        State s1b = new State();
        
        s1b.setAccept(true);
        
        a1.setInitialState(s1a);
        s1a.addTransition(new Transition('a', s1b));
        
        Automaton a = new Automaton();
        State sa = new State();
        State sb = new State();
        
        sb.setAccept(true);
        
        a.setInitialState(sa);
        sa.addTransition(new Transition('b', sb));
        
        String result = ShuffleOperations.shuffleSubsetOf(Collections.singletonList(a1), a, null, null);
        
        assertNotNull(result);
        assertEquals("a", result);
    }

    @Test
    public void testShuffleSubsetOfMultipleAutomata() {
        Automaton a1 = new Automaton();
        Automaton a2 = new Automaton();
        
        State s1a = new State();
        State s1b = new State();
        State s2a = new State();
        State s2b = new State();
        
        s1b.setAccept(true);
        s2b.setAccept(true);
        
        a1.setInitialState(s1a);
        a2.setInitialState(s2a);
        
        s1a.addTransition(new Transition('a', s1b));
        s2a.addTransition(new Transition('b', s2b));
        
        Automaton a = new Automaton();
        State sa = new State();
        State sb = new State();
        
        sb.setAccept(true);
        
        a.setInitialState(sa);
        sa.addTransition(new Transition('c', sb));
        
        String result = ShuffleOperations.shuffleSubsetOf(Arrays.asList(a1, a2), a, null, null);
        
        assertNotNull(result);
        assertEquals("ab", result);
    }

    @Test
    public void testShuffleSubsetOfEmptyAutomaton() {
        Automaton a = new Automaton();
        String result = ShuffleOperations.shuffleSubsetOf(new ArrayList<Automaton>(), a, null, null);
        
        assertNull(result);
    }

    @Test
    public void testShuffleWithSuspendResume() {
        Automaton a1 = new Automaton();
        State s1a = new State();
        State s1b = new State();
        
        s1b.setAccept(true);
        
        a1.setInitialState(s1a);
        s1a.addTransition(new Transition('a', s1b));
        
        Automaton a = new Automaton();
        State sa = new State();
        State sb = new State();
        
        sb.setAccept(true);
        
        a.setInitialState(sa);
        sa.addTransition(new Transition('b', sb));
        
        String result = ShuffleOperations.shuffleSubsetOf(Collections.singletonList(a1), a, 'a', 'b');
        
        assertNotNull(result);
        assertEquals("a", result);
    }
    @Test
    public void testAddBasicTransition() throws Exception {
        State s1 = new State();
        State s2 = new State();
        State s3 = new State();
        Transition t1 = new Transition('a', 'd', s2);
        Transition t2 = new Transition('b', 'e', s3);

        LinkedList<ShuffleConfiguration> pending = new LinkedList<>();
        Set<ShuffleConfiguration> visited = new HashSet<>();
        ShuffleConfiguration c = new ShuffleConfiguration(Arrays.asList(new Automaton(), new Automaton()), new Automaton());

        Method method = ShuffleOperations.class.getDeclaredMethod("add", Character.class, Character.class, LinkedList.class, Set.class, ShuffleConfiguration.class, int.class, Transition.class, Transition.class, char.class, char.class);
        method.setAccessible(true);
        method.invoke(null, null, null, pending, visited, c, 0, t1, t2, 'a', 'b');

        assertFalse(pending.isEmpty());
        assertFalse(visited.isEmpty());
    }

    @Test
    public void testAddWithSuspendShuffle() throws Exception {
        State s1 = new State();
        State s2 = new State();
        State s3 = new State();
        Transition t1 = new Transition('a', 'd', s2);
        Transition t2 = new Transition('b', 'e', s3);

        LinkedList<ShuffleConfiguration> pending = new LinkedList<>();
        Set<ShuffleConfiguration> visited = new HashSet<>();
        ShuffleConfiguration c = new ShuffleConfiguration(Arrays.asList(new Automaton(), new Automaton()), new Automaton());

        Method method = ShuffleOperations.class.getDeclaredMethod("add", Character.class, Character.class, LinkedList.class, Set.class, ShuffleConfiguration.class, int.class, Transition.class, Transition.class, char.class, char.class);
        method.setAccessible(true);
        method.invoke(null, 'c', null, pending, visited, c, 0, t1, t2, 'a', 'd');

        assertFalse(pending.isEmpty());
        assertFalse(visited.isEmpty());
    }

    @Test
    public void testAddWithResumeShuffle() throws Exception {
        State s1 = new State();
        State s2 = new State();
        State s3 = new State();
        Transition t1 = new Transition('a', 'd', s2);
        Transition t2 = new Transition('b', 'e', s3);

        LinkedList<ShuffleConfiguration> pending = new LinkedList<>();
        Set<ShuffleConfiguration> visited = new HashSet<>();
        ShuffleConfiguration c = new ShuffleConfiguration(Arrays.asList(new Automaton(), new Automaton()), new Automaton());

        Method method = ShuffleOperations.class.getDeclaredMethod("add", Character.class, Character.class, LinkedList.class, Set.class, ShuffleConfiguration.class, int.class, Transition.class, Transition.class, char.class, char.class);
        method.setAccessible(true);
        method.invoke(null, null, 'd', pending, visited, c, 0, t1, t2, 'a', 'd');

        assertFalse(pending.isEmpty());
        assertFalse(visited.isEmpty());
    }

    @Test
    public void testAddWithSurrogate() throws Exception {
        State s1 = new State();
        State s2 = new State();
        State s3 = new State();
        Transition t1 = new Transition('a', '\uD800', s2);
        Transition t2 = new Transition('\uD800', 'e', s3);

        LinkedList<ShuffleConfiguration> pending = new LinkedList<>();
        Set<ShuffleConfiguration> visited = new HashSet<>();
        ShuffleConfiguration c = new ShuffleConfiguration(Arrays.asList(new Automaton(), new Automaton()), new Automaton());

        Method method = ShuffleOperations.class.getDeclaredMethod("add", Character.class, Character.class, LinkedList.class, Set.class, ShuffleConfiguration.class, int.class, Transition.class, Transition.class, char.class, char.class);
        method.setAccessible(true);
        method.invoke(null, null, null, pending, visited, c, 0, t1, t2, 'a', '\uDBFF');

        assertFalse(pending.isEmpty());
        assertFalse(visited.isEmpty());
    }

    @Test
    public void testAddWithSurrogateEdgeCase() throws Exception {
        State s1 = new State();
        State s2 = new State();
        State s3 = new State();
        Transition t1 = new Transition('a', '\uD800', s2);
        Transition t2 = new Transition('\uD800', '\uDBFF', s3);

        LinkedList<ShuffleConfiguration> pending = new LinkedList<>();
        Set<ShuffleConfiguration> visited = new HashSet<>();
        ShuffleConfiguration c = new ShuffleConfiguration(Arrays.asList(new Automaton(), new Automaton()), new Automaton());

        Method method = ShuffleOperations.class.getDeclaredMethod("add", Character.class, Character.class, LinkedList.class, Set.class, ShuffleConfiguration.class, int.class, Transition.class, Transition.class, char.class, char.class);
        method.setAccessible(true);
        method.invoke(null, null, null, pending, visited, c, 0, t1, t2, '\uD800', '\uDBFF');

        assertFalse(pending.isEmpty());
        assertFalse(visited.isEmpty());
    }
    @Test
    public void testAddWithSuspendShuffleMinLessThanSuspend() throws Exception {
        State s1 = new State();
        State s2 = new State();
        State s3 = new State();
        Transition t1 = new Transition('a', 'd', s2);
        Transition t2 = new Transition('b', 'e', s3);

        LinkedList<ShuffleConfiguration> pending = new LinkedList<>();
        Set<ShuffleConfiguration> visited = new HashSet<>();
        ShuffleConfiguration c = new ShuffleConfiguration(Arrays.asList(new Automaton(), new Automaton()), new Automaton());

        Method method = ShuffleOperations.class.getDeclaredMethod("add", Character.class, Character.class, LinkedList.class, Set.class, ShuffleConfiguration.class, int.class, Transition.class, Transition.class, char.class, char.class);
        method.setAccessible(true);
        method.invoke(null, 'c', null, pending, visited, c, 0, t1, t2, 'a', 'd');

        assertFalse(pending.isEmpty());
        assertFalse(visited.isEmpty());
    }
    @Test
    public void testAddWithSuspendShuffleLessThanMax() throws Exception {
        State s1 = new State();
        State s2 = new State();
        State s3 = new State();
        Transition t1 = new Transition('a', 'd', s2);
        Transition t2 = new Transition('b', 'e', s3);

        LinkedList<ShuffleConfiguration> pending = new LinkedList<>();
        Set<ShuffleConfiguration> visited = new HashSet<>();
        ShuffleConfiguration c = new ShuffleConfiguration(Arrays.asList(new Automaton(), new Automaton()), new Automaton());

        Method method = ShuffleOperations.class.getDeclaredMethod("add", Character.class, Character.class, LinkedList.class, Set.class, ShuffleConfiguration.class, int.class, Transition.class, Transition.class, char.class, char.class);
        method.setAccessible(true);
        method.invoke(null, 'c', null, pending, visited, c, 0, t1, t2, 'a', 'd');

        assertFalse(pending.isEmpty());
        assertFalse(visited.isEmpty());
    }
    @Test
    public void testAddWithResumeShuffleMinLessThanResume() throws Exception {
        State s1 = new State();
        State s2 = new State();
        State s3 = new State();
        Transition t1 = new Transition('a', 'd', s2);
        Transition t2 = new Transition('b', 'e', s3);

        LinkedList<ShuffleConfiguration> pending = new LinkedList<>();
        Set<ShuffleConfiguration> visited = new HashSet<>();
        ShuffleConfiguration c = new ShuffleConfiguration(Arrays.asList(new Automaton(), new Automaton()), new Automaton());

        Method method = ShuffleOperations.class.getDeclaredMethod("add", Character.class, Character.class, LinkedList.class, Set.class, ShuffleConfiguration.class, int.class, Transition.class, Transition.class, char.class, char.class);
        method.setAccessible(true);
        method.invoke(null, null, 'd', pending, visited, c, 0, t1, t2, 'a', 'd');

        assertFalse(pending.isEmpty());
        assertFalse(visited.isEmpty());
    }

    @Test
    public void testAddWithHighSurrogateBegin() throws Exception {
        State s1 = new State();
        State s2 = new State();
        State s3 = new State();
        Transition t1 = new Transition('a', '\uD800', s2);
        Transition t2 = new Transition('\uD800', 'e', s3);

        LinkedList<ShuffleConfiguration> pending = new LinkedList<>();
        Set<ShuffleConfiguration> visited = new HashSet<>();
        ShuffleConfiguration c = new ShuffleConfiguration(Arrays.asList(new Automaton(), new Automaton()), new Automaton());

        Method method = ShuffleOperations.class.getDeclaredMethod("add", Character.class, Character.class, LinkedList.class, Set.class, ShuffleConfiguration.class, int.class, Transition.class, Transition.class, char.class, char.class);
        method.setAccessible(true);
        method.invoke(null, null, null, pending, visited, c, 0, t1, t2, 'a', '\uD800');

        assertFalse(pending.isEmpty());
        assertFalse(visited.isEmpty());
    }
    @Test
    public void testAddWithHighSurrogateEnd() throws Exception {
        State s1 = new State();
        State s2 = new State();
        State s3 = new State();
        Transition t1 = new Transition('a', '\uD800', s2);
        Transition t2 = new Transition('\uD800', '\uDBFF', s3);

        LinkedList<ShuffleConfiguration> pending = new LinkedList<>();
        Set<ShuffleConfiguration> visited = new HashSet<>();
        ShuffleConfiguration c = new ShuffleConfiguration(Arrays.asList(new Automaton(), new Automaton()), new Automaton());

        Method method = ShuffleOperations.class.getDeclaredMethod("add", Character.class, Character.class, LinkedList.class, Set.class, ShuffleConfiguration.class, int.class, Transition.class, Transition.class, char.class, char.class);
        method.setAccessible(true);
        method.invoke(null, null, null, pending, visited, c, 0, t1, t2, '\uD800', '\uDC00');

        assertFalse(pending.isEmpty());
        assertFalse(visited.isEmpty());
    }
    @Test
    public void testAddWithResumeShuffleLessThanMax() throws Exception {
        State s1 = new State();
        State s2 = new State();
        State s3 = new State();
        Transition t1 = new Transition('a', 'd', s2);
        Transition t2 = new Transition('b', 'f', s3);

        LinkedList<ShuffleConfiguration> pending = new LinkedList<>();
        Set<ShuffleConfiguration> visited = new HashSet<>();
        ShuffleConfiguration c = new ShuffleConfiguration(Arrays.asList(new Automaton(), new Automaton()), new Automaton());

        Method method = ShuffleOperations.class.getDeclaredMethod("add", Character.class, Character.class, LinkedList.class, Set.class, ShuffleConfiguration.class, int.class, Transition.class, Transition.class, char.class, char.class);
        method.setAccessible(true);
        method.invoke(null, null, 'e', pending, visited, c, 0, t1, t2, 'a', 'g');

        assertFalse(pending.isEmpty());
        assertFalse(visited.isEmpty());
    }
    @Test
    public void testShuffleConfigurationEqualsAndHashCode() {
        State initialState1 = new State();
        State initialState2 = new State();
        State state1 = new State();
        State state2 = new State();
        state1.accept = true;
        state2.accept = true;

        Automaton automaton1 = new Automaton();
        automaton1.setInitialState(initialState1);
        initialState1.addTransition(new Transition('a', 'b', state1));

        Automaton automaton2 = new Automaton();
        automaton2.setInitialState(initialState2);
        initialState2.addTransition(new Transition('a', 'b', state2));

        Collection<Automaton> ca = Arrays.asList(automaton1, automaton2);
        Automaton a = new Automaton();
        a.setInitialState(new State());

        ShuffleConfiguration config1 = new ShuffleConfiguration(ca, a);
        ShuffleConfiguration config2 = new ShuffleConfiguration(ca, a);

        // Ensure that both configurations are initially equal and have the same hash code
        assertTrue(config1.equals(config2));
        assertEquals(config1.hashCode(), config2.hashCode());

        // Modify the configurations to trigger the (shuffle_suspended || surrogate) condition
        config1.shuffle_suspended = true;
        config1.surrogate = true;
        config1.suspended1 = 1;

        config2.shuffle_suspended = true;
        config2.surrogate = true;
        config2.suspended1 = 1;

        // Ensure that the configurations are still equal and have the same hash code
        assertTrue(config1.equals(config2));
        assertEquals(config1.hashCode(), config2.hashCode());
    }
    @Disabled
    @Test
    public void testShuffleConfigurationHashCodeWithSurrogate() {
        State initialState1 = new State();
        State initialState2 = new State();
        State state1 = new State();
        State state2 = new State();
        state1.accept = true;
        state2.accept = true;

        Automaton automaton1 = new Automaton();
        automaton1.setInitialState(initialState1);
        initialState1.addTransition(new Transition('a', 'b', state1));

        Automaton automaton2 = new Automaton();
        automaton2.setInitialState(initialState2);
        initialState2.addTransition(new Transition('a', 'b', state2));

        Collection<Automaton> ca = Arrays.asList(automaton1, automaton2);
        Automaton a = new Automaton();
        a.setInitialState(new State());

        ShuffleConfiguration config1 = new ShuffleConfiguration(ca, a);
        ShuffleConfiguration config2 = new ShuffleConfiguration(ca, a);

        // Modify config1 to set surrogate to true and ensure they are not equal
        config1.surrogate = true;
        config1.suspended1 = 1;

        assertFalse(config1.equals(config2));
        assertNotEquals(config1.hashCode(), config2.hashCode());
    }
    @Disabled
    @Test
    public void testShuffleConfigurationHashCodeWithSuspended() {
        State initialState1 = new State();
        State initialState2 = new State();
        State state1 = new State();
        State state2 = new State();
        state1.accept = true;
        state2.accept = true;

        Automaton automaton1 = new Automaton();
        automaton1.setInitialState(initialState1);
        initialState1.addTransition(new Transition('a', 'b', state1));

        Automaton automaton2 = new Automaton();
        automaton2.setInitialState(initialState2);
        initialState2.addTransition(new Transition('a', 'b', state2));

        Collection<Automaton> ca = Arrays.asList(automaton1, automaton2);
        Automaton a = new Automaton();
        a.setInitialState(new State());

        ShuffleConfiguration config1 = new ShuffleConfiguration(ca, a);
        ShuffleConfiguration config2 = new ShuffleConfiguration(ca, a);

        // Modify config1 to set shuffle_suspended to true and ensure they are not equal
        config1.shuffle_suspended = true;
        config1.suspended1 = 1;

        assertFalse(config1.equals(config2));
        assertNotEquals(config1.hashCode(), config2.hashCode());
    }
}
