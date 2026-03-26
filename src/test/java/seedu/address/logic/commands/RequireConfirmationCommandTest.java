package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

/**
 * Contains tests for {@code RequireConfirmationCommand}.
 */
public class RequireConfirmationCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void constructor_validInputs_success() {
        String userInput = "clear";
        Command mockCommand = new ClearCommand();

        RequireConfirmationCommand requireConfirmationCommand = new RequireConfirmationCommand(userInput, mockCommand);

        assertNotNull(requireConfirmationCommand);
        assertEquals(mockCommand, requireConfirmationCommand.getPendingCommand());
    }

    @Test
    public void execute_success() {
        String userInput = "clear";
        Command mockCommand = new ClearCommand();
        RequireConfirmationCommand requireConfirmationCommand = new RequireConfirmationCommand(userInput, mockCommand);

        String expectedFeedback = String.format(
                RequireConfirmationCommand.MESSAGE_REQUIRE_CONFIRMATION,
                userInput,
                AnswerConfirmationCommand.COMMAND_WORD_YES,
                AnswerConfirmationCommand.COMMAND_WORD_NO
        );

        CommandResult expectedCommandResult = new CommandResult(expectedFeedback, false, false, true);
        assertCommandSuccess(requireConfirmationCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_messageFormat_containsUserInput() throws Exception {
        String userInput = "special command input";
        Command mockCommand = new HelpCommand();
        RequireConfirmationCommand requireConfirmationCommand = new RequireConfirmationCommand(userInput, mockCommand);

        CommandResult result = requireConfirmationCommand.execute(model);

        assertEquals(true, result.getFeedbackToUser().contains(userInput));
    }

    @Test
    public void execute_messageFormat_containsCommandWords() throws Exception {
        String userInput = "test input";
        Command mockCommand = new HelpCommand();
        RequireConfirmationCommand requireConfirmationCommand = new RequireConfirmationCommand(userInput, mockCommand);

        CommandResult result = requireConfirmationCommand.execute(model);
        String feedback = result.getFeedbackToUser();

        assertEquals(true, feedback.contains(AnswerConfirmationCommand.COMMAND_WORD_YES));
        assertEquals(true, feedback.contains(AnswerConfirmationCommand.COMMAND_WORD_NO));
    }
}

