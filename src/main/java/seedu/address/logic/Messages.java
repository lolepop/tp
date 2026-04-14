package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;
import seedu.address.model.person.TeachingStaff;
import seedu.address.model.person.TimeSlot;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_ARGUMENT_NUMBER =
            "%1$s command should not have more than %2$d arguments";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_ADD_STUDENT_DISALLOWS_POSITION =
            "Adding a student does not accept pos/. Use \"add staff\" for teaching staff, or remove pos/.";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Username: ")
                .append(person.getUsername());
        if (person instanceof TeachingStaff staff) {
            builder.append("; Position: ")
                    .append(staff.getPosition());
            if (!staff.getAvailability().isEmpty()) {
                builder.append("; Availability: ");
                staff.getAvailability().stream()
                        .sorted()
                        .map(TimeSlot::toDisplayString)
                        .forEach(s -> builder.append("[").append(s).append("] "));
            }
        }
        builder.append("; Tags: ");
        person.getTags().stream()
                .sorted(java.util.Comparator.comparing(Object::toString))
                .forEach(builder::append);
        return builder.toString();
    }

}
