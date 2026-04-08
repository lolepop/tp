package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

public class SubstringStreamTest {
    @Test
    public void nextSubstringUntil_singleCharDelimitersInStream_returnsSubstringTillFirstDelimiterAfterOneInvocation() {
        String input = "there,are,commas";
        SubstringStream stream = new SubstringStream(input);
        assertEquals("there", stream.nextSubstringUntil(","));
    }

    @Test
    public void nextSubstringUntil_singleCharDelimitersInStream_returnsSubstringsBtwnDelimitersAfterMultipleInvokes() {
        String input = "there,are,commas";
        SubstringStream stream = new SubstringStream(input);
        assertEquals("there", stream.nextSubstringUntil(","));
        assertEquals("are", stream.nextSubstringUntil(","));
        assertEquals("commas", stream.nextSubstringUntil(","));
    }

    @Test
    public void nextSubstringUntil_multiCharDelimitersInStream_returnsSubstringTillFirstDelimiterAfterOneInvocation() {
        String input = "there,\"are,\"commas";
        SubstringStream stream = new SubstringStream(input);
        assertEquals("there", stream.nextSubstringUntil(",\""));
    }

    @Test
    public void nextSubstringUntil_multiCharDelimitersInStream_returnsSubstringsBtwnDelimitersAfterMultipleInvokes() {
        String input = "there,\"are,\"commas";
        SubstringStream stream = new SubstringStream(input);
        assertEquals("there", stream.nextSubstringUntil(",\""));
        assertEquals("are", stream.nextSubstringUntil(",\""));
        assertEquals("commas", stream.nextSubstringUntil(",\""));
    }

    @Test
    public void nextSubstringUntil_noSpecifiedDelimiterInStream_returnsRemainingSubstring() {
        String input = "nocomma";
        SubstringStream stream = new SubstringStream(input);
        String output = stream.nextSubstringUntil(",");
        assertEquals(input, output);
    }

    @Test
    public void nextSubstringUntil_emptyStream_throwsNoSuchElementException() {
        String input = "";
        SubstringStream stream = new SubstringStream(input);
        assertThrows(NoSuchElementException.class, () -> stream.nextSubstringUntil(","));
    }

    @Test
    public void nextSubstringUntil_exhaustedStream_throwsNoSuchElementException() {
        String input = "test,";
        SubstringStream stream = new SubstringStream(input);
        stream.nextSubstringUntil(",");
        assertThrows(NoSuchElementException.class, () -> stream.nextSubstringUntil(","));
    }

    @Test
    public void isExhausted_emptyStream_returnsTrue() {
        String input = "";
        SubstringStream stream = new SubstringStream(input);
        assertTrue(stream.isExhausted());
    }

    @Test
    public void isExhausted_exhaustedStream_returnsTrue() {
        String input = "test,";
        SubstringStream stream = new SubstringStream(input);
        stream.nextSubstringUntil(",");
        assertTrue(stream.isExhausted());
    }

    @Test
    public void isExhausted_nonEmptyStream_returnsFalse() {
        String input = "test";
        SubstringStream stream = new SubstringStream(input);
        assertFalse(stream.isExhausted());
    }

    @Test
    public void isExhausted_nonExhaustedStream_returnsFalse() {
        String input = "test,hello";
        SubstringStream stream = new SubstringStream(input);
        stream.nextSubstringUntil(",");
        assertFalse(stream.isExhausted());
    }

}
