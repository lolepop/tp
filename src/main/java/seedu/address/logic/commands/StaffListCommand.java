package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;
import seedu.address.model.person.TeachingStaff;

/**
 * Lists all teaching staff in the address book to the user.
 */
public class StaffListCommand extends Command implements NullaryCommand {

    public static final String COMMAND_WORD = "staffslist";

    public static final String MESSAGE_SUCCESS = "Listed all teaching staff\nShowing %1$d contacts";
    public static final String MESSAGE_EMPTY = "No teaching staff found.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(person -> person instanceof TeachingStaff);
        int size = model.getFilteredPersonList().size();
        if (size == 0) {
            return new CommandResult(MESSAGE_EMPTY);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, size));
    }
}
