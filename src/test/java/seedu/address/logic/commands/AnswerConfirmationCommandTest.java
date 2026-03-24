package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains unit tests for {@code AnswerConfirmationCommand}.
 */
public class AnswerConfirmationCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    // ==================== execute() — null pending command ====================

    @Test
    public void execute_noPendingCommand_yesAnswer_throwsCommandException() {
        AnswerConfirmationCommand command =
                new AnswerConfirmationCommand(AnswerConfirmationCommand.AnswerType.YES);
        assertCommandFailure(command, model, AnswerConfirmationCommand.MESSAGE_NO_PENDING_COMMAND);
    }

    @Test
    public void execute_noPendingCommand_noAnswer_throwsCommandException() {
        AnswerConfirmationCommand command =
                new AnswerConfirmationCommand(AnswerConfirmationCommand.AnswerType.NO);
        assertCommandFailure(command, model, AnswerConfirmationCommand.MESSAGE_NO_PENDING_COMMAND);
    }

    // ==================== execute() — NO answer ====================

    @Test
    public void execute_noAnswer_returnsCancelledMessage() {
        AnswerConfirmationCommand command = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.NO, new ClearCommand());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        assertCommandSuccess(command, model, AnswerConfirmationCommand.MESSAGE_COMMAND_CANCELLED, expectedModel);
    }

    @Test
    public void execute_noAnswer_modelUnchanged() {
        AnswerConfirmationCommand command = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.NO, new ClearCommand());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        assertCommandSuccess(command, model, AnswerConfirmationCommand.MESSAGE_COMMAND_CANCELLED, expectedModel);
        assertEquals(expectedModel.getAddressBook(), model.getAddressBook());
    }

    // ==================== execute() — YES answer ====================

    @Test
    public void execute_yesAnswer_withPendingClearCommand_clearsModel() {
        AnswerConfirmationCommand command = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.YES, new ClearCommand());

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setAddressBook(new AddressBook());

        assertCommandSuccess(command, model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_yesAnswer_withPendingDeleteCommand_deletesPerson() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        AnswerConfirmationCommand command = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.YES, new DeleteCommand(INDEX_FIRST_PERSON));

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                seedu.address.logic.Messages.format(personToDelete));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    // ==================== getAnswerType() ====================

    @Test
    public void getAnswerType_yesAnswer_returnsYes() {
        AnswerConfirmationCommand command =
                new AnswerConfirmationCommand(AnswerConfirmationCommand.AnswerType.YES);
        assertEquals(AnswerConfirmationCommand.AnswerType.YES, command.getAnswerType());
    }

    @Test
    public void getAnswerType_noAnswer_returnsNo() {
        AnswerConfirmationCommand command =
                new AnswerConfirmationCommand(AnswerConfirmationCommand.AnswerType.NO);
        assertEquals(AnswerConfirmationCommand.AnswerType.NO, command.getAnswerType());
    }

    // ==================== equals() ====================

    @Test
    public void equals_sameObject_returnsTrue() {
        AnswerConfirmationCommand command = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.YES, new ClearCommand());
        assertTrue(command.equals(command));
    }

    @Test
    public void equals_sameAnswerTypeAndPendingCommand_returnsTrue() {
        ClearCommand clearCommand = new ClearCommand();
        AnswerConfirmationCommand command1 = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.NO, clearCommand);
        AnswerConfirmationCommand command2 = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.NO, clearCommand);
        assertTrue(command1.equals(command2));
    }

    @Test
    public void equals_differentAnswerType_returnsFalse() {
        AnswerConfirmationCommand yesCommand = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.YES, new ClearCommand());
        AnswerConfirmationCommand noCommand = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.NO, new ClearCommand());
        assertFalse(yesCommand.equals(noCommand));
    }

    @Test
    public void equals_differentPendingCommand_returnsFalse() {
        AnswerConfirmationCommand command1 = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.YES, new ClearCommand());
        AnswerConfirmationCommand command2 = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.YES, new DeleteCommand(INDEX_FIRST_PERSON));
        assertFalse(command1.equals(command2));
    }

    @Test
    public void equals_nullPendingCommandVsNonNull_returnsFalse() {
        AnswerConfirmationCommand withPending = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.NO, new ClearCommand());
        AnswerConfirmationCommand withoutPending = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.NO);
        assertFalse(withPending.equals(withoutPending));
    }

    @Test
    public void equals_null_returnsFalse() {
        AnswerConfirmationCommand command = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.NO, new ClearCommand());
        assertFalse(command.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        AnswerConfirmationCommand command = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.NO, new ClearCommand());
        assertFalse(command.equals("N"));
    }
}
