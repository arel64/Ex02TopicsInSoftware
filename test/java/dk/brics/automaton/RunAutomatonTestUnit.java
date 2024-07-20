package dk.brics.automaton;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RunAutomatonTestUnit {
     private Automaton automaton;
    private RunAutomaton runAutomaton;

    @BeforeEach
    public void setUp() {
        automaton = BasicAutomata.makeString("test");
        runAutomaton = new RunAutomaton(automaton);
    }

    @Test
    public void testToString() {
        String result = runAutomaton.toString();
        assertNotNull(result);
        assertTrue(result.contains("initial state"));
        assertTrue(result.contains("state"));
    }

    @Test
    public void testGetCharIntervals() {
        char[] intervals = runAutomaton.getCharIntervals();
        assertNotNull(intervals);
        assertTrue(intervals.length > 0);
    }

    @Test
    public void testLoadFromUrl() throws IOException, ClassCastException, ClassNotFoundException, MalformedURLException {
        URL url = new URL("http://example.com/runAutomaton.ser");
        assertThrows(IOException.class, () -> RunAutomaton.load(url));
    }

    @Test
    public void testLoadFromStream() throws IOException, ClassCastException, ClassNotFoundException {
        InputStream inputStream = new ByteArrayInputStream(serializeRunAutomaton(runAutomaton));
        RunAutomaton loadedRunAutomaton = RunAutomaton.load(inputStream);
        assertNotNull(loadedRunAutomaton);
    }

    @Test
    public void testStore() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        runAutomaton.store(outputStream);
        assertTrue(outputStream.size() > 0);
    }

    @Test
    public void testNewMatcherWithOffsets() {
        String input = "test input";
        AutomatonMatcher matcher = runAutomaton.newMatcher(input, 0, input.length());
        assertNotNull(matcher);
    }

    @Test
    public void testGetSize() {
        int size = runAutomaton.getSize();
        assertTrue(size > 0);
    }

    private byte[] serializeRunAutomaton(RunAutomaton automaton) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        automaton.store(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
