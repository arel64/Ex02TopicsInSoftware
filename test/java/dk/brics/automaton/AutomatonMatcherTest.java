package dk.brics.automaton;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class AutomatonMatcherTest {

    private AutomatonMatcher matcher;
    private RunAutomaton automaton;
    private CharSequence chars;

    @BeforeEach
    void setUp() {
        chars = "aabbcc";
        automaton = mock(RunAutomaton.class);
        matcher = new AutomatonMatcher(chars, automaton);
    }

    @Test
    void testFindNoMatch() {
        when(automaton.getInitialState()).thenReturn(0);
        when(automaton.isAccept(0)).thenReturn(false);
        when(automaton.step(anyInt(), anyChar())).thenReturn(-1);
        assertFalse(matcher.find());
    }

    @Test
    void testFindMatch() {
        when(automaton.getInitialState()).thenReturn(0);
        when(automaton.step(0, 'a')).thenReturn(1);
        when(automaton.isAccept(1)).thenReturn(true);

        assertTrue(matcher.find());
        assertEquals(0, matcher.start());
        assertEquals(1, matcher.end());
    }

    @Test
    void testEndWithoutMatch() {
        assertThrows(IllegalStateException.class, () -> matcher.end());
    }

    @Test
    void testEndWithMatch() {
        when(automaton.getInitialState()).thenReturn(0);
        when(automaton.step(0, 'a')).thenReturn(1);
        when(automaton.isAccept(1)).thenReturn(true);
        matcher.find();
        assertEquals(1, matcher.end());
    }

    @Test
    void testGroupWithoutMatch() {
        assertThrows(IllegalStateException.class, () -> matcher.group());
    }

    @Test
    void testGroupWithMatch() {
        when(automaton.getInitialState()).thenReturn(0);
        when(automaton.step(0, 'a')).thenReturn(1);
        when(automaton.isAccept(1)).thenReturn(true);
        matcher.find();
        assertEquals("a", matcher.group());
    }

    @Test
    void testStartWithoutMatch() {
        assertThrows(IllegalStateException.class, () -> matcher.start());
    }

    @Test
    void testStartWithMatch() {
        when(automaton.getInitialState()).thenReturn(0);
        when(automaton.step(0, 'a')).thenReturn(1);
        when(automaton.isAccept(1)).thenReturn(true);
        matcher.find();
        assertEquals(0, matcher.start());
    }
    @Test
    void testToMatchResult() {
        when(automaton.getInitialState()).thenReturn(0);
        when(automaton.step(0, 'a')).thenReturn(1);
        when(automaton.isAccept(1)).thenReturn(true);
        matcher.find();
        AutomatonMatcher result = (AutomatonMatcher) matcher.toMatchResult();
        assertEquals(0, result.start());
        assertEquals(1, result.end());
    }

    @Test
    void testOnlyZero() {
        assertThrows(IndexOutOfBoundsException.class, () -> matcher.start(1));
        assertThrows(IndexOutOfBoundsException.class, () -> matcher.end(1));
        assertThrows(IndexOutOfBoundsException.class, () -> matcher.group(1));
    }

    @Test
    void testGroupCount() {
        assertEquals(0, matcher.groupCount());
    }

    @Test
    public void testFindEmptyStringMatch() {
        Automaton emptyAutomaton = new Automaton();
        State initialState = new State();
        initialState.setAccept(true);
        emptyAutomaton.setInitialState(initialState);
        RunAutomaton runAutomaton = new RunAutomaton(emptyAutomaton);
        matcher = new AutomatonMatcher("abc", runAutomaton);

        assertTrue(matcher.find());
        assertEquals(0, matcher.start());
        assertEquals(0, matcher.end());
        assertEquals("", matcher.group());

        assertTrue(matcher.find());
        assertEquals(1, matcher.start());
        assertEquals(1, matcher.end());
        assertEquals("", matcher.group());

        assertTrue(matcher.find());
        assertEquals(2, matcher.start());
        assertEquals(2, matcher.end());
        assertEquals("", matcher.group());

        assertTrue(matcher.find());
        assertEquals(3, matcher.start());
        assertEquals(3, matcher.end());
        assertEquals("", matcher.group());

        assertFalse(matcher.find());
    }

    @Test
    public void testSetMatchValid() {
        matcher = new AutomatonMatcher("a", automaton);
        when(automaton.getInitialState()).thenReturn(0);
        when(automaton.step(0, 'a')).thenReturn(1);
        when(automaton.isAccept(1)).thenReturn(true);
        matcher.find();
        assertEquals(0, matcher.start());
        assertEquals(1, matcher.end());
    }
    
    @Test
    public void testGetMatchEnd() {
        matcher = new AutomatonMatcher("a", automaton);
        when(automaton.getInitialState()).thenReturn(0);
        when(automaton.step(0, 'a')).thenReturn(1);
        when(automaton.isAccept(1)).thenReturn(true);
        matcher.find();
        // Verify the end after find
        assertEquals(1, matcher.end());
    }
  
    @Test
    public void testEndWithInvalidGroup() {
        matcher = new AutomatonMatcher("a", automaton);
        matcher.find();
        assertThrows(IndexOutOfBoundsException.class, () -> {
            matcher.end(1);
        });
    }
    @Test
    public void testEndWithGroup() {
        matcher = new AutomatonMatcher("a", automaton);
        when(automaton.getInitialState()).thenReturn(0);
        when(automaton.step(0, 'a')).thenReturn(1);
        when(automaton.isAccept(1)).thenReturn(true);
        matcher.find();
        assertEquals(1, matcher.end(0));
    }

    @Test
    public void testGroupWithGroup() {
        matcher = new AutomatonMatcher("a", automaton);
        when(automaton.getInitialState()).thenReturn(0);
        when(automaton.step(0, 'a')).thenReturn(1);
        when(automaton.isAccept(1)).thenReturn(true);
        matcher.find();
        assertEquals("a", matcher.group(0));
    }
    public void testIntersectionA1EqualsA2() {
        Automaton a1 = BasicAutomata.makeString("a");
        Automaton result = BasicOperations.intersection(a1, a1);
        assertFalse(result.isEmpty());
        assertTrue(result.isSingleton());
        assertEquals("a", result.getSingleton());
    }
    @Test
    public void testStartWithGroup() {
        matcher = new AutomatonMatcher("a", automaton);
        when(automaton.getInitialState()).thenReturn(0);
        when(automaton.step(0, 'a')).thenReturn(1);
        when(automaton.isAccept(1)).thenReturn(true);
        matcher.find();
        assertEquals(0, matcher.start(0));
    }

    @Test
    public void testGroupWithInvalidGroup() {
        matcher = new AutomatonMatcher("a", automaton);
        matcher.find();
        assertThrows(IndexOutOfBoundsException.class, () -> {
            matcher.group(1);
        });
    }

   
    @Test
    public void testStartWithInvalidGroup() {
        matcher = new AutomatonMatcher("a", automaton);
        matcher.find();
        assertThrows(IndexOutOfBoundsException.class, () -> {
            matcher.start(1);
        });
    }

}