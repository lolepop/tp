package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Represents a command that requires user confirmation before execution.
 * When executed, it prompts the user to confirm
 */
public class RequireConfirmationCommand extends Command {

    public static final String MESSAGE_REQUIRE_CONFIRMATION = """
            Are you sure you want to execute the following command?
            "%s"
            Please type %s to confirm or %s to cancel.
            """;

    private final String userInput;
    private final Command pendingCommand;

    /**
     * @param userInput the command text input by the user
     * @param pendingCommand the pending command to be executed if the user confirms
     */
    public RequireConfirmationCommand(String userInput, Command pendingCommand) {
        this.userInput = userInput;
        this.pendingCommand = pendingCommand;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        model.setPendingCommand(pendingCommand);
        return new CommandResult(String.format(
                MESSAGE_REQUIRE_CONFIRMATION,
                userInput,
                AnswerConfirmationCommand.COMMAND_WORD_YES,
                AnswerConfirmationCommand.COMMAND_WORD_NO
        ), false, false, true);
    }

    /**
     * For tests only
     */
    public Command getPendingCommand() {
        return pendingCommand;
    }
}
