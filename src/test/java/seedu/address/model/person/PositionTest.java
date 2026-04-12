package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PositionTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Position(null));
    }

    @Test
    public void constructor_invalidPosition_throwsIllegalArgumentException() {
        String invalidPosition = "";
        assertThrows(IllegalArgumentException.class, () -> new Position(invalidPosition));
    }

    @Test
    public void constructor_caseInsensitiveInput_storesCanonicalValue() {
        assertEquals("Teaching Assistant", new Position("teaching assistant").value);
        assertEquals("Professors", new Position("PROFESSORS").value);
    }

    @Test
    public void isValidPosition() {
        // null position
        assertFalse(Position.isValidPosition(null));

        // invalid positions
        assertFalse(Position.isValidPosition("")); // empty string
        assertFalse(Position.isValidPosition(" ")); // spaces only
        assertFalse(Position.isValidPosition("Lecturer"));
        assertFalse(Position.isValidPosition("Professor")); // singular not allowed
        assertFalse(Position.isValidPosition("TA"));

        // valid positions
        assertTrue(Position.isValidPosition("Teaching Assistant"));
        assertTrue(Position.isValidPosition("Professors"));
        assertTrue(Position.isValidPosition("teaching assistant"));
        assertTrue(Position.isValidPosition("PROFESSORS"));
        assertTrue(Position.isValidPosition(" Teaching Assistant ")); // trimmed
        assertTrue(Position.isValidPosition(" Professors ")); // trimmed
    }

    @Test
    public void equals() {
        Position position = new Position("Teaching Assistant");

        // same values -> returns true
        assertTrue(position.equals(new Position("Teaching Assistant")));

        // same canonical value after case-insensitive input -> returns true
        assertTrue(position.equals(new Position("teaching assistant")));

        // same object -> returns true
        assertTrue(position.equals(position));

        // null -> returns false
        assertFalse(position.equals(null));

        // different types -> returns false
        assertFalse(position.equals(5.0f));

        // different values -> returns false
        assertFalse(position.equals(new Position("Professors")));
    }
}
