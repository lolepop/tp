package seedu.address.logic.commands;

import java.util.Objects;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Represents a command that answers a confirmation prompt for a pending command.
 */
public class AnswerConfirmationCommand extends Command implements NullaryCommand {

    public static final String COMMAND_WORD_YES = "Y";
    public static final String COMMAND_WORD_NO = "N";

    public static final String MESSAGE_UNKNOWN_ANSWER = """
            Unknown answer: %s.
            Expected answers are:
                %s (for yes)
                %s (for no)
            """;
    public static final String MESSAGE_COMMAND_CANCELLED =
            "Command Cancelled!";
    public static final String MESSAGE_NO_PENDING_COMMAND = "No pending command to answer for.";

    private final AnswerType answerType;

    /**
     * @param answerType The type of answer provided by the user.
     */
    public AnswerConfirmationCommand(AnswerType answerType) {
        Objects.requireNonNull(answerType);
        this.answerType = answerType;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (model.getPendingCommand() == null) {
            throw new CommandException(MESSAGE_NO_PENDING_COMMAND);
        }

        switch (answerType) {
        case YES -> {
            return model.getPendingCommand().execute(model);
        }
        case NO -> {
            return new CommandResult(MESSAGE_COMMAND_CANCELLED);
        }
        default -> throw new CommandException(String.format(
                MESSAGE_UNKNOWN_ANSWER,
                answerType,
                COMMAND_WORD_YES,
                COMMAND_WORD_NO
        ));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AnswerConfirmationCommand other)) {
            return false;
        }
        return this.answerType == other.answerType;
    }

    /**
     * The type of answer provided by the user for a confirmation prompt.
     */
    public enum AnswerType {
        YES,
        NO
    }
}
