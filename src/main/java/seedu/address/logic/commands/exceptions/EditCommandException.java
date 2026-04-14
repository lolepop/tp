package seedu.address.logic.commands.exceptions;

import seedu.address.logic.commands.EditCommand;

/**
 * Represents an error which occurs during execution of a {@link EditCommand}.
 */
public class EditCommandException extends RuntimeException {
    public EditCommandException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code EditCommandException} with the specified detail {@code message} and {@code cause}.
     */
    public EditCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
