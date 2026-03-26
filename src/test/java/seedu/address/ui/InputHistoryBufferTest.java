package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class InputHistoryBufferTest {
    @Test
    void constructorWithValidSize() {
        InputHistoryBuffer buffer = new InputHistoryBuffer(5);
        assertNotNull(buffer);
    }

    @Test
    void constructorWithInvalidSize() {
        assertThrows(IllegalArgumentException.class, () -> new InputHistoryBuffer(0));
        assertThrows(IllegalArgumentException.class, () -> new InputHistoryBuffer(-1));
    }

    @Test
    void constructorDefaultSize() {
        InputHistoryBuffer buffer = new InputHistoryBuffer();
        assertNotNull(buffer);
    }

    @Test
    void pushHistoryAddsCommand() {
        InputHistoryBuffer buffer = new InputHistoryBuffer(2);
        assertEquals(Optional.empty(), buffer.moveCursorUp());
        assertEquals(Optional.empty(), buffer.moveCursorDown());

        buffer.pushHistory("cmd1");
        // After push, cursor is at history.size() (1)
        assertEquals(Optional.of("cmd1"), buffer.moveCursorUp());
        assertEquals(Optional.of("cmd1"), buffer.moveCursorDown());
        assertEquals(Optional.of("cmd1"), buffer.moveCursorDown());
    }

    @Test
    void pushHistoryMaintainsMaxSize() {
        InputHistoryBuffer buffer = new InputHistoryBuffer(2);
        buffer.pushHistory("cmd1");
        buffer.pushHistory("cmd2");
        buffer.pushHistory("cmd3"); // should evict cmd1

        // History should now contain ["cmd2", "cmd3"]
        // Cursor is at size (2)
        assertEquals(Optional.of("cmd3"), buffer.moveCursorUp()); // newest
        assertEquals(Optional.of("cmd2"), buffer.moveCursorUp()); // older
        assertEquals(Optional.of("cmd2"), buffer.moveCursorUp()); // already at oldest
    }

    @Test
    void pushHistoryWithNullThrowsException() {
        InputHistoryBuffer buffer = new InputHistoryBuffer();
        assertThrows(NullPointerException.class, () -> buffer.pushHistory(null));
    }

    @Test
    void emptyHistoryNavigation() {
        InputHistoryBuffer buffer = new InputHistoryBuffer();
        assertEquals(Optional.empty(), buffer.moveCursorUp());
        assertEquals(Optional.empty(), buffer.moveCursorDown());
        assertEquals(Optional.empty(), buffer.moveCursorUp());
        assertEquals(Optional.empty(), buffer.moveCursorUp());
    }

    @Test
    void navigationSingleEntry_upDown() {
        InputHistoryBuffer buffer = new InputHistoryBuffer();
        buffer.pushHistory("only");

        assertEquals(Optional.of("only"), buffer.moveCursorUp());
        assertEquals(Optional.of("only"), buffer.moveCursorUp());
        assertEquals(Optional.of("only"), buffer.moveCursorDown());
        assertEquals(Optional.of("only"), buffer.moveCursorDown());
    }

    @Test
    void navigationWithMultipleEntries() {
        InputHistoryBuffer buffer = new InputHistoryBuffer(5);
        buffer.pushHistory("first");
        buffer.pushHistory("second");
        buffer.pushHistory("third");

        // After pushes, cursor = 3 (size)
        // Move up three times to reach oldest
        assertEquals(Optional.of("third"), buffer.moveCursorUp()); // cursor=2
        assertEquals(Optional.of("second"), buffer.moveCursorUp()); // cursor=1
        assertEquals(Optional.of("first"), buffer.moveCursorUp()); // cursor=0
        // At oldest, moving up stays and returns oldest
        assertEquals(Optional.of("first"), buffer.moveCursorUp()); // cursor remains at 0

        // Move down
        assertEquals(Optional.of("second"), buffer.moveCursorDown()); // cursor=1
        assertEquals(Optional.of("third"), buffer.moveCursorDown()); // cursor=2
        assertEquals(Optional.of("third"), buffer.moveCursorDown()); // cursor=3, remain
        assertEquals(Optional.of("third"), buffer.moveCursorDown());
    }

    @Test
    void cursorAfterPushIsSetToSize() {
        InputHistoryBuffer buffer = new InputHistoryBuffer(3);
        buffer.pushHistory("a");
        buffer.pushHistory("b");

        assertEquals(Optional.of("b"), buffer.moveCursorUp());
        assertEquals(Optional.of("a"), buffer.moveCursorUp());
        assertEquals(Optional.of("a"), buffer.moveCursorUp());
    }

    @Test
    void maxSizeOne() {
        // testing on boundary case
        InputHistoryBuffer buffer = new InputHistoryBuffer(1);
        buffer.pushHistory("first");
        buffer.pushHistory("second"); // should evict "first"

        // Only "second" remains
        assertEquals(Optional.of("second"), buffer.moveCursorUp()); // cursor=0
        assertEquals(Optional.of("second"), buffer.moveCursorUp()); // stays
        assertEquals(Optional.of("second"), buffer.moveCursorDown());
    }

}
