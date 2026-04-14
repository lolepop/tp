package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.TeachingStaff;
import seedu.address.model.person.TimeSlot;

/**
 * Displays a dashboard showing all teaching staff and their available time slots.
 */
public class TutorDashboardCommand extends Command implements NullaryCommand {

    public static final String COMMAND_WORD = "tutordashboard";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Shows all teaching staff with their available time slots.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Tutor Availability Dashboard (%1$d tutor(s)):\n%2$s";
    public static final String MESSAGE_EMPTY = "No teaching staff found.";
    public static final String MESSAGE_NO_SLOTS = "(no slots set)";

    private static final Logger logger = LogsCenter.getLogger(TutorDashboardCommand.class);

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        List<Person> allPersons = model.getAddressBook().getPersonList();
        List<TeachingStaff> staffList = allPersons.stream()
                .filter(p -> p instanceof TeachingStaff)
                .map(p -> (TeachingStaff) p)
                .collect(Collectors.toList());

        if (staffList.isEmpty()) {
            logger.fine("TutorDashboard: no teaching staff found");
            return new CommandResult(MESSAGE_EMPTY);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < staffList.size(); i++) {
            TeachingStaff staff = staffList.get(i);
            Set<TimeSlot> sorted = new TreeSet<>(staff.getAvailability());
            String slots = sorted.isEmpty()
                    ? MESSAGE_NO_SLOTS
                    : sorted.stream().map(TimeSlot::toDisplayString).collect(Collectors.joining(", "));
            sb.append(i + 1).append(". ").append(staff.getName()).append(": ").append(slots);
            if (i < staffList.size() - 1) {
                sb.append("\n");
            }
        }

        logger.fine("TutorDashboard: showing " + staffList.size() + " staff");
        return new CommandResult(String.format(MESSAGE_SUCCESS, staffList.size(), sb.toString()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof TutorDashboardCommand;
    }
}
