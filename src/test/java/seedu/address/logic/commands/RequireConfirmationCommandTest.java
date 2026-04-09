package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains tests for {@code RequireConfirmationCommand}.
 */
public class RequireConfirmationCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

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
        Command mockCommand = new ClearCommand();
        RequireConfirmationCommand requireConfirmationCommand = new RequireConfirmationCommand(userInput, mockCommand);

        CommandResult result = requireConfirmationCommand.execute(model);

        assertEquals(true, result.getFeedbackToUser().contains(userInput));
    }

    @Test
    public void execute_messageFormat_containsCommandWords() throws Exception {
        String userInput = "test input";
        Command mockCommand = new ClearCommand();
        RequireConfirmationCommand requireConfirmationCommand = new RequireConfirmationCommand(userInput, mockCommand);

        CommandResult result = requireConfirmationCommand.execute(model);
        String feedback = result.getFeedbackToUser();

        assertEquals(true, feedback.contains(AnswerConfirmationCommand.COMMAND_WORD_YES));
        assertEquals(true, feedback.contains(AnswerConfirmationCommand.COMMAND_WORD_NO));
    }

    @Test
    public void execute_commandResultIsPending() throws Exception {
        RequireConfirmationCommand requireConfirmationCommand =
                new RequireConfirmationCommand("clear", new ClearCommand());

        CommandResult result = requireConfirmationCommand.execute(model);

        assertTrue(result.isPending());
    }

    @Test
    public void execute_setsPendingCommandOnModel() throws Exception {
        Command pendingCommand = new ClearCommand();
        RequireConfirmationCommand requireConfirmationCommand =
                new RequireConfirmationCommand("clear", pendingCommand);

        assertNull(model.getPendingCommand());
        requireConfirmationCommand.execute(model);
        assertEquals(pendingCommand, model.getPendingCommand());
    }

    @Test
    public void execute_withValidDeleteCommand_success() throws Exception {
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        String userInput = "delete 1";
        RequireConfirmationCommand requireConfirmationCommand =
                new RequireConfirmationCommand(userInput, deleteCommand);

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
    public void execute_withInvalidDeleteCommand_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);
        RequireConfirmationCommand requireConfirmationCommand =
                new RequireConfirmationCommand("delete 999", deleteCommand);

        assertCommandFailure(requireConfirmationCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void getPendingCommand_returnsCorrectCommand() {
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        RequireConfirmationCommand requireConfirmationCommand =
                new RequireConfirmationCommand("delete 1", deleteCommand);

        assertEquals(deleteCommand, requireConfirmationCommand.getPendingCommand());
    }
}

