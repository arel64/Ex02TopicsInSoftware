package dk.brics.automaton;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
public class TransitionTest {

    private State state1;
    private State state2;
    private Transition transition1;
    private Transition transition2;

    @BeforeEach
    public void setUp() {
        state1 = new State();
        state2 = new State();
        state2.number = 1;
        transition1 = new Transition('a', state1);
        transition2 = new Transition('a', 'z', state2);
    }

    @Test
    public void testConstructor_SingleChar() {
        assertEquals('a', transition1.getMin());
        assertEquals('a', transition1.getMax());
        assertEquals(state1, transition1.getDest());
    }

    @Test
    public void testConstructor_CharInterval() {
        assertEquals('a', transition2.getMin());
        assertEquals('z', transition2.getMax());
        assertEquals(state2, transition2.getDest());
    }

    @Test
    public void testConstructor_CharIntervalReversed() {
        Transition t = new Transition('z', 'a', state1);
        assertEquals('a', t.getMin());
        assertEquals('z', t.getMax());
        assertEquals(state1, t.getDest());
    }

    @Test
    public void testGetMin() {
        assertEquals('a', transition1.getMin());
        assertEquals('a', transition2.getMin());
    }

    @Test
    public void testGetMax() {
        assertEquals('a', transition1.getMax());
        assertEquals('z', transition2.getMax());
    }

    @Test
    public void testGetDest() {
        assertEquals(state1, transition1.getDest());
        assertEquals(state2, transition2.getDest());
    }

    @Test
    public void testEquals() {
        Transition t1 = new Transition('a', state1);
        Transition t2 = new Transition('a', state1);
        Transition t3 = new Transition('b', state1);
        Transition t4 = new Transition('a', 'z', state1);
        Transition t5 = new Transition('a', state2);

        assertTrue(t1.equals(t2));
        assertFalse(t1.equals(t3));
        assertFalse(t1.equals(t4));
        assertFalse(t1.equals(t5));
        assertFalse(t1.equals(null));
        assertFalse(t1.equals("some string"));
    }

    @Test
    public void testHashCode() {
        Transition t1 = new Transition('a', 'b', state1);
        Transition t2 = new Transition('a', 'b', state2);
        assertEquals(t1.hashCode(), t2.hashCode());

        Transition t3 = new Transition('a', state1);
        Transition t4 = new Transition('a', state1);
        assertEquals(t3.hashCode(), t4.hashCode());
    }

    @Test
    public void testClone() {
        Transition clone = transition1.clone();
        assertEquals(transition1, clone);
        assertNotSame(transition1, clone);
    }
    @Test
    public void testAppendCharString() {
        StringBuilder b = new StringBuilder();
        Transition.appendCharString('a', b);
        assertEquals("a", b.toString());

        b.setLength(0);
        Transition.appendCharString((char)0x1F, b);
        assertEquals("\\u001f", b.toString());

        b.setLength(0);
        Transition.appendCharString((char)0x7E, b);
        assertEquals("~", b.toString());

        b.setLength(0);
        Transition.appendCharString((char)0x80, b);
        assertEquals("\\u0080", b.toString());
    }

    @Test
    public void testToString() {
        assertEquals("a -> 0", transition1.toString());
        assertEquals("a-z -> 1", transition2.toString());
    }

    @Test
    public void testAppendDot() {
        StringBuilder b = new StringBuilder();
        transition1.appendDot(b);
        assertEquals(" -> 0 [label=\"a\"]\n", b.toString());

        b.setLength(0);
        transition2.appendDot(b);
        assertEquals(" -> 1 [label=\"a-z\"]\n", b.toString());
    }
    @Test
    public void testAppendCharString_NormalChar() {
        StringBuilder b = new StringBuilder();
        Transition.appendCharString('a', b);
        assertEquals("a", b.toString());

        b.setLength(0);
        Transition.appendCharString('Z', b);
        assertEquals("Z", b.toString());
    }

    @Test
    public void testAppendCharString_Backslash() {
        StringBuilder b = new StringBuilder();
        Transition.appendCharString('\\', b);
        assertEquals("\\u005c", b.toString());
    }

    @Test
    public void testAppendCharString_Quote() {
        StringBuilder b = new StringBuilder();
        Transition.appendCharString('"', b);
        assertEquals("\\u0022", b.toString());
    }

    @Test
    public void testAppendCharString_Under0x10() {
        StringBuilder b = new StringBuilder();
        Transition.appendCharString((char)0x0F, b);
        assertEquals("\\u000f", b.toString());
    }

    @Test
    public void testAppendCharString_Under0x100() {
        StringBuilder b = new StringBuilder();
        Transition.appendCharString((char)0x8F, b);
        assertEquals("\\u008f", b.toString());
    }

    @Test
    public void testAppendCharString_Under0x1000() {
        StringBuilder b = new StringBuilder();
        Transition.appendCharString((char)0x9FF, b);
        assertEquals("\\u09ff", b.toString());
    }

    @Test
    public void testAppendCharString_Above0x1000() {
        StringBuilder b = new StringBuilder();
        Transition.appendCharString((char)0x1000, b);
        assertEquals("\\u1000", b.toString());
    }

    @Test
    public void testAppendCharString_ControlChar() {
        StringBuilder b = new StringBuilder();
        Transition.appendCharString((char)0x1F, b);
        assertEquals("\\u001f", b.toString());
    }

    @Test
    public void testAppendCharString_DelChar() {
        StringBuilder b = new StringBuilder();
        Transition.appendCharString((char)0x7F, b);
        assertEquals("\\u007f", b.toString());
    }

}