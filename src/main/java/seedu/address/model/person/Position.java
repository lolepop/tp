package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Set;

/**
 * Represents a Teaching Staff's position in the address book.
 * Only "Teaching Assistant" and "Professors" are allowed (input may use any letter case; stored canonically).
 * Guarantees: immutable; is valid as declared in {@link #isValidPosition(String)}
 */
public class Position {

    /** Allowed position values. */
    public static final String TEACHING_ASSISTANT = "Teaching Assistant";
    public static final String PROFESSORS = "Professors";

    private static final Set<String> ALLOWED_VALUES = Set.of(TEACHING_ASSISTANT, PROFESSORS);

    public static final String MESSAGE_CONSTRAINTS =
            "Position must be one of: " + String.join(", ", ALLOWED_VALUES)
                    + " (full wording as shown; letter case is ignored).";

    public final String value;

    /**
     * Constructs a {@code Position}.
     *
     * @param position A valid position (Teaching Assistant or Professors); letter case is ignored.
     */
    public Position(String position) {
        requireNonNull(position);
        String trimmed = position.trim();
        String canonical = canonicalize(trimmed);
        checkArgument(canonical != null, MESSAGE_CONSTRAINTS);
        value = canonical;
    }

    /**
     * Returns the canonical allowed value if {@code trimmed} matches one of the allowed positions ignoring case,
     * or {@code null} if none match.
     */
    private static String canonicalize(String trimmed) {
        if (trimmed.isEmpty()) {
            return null;
        }
        if (TEACHING_ASSISTANT.equalsIgnoreCase(trimmed)) {
            return TEACHING_ASSISTANT;
        }
        if (PROFESSORS.equalsIgnoreCase(trimmed)) {
            return PROFESSORS;
        }
        return null;
    }

    /**
     * Returns true if a given string is a valid position (Teaching Assistant or Professors, after trim; letter case
     * ignored).
     *
     * @param test The string to validate; may be null (returns false).
     * @return True if {@code test} is non-null and matches an allowed position after trimming.
     */
    public static boolean isValidPosition(String test) {
        return test != null && canonicalize(test.trim()) != null;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Position)) {
            return false;
        }

        Position otherPosition = (Position) other;
        return value.equals(otherPosition.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
