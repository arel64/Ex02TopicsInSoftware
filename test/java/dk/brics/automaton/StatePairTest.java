package dk.brics.automaton;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StatePairTest {
    
    private State s1;
    private State s2;
    private State s3;
    private StatePair pair1;
    private StatePair pair2;
    private StatePair pair3;

    @BeforeEach
    public void setUp() {
        s1 = new State();
        s2 = new State();
        s3 = new State();

        pair1 = new StatePair(s1, s2);
        pair2 = new StatePair(s1, s2);
        pair3 = new StatePair(s2, s3);
    }

    @Test
    public void testConstructorWithThreeArguments() {
        StatePair pair = new StatePair(s1, s2, s3);
        assertNotNull(pair);
        assertEquals(s1, pair.s);
        assertEquals(s2, pair.s1);
        assertEquals(s3, pair.s2);
    }

    @Test
    public void testConstructorWithTwoArguments() {
        assertNotNull(pair1);
        assertEquals(s1, pair1.getFirstState());
        assertEquals(s2, pair1.getSecondState());
    }

    @Test
    public void testGetFirstState() {
        assertEquals(s1, pair1.getFirstState());
    }

    @Test
    public void testGetSecondState() {
        assertEquals(s2, pair1.getSecondState());
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(pair1.equals(pair1));
    }

    @Test
    public void testEqualsEqualObjects() {
        assertTrue(pair1.equals(pair2));
    }

    @Test
    public void testEqualsDifferentObjects() {
        assertFalse(pair1.equals(pair3));
    }

    @Test
    public void testEqualsNullObject() {
        assertFalse(pair1.equals(null));
    }

    @Test
    public void testEqualsDifferentClassObject() {
        assertFalse(pair1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        assertEquals(pair1.hashCode(), pair2.hashCode());
        assertNotEquals(pair1.hashCode(), pair3.hashCode());
    }
    @Test
    public void testEqualsDifferentFirstState() {
        StatePair pair = new StatePair(s3, s2);
        assertFalse(pair1.equals(pair));
    }

    @Test
    public void testEqualsDifferentSecondState() {
        StatePair pair = new StatePair(s1, s3);
        assertFalse(pair1.equals(pair));
    }
}
