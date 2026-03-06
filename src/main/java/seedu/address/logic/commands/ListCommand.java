package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.model.Model;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all contacts\nShowing %1$d contacts";
    public static final String MESSAGE_EMPTY = "No contacts found. Add your first contact to get started!";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all contacts in the address book.\n"
            + "The list command does not take any parameters.\n"
            + "Format: " + COMMAND_WORD;


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        int size = model.getFilteredPersonList().size();
        if (size == 0) {
            return new CommandResult(MESSAGE_EMPTY);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, size));
    }
}
