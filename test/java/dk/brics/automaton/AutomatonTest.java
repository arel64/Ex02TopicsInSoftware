package dk.brics.automaton;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AutomatonTest {
    Automaton automaton1;
    Automaton automaton2;
    @BeforeEach
    public void setUp() {
        automaton1 = new Automaton();
        automaton2 = new Automaton();
    }
    
    @Test
    public void testSetMinimization() {
        Automaton.setMinimization(Automaton.MINIMIZE_HUFFMAN);
        assertEquals(Automaton.MINIMIZE_HUFFMAN, Automaton.minimization);

        Automaton.setMinimization(Automaton.MINIMIZE_BRZOZOWSKI);
        assertEquals(Automaton.MINIMIZE_BRZOZOWSKI, Automaton.minimization);

        Automaton.setMinimization(Automaton.MINIMIZE_HOPCROFT);
        assertEquals(Automaton.MINIMIZE_HOPCROFT, Automaton.minimization);

        Automaton.setMinimization(Automaton.MINIMIZE_VALMARI);
        assertEquals(Automaton.MINIMIZE_VALMARI, Automaton.minimization);
    }

    @Test
    public void testSetMinimizeAlways() {
        Automaton.setMinimizeAlways(true);
        assertTrue(Automaton.minimize_always);

        Automaton.setMinimizeAlways(false);
        assertFalse(Automaton.minimize_always);
    }

    @Test
    public void testSetAllowMutate() {
        boolean previousValue = Automaton.setAllowMutate(true);
        assertFalse(previousValue);
        assertTrue(Automaton.allow_mutation);

        previousValue = Automaton.setAllowMutate(false);
        assertTrue(previousValue);
        assertFalse(Automaton.allow_mutation);
    }

    @Test
    public void testGetAllowMutate() {
        Automaton.setAllowMutate(true);
        assertTrue(Automaton.getAllowMutate());

        Automaton.setAllowMutate(false);
        assertFalse(Automaton.getAllowMutate());
    }

    @Test
    public void testCheckMinimizeAlways() {
        Automaton.setMinimizeAlways(true);
        automaton1.checkMinimizeAlways(); 

        Automaton.setMinimizeAlways(false);
        automaton1.checkMinimizeAlways();
    }

    @Test
    public void testIsSingleton() {
        assertFalse(automaton1.isSingleton());
        automaton1.singleton = "test";
        assertTrue(automaton1.isSingleton());
    }

    @Test
    public void testGetSingleton() {
        assertNull(automaton1.getSingleton());
        automaton1.singleton = "test";
        assertEquals("test", automaton1.getSingleton());
    }
    @Test
    public void testSetDeterministic() {
        automaton1.setDeterministic(false);
        assertFalse(automaton1.deterministic);

        automaton1.setDeterministic(true);
        assertTrue(automaton1.deterministic);
    }

    @Test
    public void testSetInfo() {
        Object info = new Object();
        automaton1.setInfo(info);
        assertEquals(info, automaton1.info);
    }

    @Test
    public void testGetInfo() {
        assertNull(automaton1.getInfo());
        Object info = new Object();
        automaton1.setInfo(info);
        assertEquals(info, automaton1.getInfo());
    }

    @Test
    public void testMakeEmpty() {
        Automaton emptyAutomaton = Automaton.makeEmpty();
        assertNotNull(emptyAutomaton);
        // Further checks can be added based on the known state of an empty automaton.
    }

    @Test
    public void testMakeEmptyString() {
        Automaton emptyStringAutomaton = Automaton.makeEmptyString();
        assertNotNull(emptyStringAutomaton);
    }

    @Test
    public void testMakeAnyString() {
        Automaton anyStringAutomaton = Automaton.makeAnyString();
        assertNotNull(anyStringAutomaton);
    }

    @Test
    public void testMakeAnyChar() {
        Automaton anyCharAutomaton = Automaton.makeAnyChar();
        assertNotNull(anyCharAutomaton);
    }

    @Test
    public void testMakeChar() {
        Automaton charAutomaton = Automaton.makeChar('a');
        assertNotNull(charAutomaton);
    }

    @Test
    public void testMakeCharRange() {
        Automaton charRangeAutomaton = Automaton.makeCharRange('a', 'z');
        assertNotNull(charRangeAutomaton);
    }

    @Test
    public void testMakeCharSet() {
        Automaton charSetAutomaton = Automaton.makeCharSet("abc");
        assertNotNull(charSetAutomaton);
    }

    @Test
    public void testMakeInterval() {
        Automaton intervalAutomaton = Automaton.makeInterval(1, 10, 2);
        assertNotNull(intervalAutomaton);
    }

    @Test
    public void testMakeString() {
        Automaton stringAutomaton = Automaton.makeString("test");
        assertNotNull(stringAutomaton);
    }

    @Test
    public void testMakeStringUnion() {
        Automaton stringUnionAutomaton = Automaton.makeStringUnion("test1", "test2");
        assertNotNull(stringUnionAutomaton);
    }

    @Test
    public void testMakeMaxInteger() {
        Automaton maxIntegerAutomaton = Automaton.makeMaxInteger("100");
        assertNotNull(maxIntegerAutomaton);
    }

    @Test
    public void testMakeMinInteger() {
        Automaton minIntegerAutomaton = Automaton.makeMinInteger("10");
        assertNotNull(minIntegerAutomaton);
    }

    @Test
    public void testMakeTotalDigits() {
        Automaton totalDigitsAutomaton = Automaton.makeTotalDigits(5);
        assertNotNull(totalDigitsAutomaton);
    }

    @Test
    public void testMakeFractionDigits() {
        Automaton fractionDigitsAutomaton = Automaton.makeFractionDigits(2);
        assertNotNull(fractionDigitsAutomaton);
    }

    @Test
    public void testMakeIntegerValue() {
        Automaton integerValueAutomaton = Automaton.makeIntegerValue("123");
        assertNotNull(integerValueAutomaton);
    }

    @Test
    public void testMakeDecimalValue() {
        Automaton decimalValueAutomaton = Automaton.makeDecimalValue("123.45");
        assertNotNull(decimalValueAutomaton);
    }

    @Test
    public void testMakeStringMatcher() {
        Automaton stringMatcherAutomaton = Automaton.makeStringMatcher("test");
        assertNotNull(stringMatcherAutomaton);
    }

    @Test
    public void testConcatenate() {
        Automaton automaton1 = Automaton.makeString("a");
        Automaton automaton2 = Automaton.makeString("b");
        Automaton concatenatedAutomaton = automaton1.concatenate(automaton2);
        assertNotNull(concatenatedAutomaton);
    }


    @Test
    public void testOptional() {
        Automaton optionalAutomaton = automaton1.optional();
        assertNotNull(optionalAutomaton);
    }
      @Test
    void testEqualsSameObject() {
        assertTrue(automaton1.equals(automaton1));
    }

    @Test
    void testEqualsDifferentType() {
        assertFalse(automaton1.equals("not an automaton"));
    }

    @Test
    void testEqualsBothSingletons() {
        automaton1.singleton = "a";
        automaton2.singleton = "a";
        assertTrue(automaton1.equals(automaton2));
    }

    @Test
    void testEqualsOneSingleton() {
        automaton1.singleton = "a";
        assertFalse(automaton1.equals(automaton2));
    }

    @Test
    void testEqualsDifferentSingletons() {
        automaton1.singleton = "a";
        automaton2.singleton = "b";
        assertFalse(automaton1.equals(automaton2));
    }
    @Disabled
    @Test
    void testEqualsSameHashCodeAndSubset() {
        automaton1.hash_code = 1;
        automaton2.hash_code = 1;
        Automaton spy1 = Mockito.spy(automaton1);
        Automaton spy2 = Mockito.spy(automaton2);

        doReturn(true).when(spy1).subsetOf(spy2);
        doReturn(true).when(spy2).subsetOf(spy1);

        assertTrue(spy1.equals(spy2));
    }

    @Test
    void testEqualsDifferentHashCode() {
        automaton1.hash_code = 1;
        automaton2.hash_code = 2;
        assertFalse(automaton1.equals(automaton2));
    }
    @Disabled
    @Test
    void testHashCode() {
        Automaton automaton = new Automaton();
        automaton.hash_code = 0;

        // Mocking or setting the conditions for minimize to be called
        Automaton spy = Mockito.spy(automaton);
        doNothing().when(spy).minimize();

        spy.hashCode();
        verify(spy, times(1)).minimize();
    }

    @Test
    void testHashCodeNonZero() {
        Automaton automaton = new Automaton();
        automaton.hash_code = 42;
        assertEquals(42, automaton.hashCode());
    }
     @Test
    void testToStringSingleton() {
        automaton1.singleton = "a";
        String result = automaton1.toString();
        assertTrue(result.contains("singleton: a"));
    }

    @Test
    void testToStringNonSingleton() {
        automaton1.singleton = null;
        automaton1.initial = new State();
        Set<State> states = new HashSet<>();
        states.add(automaton1.initial);

        Automaton spy = Mockito.spy(automaton1);
        doReturn(states).when(spy).getStates();

        String result = spy.toString();
        assertTrue(result.contains("initial state: 0"));
    }
    @Test
    void testToDot() {
        automaton1.initial = new State();
        Set<State> states = new HashSet<>();
        states.add(automaton1.initial);

        Automaton spy = Mockito.spy(automaton1);
        doReturn(states).when(spy).getStates();

        String result = spy.toDot();
        assertTrue(result.contains("digraph Automaton"));
    }
    @Test
    void testCloneExpandedIfRequiredAllowMutation() {
        Automaton.allow_mutation = true;
        Automaton spy = Mockito.spy(automaton1);

        doNothing().when(spy).expandSingleton();

        Automaton result = spy.cloneExpandedIfRequired();
        assertEquals(spy, result);
        verify(spy, times(1)).expandSingleton();
    }

    @Test
    void testCloneExpandedIfRequiredNoMutation() {
        Automaton.allow_mutation = false;
        Automaton spy = Mockito.spy(automaton1);

        Automaton result = spy.cloneExpandedIfRequired();
        assertNotEquals(spy, result);
        verify(spy, times(1)).cloneExpanded();
    }
    @Test
    void testMinus() {
        Automaton a = new Automaton();
        Automaton result = automaton1.minus(a);
        assertNotNull(result);
    }
    @Test
    void testSubsetOf() {
        Automaton a = new Automaton();
        boolean result = automaton1.subsetOf(a);
        assertTrue(result); 
    }
    @Test
    void testUnionSingleAutomaton() {
        Automaton a = new Automaton();
        Automaton result = automaton1.union(a);
        assertNotNull(result);
    }
    @Test
    public void testIsDebug() {
        Automaton automaton = new Automaton();
        
        try {
            Field isDebugField = Automaton.class.getDeclaredField("is_debug");
            isDebugField.setAccessible(true);
            isDebugField.set(null, null);  // Reset the cached value to null
            assertFalse(automaton.isDebug());
        
            // Cover branch where is_debug is set
            isDebugField.set(null, true);
            assertTrue(automaton.isDebug());
        
            // Reset the cached value to false
            isDebugField.set(null, false);
        } catch (Exception e) {
            fail();
        } 
       
    }
    @Disabled
    @Test
    public void testSetStateNumbers() {
        Automaton automaton = new Automaton();
        State state1 = new State();
        State state2 = new State();
        state1.addTransition(new Transition('a', state2));
        automaton.setInitialState(state1);

        Set<State> states = automaton.getStates();
        Automaton.setStateNumbers(states);
        assertEquals(0, state1.number);
        assertEquals(1, state2.number);
        
        // Cover edge case where states size == Integer.MAX_VALUE (impractical to test directly, just ensure method exists)
    }
    @Test
    public void testRestoreInvariant() {
        Automaton automaton = new Automaton();
        automaton.setInitialState(new State());

        // Ensure method exists and can be called
        automaton.restoreInvariant();
    }
    @Test
    public void testReduce() {
        Automaton automaton = new Automaton();
        automaton.setInitialState(new State());
        automaton.singleton = "abc";

        // Cover branch where isSingleton is true
        automaton.reduce();

        // Ensure that the method exists and can be called when singleton is not true
        automaton.singleton = null;
        automaton.reduce();
    }
    @Disabled
    @Test
    public void testGetLiveStates() {
        Automaton automaton = new Automaton();
        State initialState = new State();
        State acceptState = new State();
        acceptState.setAccept(true);
        initialState.addTransition(new Transition('a', 'b', acceptState));
        automaton.setInitialState(initialState);

        // Cover branch where isSingleton is true
        automaton.singleton = "a";
        Set<State> liveStates = automaton.getLiveStates();
        assertNotNull(liveStates);

        // Cover branch where isSingleton is false
        automaton.singleton = null;
        liveStates = automaton.getLiveStates();
        assertEquals(1, liveStates.size());
    }
    @Test
    public void testRemoveDeadTransitions() {
        Automaton automaton = new Automaton();
        State initialState = new State();
        State deadState = new State();
        initialState.addTransition(new Transition('a', deadState));
        automaton.setInitialState(initialState);

        // Cover branch where isSingleton is true
        automaton.singleton = "a";
        automaton.removeDeadTransitions();

        // Cover branch where isSingleton is false
        automaton.singleton = null;
        automaton.removeDeadTransitions();
    }
    @Test
    public void testRecomputeHashCode() {
        Automaton automaton = new Automaton();
        State initialState = new State();
        automaton.setInitialState(initialState);

        // Ensure hash_code is 0 initially
        automaton.recomputeHashCode();
        assertNotEquals(0, automaton.hash_code);
    }
    @Test
    void testUnionCollection() {
        Collection<Automaton> automatonCollection = Collections.singletonList(new Automaton());
        Automaton result = Automaton.union(automatonCollection);
        assertNotNull(result);
    }
    
      @Test
    void testOverlap() {
        Automaton result = automaton1.overlap(automaton2);
        assertNotNull(result);
    }

    @Test
    void testSingleChars() {
        Automaton result = automaton1.singleChars();
        assertNotNull(result);
    }

    @Test
    void testTrim() {
        Automaton result = automaton1.trim("set", 'c');
        assertNotNull(result);
    }

    @Test
    void testCompress() {
        Automaton result = automaton1.compress("set", 'c');
        assertNotNull(result);
    }

    @Test
    void testSubstWithMap() {
        Map<Character, Set<Character>> map = new HashMap<>();
        map.put('a', new HashSet<>(Set.of('b', 'c')));
        Automaton result = automaton1.subst(map);
        assertNotNull(result);
    }

    @Test
    void testSubstWithCharAndString() {
        Automaton result = automaton1.subst('a', "bc");
        assertNotNull(result);
    }

    @Test
    void testHomomorph() {
        char[] source = {'a', 'b'};
        char[] dest = {'c', 'd'};
        Automaton result = automaton1.homomorph(source, dest);
        assertNotNull(result);
    }

    @Test
    void testProjectChars() {
        Set<Character> chars = new HashSet<>(Set.of('a', 'b', 'c'));
        Automaton result = automaton1.projectChars(chars);
        assertNotNull(result);
    }

    @Test
    void testIsFinite() {
        boolean result = automaton1.isFinite();
        assertTrue(result);
    }

    @Test
    void testGetStrings() {
        Set<String> result = automaton1.getStrings(5);
        assertNotNull(result);
    }

    @Test
    void testGetFiniteStrings() {
        Set<String> result = automaton1.getFiniteStrings();
        assertNotNull(result);
    }

    @Test
    void testGetFiniteStringsWithLimit() {
        Set<String> result = automaton1.getFiniteStrings(5);
        assertNotNull(result);
    }

    @Test
    void testGetCommonPrefix() {
        String result = automaton1.getCommonPrefix();
        assertNotNull(result);
    }

    @Test
    void testPrefixClose() {
        automaton1.prefixClose();
    }

    @Test
    void testHexCases() {
        Automaton result = Automaton.hexCases(automaton1);
        assertNotNull(result);
    }

    @Test
    void testReplaceWhitespace() {
        Automaton result = Automaton.replaceWhitespace(automaton1);
        assertNotNull(result);
    }
    @Disabled
    @Test
    void testShuffleSubsetOf() {
        String result = Automaton.shuffleSubsetOf(Collections.singletonList(automaton1), automaton2, 'a', 'b');
        assertNotNull(result);
    }

    @Test
    void testShuffle() {
        Automaton result = automaton1.shuffle(automaton2);
        assertNotNull(result);
    }
    
}
