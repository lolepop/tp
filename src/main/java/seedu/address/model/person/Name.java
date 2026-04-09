package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS = getNameConstraintsMessage();

    public static final String VALIDATION_REGEX = initValidationRegex();

    public static final String MESSAGE_FIND_NAME_VALIDATE_ERROR = getNameConstraintsMessage();

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    private static String initValidationRegex() {
        String mid = "[a-zA-Z0-9/,']";

        String segment = "[a-zA-Z0-9](?:" + "(?:" + mid + "|(?:[ -](?![ -])))*" + "[a-zA-Z0-9])?";

        return "^" + segment + "(?: \\(" + segment + "\\))?$";
    }

    private static String getNameConstraintsMessage() {
        return "Constraints for names:\n"
                + "- Use only letters, numbers, and symbols: / , - ' ( )\n"
                + "- Cannot be empty or only whitespace.\n"
                + "- Use only a single space or hyphen between words.\n"
                + "- Must only start and end with alphanumeric character.\n"
                + "- Parentheses must be at the end, properly ordered (), and non-empty.\n"
                + "- Examples:\n"
                + "  - John Doe\n"
                + "  - David, Tan Ah Khow\n"
                + "  - Lily-Rose\n"
                + "  - Ronald O'Donald\n"
                + "  - Soh La Min (Su La Min)\n"
                + "  - Child S/O Father";
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
