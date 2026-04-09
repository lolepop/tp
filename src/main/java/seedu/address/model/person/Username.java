package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's username in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidUsername(String)}
 */
public class Username {

    public static final String MESSAGE_CONSTRAINTS =
            "Usernames should only contain alphanumeric characters and should not be blank";

    public static final String VALIDATION_REGEX = "[a-zA-Z0-9]+";

    public static final String MESSAGE_FIND_USERNAME_VALIDATE_ERROR =
            "Usernames should only contain alphanumeric characters";
    public final String value;

    /**
     * Constructs a {@code Username}.
     *
     * @param username A valid username.
     */
    public Username(String username) {
        requireNonNull(username);
        checkArgument(isValidUsername(username), MESSAGE_CONSTRAINTS);
        value = username;
    }

    /**
     * Returns true if a given string is a valid username.
     */
    public static boolean isValidUsername(String test) {
        return test.matches(VALIDATION_REGEX);
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
        if (!(other instanceof Username)) {
            return false;
        }

        Username otherUsername = (Username) other;
        return value.equals(otherUsername.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
