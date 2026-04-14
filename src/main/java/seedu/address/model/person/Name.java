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
        String namePart = "[a-zA-Z0-9](?:[a-zA-Z0-9']*[a-zA-Z0-9])?";

        String separator = "(?:[ /-]|[.,] )";

        String nameSequence = namePart + "(?:" + separator + namePart + ")*\\.?";

        return "^" + nameSequence + "(?: \\(" + nameSequence + "\\))?$";
    }

    private static String getNameConstraintsMessage() {
        return "Constraints for names:\n"
                + "- Use only letters, numbers, and symbols: '/', ',', '-', ''', '(', ')' and '.'\n"
                + "- Cannot be empty or only whitespace.\n"
                + "- Between words, use either:\n"
                + "    - a single space ' ',\n"
                + "    - hyphen '-',\n"
                + "    - forward slash '/',\n"
                + "    - comma and space ', ',\n"
                + "    - period and space '. ',\n"
                + "- Must start with an alphanumeric character.\n"
                + "- Must end with either alphanumeric character, closing bracket ')' or period '.'.\n"
                + "- Parentheses must be at the end, properly ordered, i.e, open bracket '(' "
                + "must always be followed with close bracket ')', and non-empty.\n"
                + "Valid Examples:\n"
                + "- John Doe\n"
                + "- David, Tan Ah Khow\n"
                + "- Lily-Rose\n"
                + "- Ronald O'Donald\n"
                + "- Soh La Min (Su La Min)\n"
                + "- Child S/O Father\n"
                + "- Donald Trump Sr.\n"
                + "- J. R. R. Tolkien";
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
