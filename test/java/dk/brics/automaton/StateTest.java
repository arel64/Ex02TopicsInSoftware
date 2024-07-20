package dk.brics.automaton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
public class StateTest{
    /**
     * Test that state is constructed correctly, there are no transitions
     * and all are properly initialized
     */
    @Test
    public void testStateConstruction(){
        State s = new State();
        assertEquals(s.accept, false);
        assertEquals(s.transitions.size(), 0);
        assertEquals(s.number, 0);
    }
    @Test
    public void testResetTransitionsNaive(){
        State s = new State();
        assertEquals(s.getTransitions().size(), 0);
        s.resetTransitions();
        assertEquals(s.getTransitions().size(), 0);
    }
    @Test
    public void testResetTransitions()
    {
        State s = new State();
        assertEquals(s.getTransitions().size(), 0);
        Transition t = new Transition('7', new State());
        s.addTransition(t);
        assertEquals(s.getTransitions().size(), 1);
        s.resetTransitions();
        assertEquals(s.getTransitions().size(), 0);
    }
    @Test
    public void testAddTransition(){
        State s = new State();
        Transition t = new Transition('7', new State());    
        s.addTransition(t);
        assertEquals(s.getTransitions().size(), 1);
        assertEquals(s.getTransitions().contains(t), true);
    }
    @Test
    public void testSetAccept(){
        State s = new State();
        s.setAccept(true);
        assertEquals(s.isAccept(), true);
        s.setAccept(false);
        assertEquals(s.isAccept(), false);
    }
    @Test
    public void testStepNullState(){
        State s = new State();
        assertEquals(s.step('7'), null);
        
    }
    @Test
    public void testStep(){
        State s2 = new State();
        Transition t = new Transition('7', s2);
        State s = new State();
        s.addTransition(t);

        assertEquals(s.step('7'), s2);
    }
    @Test
    public void testStepMatch()
    {
        State s = new State();
        State s1 = new State();
        State s2 = new State();
        s.addTransition(new Transition('7', s1));
        s.addTransition(new Transition('8', s2));
        s.addTransition(new Transition('8', s));
        s.addTransition(new Transition('8', s));
        s.addTransition(new Transition('9', s));
    }
    /**
     * 
     */
    @Test
    public void testStepCollectionNDA(){
        ArrayList<State> states = new ArrayList<State>();
        State s = new State();
        State s1 = new State();
        State s2 = new State();
        s.addTransition(new Transition('7', s1));
        s.addTransition(new Transition('8', s2));
        s.addTransition(new Transition('8', s));
        s.addTransition(new Transition('8', s));
        s.addTransition(new Transition('9', s));

        s.step('8', states);


        assertEquals(states.size(), 2);
        assertTrue(states.contains(s2));
        assertTrue(states.contains(s));
        assertTrue(!states.contains(s1));

    }
    @Test
    public void testStepNoMatch(){
        State s = new State();
        s.addTransition(new Transition('8',s));
        assertEquals(s.step('a'),null);
        assertEquals(s.step('0'),null);
    }
    @Test
    public void testStepExactlyOneMatchHigh(){
        State s = new State();
        s.addTransition(new Transition('8',s));
        assertEquals(s.step('8'),s);
        assertEquals(s.step('7'),null);
    }
    
    @Test
    public void testStepExactlyOneMatchLow(){
        State s = new State();
        s.addTransition(new Transition('7',s));
        assertEquals(s.step('8'),null);
        assertEquals(s.step('7'),s);
    }
    @Test
    public void testStepMatchRange(){
        State s = new State();
        s.addTransition(new Transition('1','7',s));
        assertEquals(s.step('8'),null);
        assertEquals(s.step('5'),s);
        assertEquals(s.step('0'),null);
    }
    @Test
    public void testEpsilonAccept(){
        State s = new State();
        State s1 = new State();
        
        
        assertEquals(s.isAccept(), false);
        assertEquals(s1.isAccept(), false);

        s.addEpsilon(s1);

        assertEquals(s.isAccept(), false);
        assertEquals(s1.isAccept(), false);

    }
    @Test
    public void testEpsilonNoAccept(){
        State s = new State();
        State s1 = new State();
        s1.setAccept(true);

        assertEquals(s.isAccept(), false);
        s.addEpsilon(s1);
        assertEquals(s.isAccept(), true);
    }
    @Test
    public void getSortedTransitionArrayTestToFirst()
    {
        State s = new State();
        State s1 = new State();
        State s2 = new State();
        Transition a1 = new Transition('7', s1);
        Transition a2 = new Transition('8', s2);
        Transition a3 = new Transition('8', s);
        Transition a4 = new Transition('9', s);
        s.addTransition(a1);
        s.addTransition(a2);
        s.addTransition(a3);
        s.addTransition(a4);
        Transition[] transitions = s.getSortedTransitionArray(true);
        assertEquals(transitions.length, 4);
        assertEquals(transitions[0],a1);
        assertEquals(transitions[0].min, '7');
        assertEquals(transitions[1],a2);
        assertEquals(transitions[2],a3);
        assertEquals(transitions[3],a4);
    }
     @Test
    public void testToString_AcceptStateWithTransitions() {
        State state = new State();
        state.setAccept(true);
        state.addTransition(new Transition('a', 'z', new State()));
        state.addTransition(new Transition('0', '9', new State()));
        
        String result = state.toString();
        
        assertTrue(result.contains("state " + state.number));
        assertTrue(result.contains("[accept]"));
        assertTrue(result.contains("a-z ->"));
        assertTrue(result.contains("0-9 ->"));
    }
    
    @Test
    public void testToString_RejectStateWithTransitions() {
        State state = new State();
        state.setAccept(false);
        state.addTransition(new Transition('a', 'z', new State()));
        state.addTransition(new Transition('0', '9', new State()));
        
        String result = state.toString();
        
        assertTrue(result.contains("state " + state.number));
        assertTrue(result.contains("[reject]"));
        assertTrue(result.contains("a-z ->"));
        assertTrue(result.contains("0-9 ->"));
    }
    
    @Test
    public void testToString_RejectStateWithoutTransitions() {
        State state = new State();
        state.setAccept(false);
        
        String result = state.toString();
        
        assertTrue(result.contains("state " + state.number));
        assertTrue(result.contains("[reject]"));
        assertFalse(result.contains("->"));
    }
    @Test
    public void compareTest()
    {
        State s1 = new State();
        assertTrue(s1.compareTo(s1)==0);
    }
    
    @Test
    public void compareTestNonEqual()
    {
        State s1 = new State();
        State s2 = new State();
        assertFalse(s1.compareTo(s2)==0);
    }
    @Test
    public void hashCodeTest()
    {
        State s1 = new State();
        State s2 = new State();
        assertTrue(s1.hashCode()!=s2.hashCode());
    }   
    @Test
    public void hashCodeTestEqual()
    {
        State s1 = new State();
        State s2 = s1;
        assertTrue(s1.hashCode()==s2.hashCode());
    }
    @Test
    public void getSortedTransitionList()
    {
        State s = new State();
        Transition[] transitions = s.getSortedTransitionArray(false);
        List<Transition> list = s.getSortedTransitions(false);
        assertEquals(transitions.length, list.size());
        assertIterableEquals(Arrays.asList(transitions), list);
    }
}