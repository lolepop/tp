package seedu.address.ui;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.ArrayList;
import java.util.Optional;

/**
 * A fixed‑size buffer that stores a history of input commands. The buffer
 * maintains a cursor that points to a position within the history. The cursor
 * can be moved to earlier (older) or later (newer) commands.
 *
 * @see #moveCursorUp()
 * @see #moveCursorDown()
 * @see #pushHistory(String)
 */
public final class InputHistoryBuffer {
    public static final int DEFAULT_HISTORY_SIZE = 100;
    private static final int CURSOR_EARLIER = -1;
    private static final int CURSOR_LATER = 1;

    // ArrayList is used over Deque since we need to access history without
    // modifying it. History should be small, so performance is not an issue
    private ArrayList<String> history;
    private int maxHistorySize;

    // position of which element in history is being pointed to
    // Convention: 0 is oldest, history.size()-1 is newest
    private int cursor;

    /**
     * Constructs an input history buffer with a custom maximum size.
     *
     * @param maxHistorySize the maximum number of commands to retain
     * @throws IllegalArgumentException if {@code maxHistorySize <= 0}
     */
    public InputHistoryBuffer(int maxHistorySize) {
        checkArgument(maxHistorySize > 0);
        this.maxHistorySize = maxHistorySize;
        history = new ArrayList<>();
        cursor = 0;
    }

    /**
     * Constructs an input history buffer with the default maximum size
     * ({@value #DEFAULT_HISTORY_SIZE}).
     */

    public InputHistoryBuffer() {
        this(DEFAULT_HISTORY_SIZE);
    }

    /**
     * Moves the cursor to the previous (older) command and returns that command. If
     * the cursor is already at the oldest command, it stays there and returns that
     * command (if any).
     *
     * @return an {@code Optional} containing the command at the new cursor
     *         position, or {@code Optional.empty()} if the history is empty.
     */
    public Optional<String> moveCursorUp() {
        setCursor(cursor + CURSOR_EARLIER);
        return getHistoryAtCursor();
    }

    /**
     * Moves the cursor to the next (newer) command and returns that command. If the
     * cursor is already at the newest command, moving down does nothing (the cursor
     * stays at the newest) and returns that command.
     *
     * @return an {@code Optional} containing the command at the new cursor
     *         position, or {@code Optional.empty()} if the history is empty
     */
    public Optional<String> moveCursorDown() {
        setCursor(cursor + CURSOR_LATER);
        return getHistoryAtCursor();
    }

    /**
     * Adds a new command to the history. Maintains that input history is no more
     * than maxHistorySize. The cursor is reset to the most recent command.
     *
     * @param command the command to add
     * @throws NullPointerException if {@code command} is {@code null}
     */
    public void pushHistory(String command) {
        requireNonNull(command);
        if (history.size() >= maxHistorySize) {
            history.remove(0);
        }
        history.add(command);
        // cursor is set past the end so a subsequent moveCursorUp correctly returns the
        // latest element added
        this.cursor = history.size();
    }

    private void setCursor(int cursor) {
        // clamp cursor within bounds of history
        this.cursor = Math.max(0, Math.min(cursor, history.size() - 1));
    }

    private Optional<String> getHistoryAtCursor() {
        if (cursor >= history.size()) {
            return Optional.empty();
        }
        return Optional.of(history.get(cursor));
    }
}
