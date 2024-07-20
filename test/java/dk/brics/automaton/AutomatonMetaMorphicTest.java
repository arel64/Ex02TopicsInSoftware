package dk.brics.automaton;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
public class AutomatonMetaMorphicTest {
    @Test
    public void testDeterminization() {
        Automaton nfa = new Automaton();
        State s1 = new State();
        State s2 = new State();
        State s3 = new State();
        nfa.setInitialState(s1);
        s1.setAccept(true);
        s2.setAccept(true);
        s1.addTransition(new Transition('a', s2));
        s1.addTransition(new Transition('a', s3));
        s2.addTransition(new Transition('b', s3));
        Automaton dfa = nfa.clone();
        dfa.determinize();
        assertTrue(nfa.equals(dfa));
    }
    @Test
    public void testMinimization() {
        Automaton automaton = new Automaton();
        State s1 = new State();
        State s2 = new State();
        State s3 = new State();
        automaton.setInitialState(s1);
        s1.setAccept(true);
        s2.setAccept(true);
        s1.addTransition(new Transition('a', s2));
        s2.addTransition(new Transition('a', s3));
        s3.addTransition(new Transition('a', s1));
        
        Automaton minimizedAutomaton = automaton.clone();
        minimizedAutomaton.minimize();
        assertTrue(automaton.equals(minimizedAutomaton));
    }
    @Test
    public void testDoubleComplement() {
        Automaton automaton = new Automaton();
        State s1 = new State();
        State s2 = new State();
        automaton.setInitialState(s1);
        s1.setAccept(true);
        s1.addTransition(new Transition('a', s2));
        s2.addTransition(new Transition('b', s1));
        
        Automaton complementAutomaton = automaton.complement();
        Automaton doubleComplementAutomaton = complementAutomaton.complement();

        assertTrue(automaton.equals(doubleComplementAutomaton));
    }
    @Test
    public void testUnionIntersection() {
        Automaton automatonA = new Automaton();
        State a1 = new State();
        State a2 = new State();
        automatonA.setInitialState(a1);
        a1.setAccept(true);
        a1.addTransition(new Transition('a', a2));
        
        Automaton automatonB = new Automaton();
        State b1 = new State();
        State b2 = new State();
        automatonB.setInitialState(b1);
        b1.setAccept(true);
        b1.addTransition(new Transition('b', b2));
        
        Automaton unionAutomaton = automatonA.union(automatonB);
        Automaton intersectionAutomaton = unionAutomaton.intersection(automatonA);

        assertTrue(automatonA.equals(intersectionAutomaton));
    }
}
