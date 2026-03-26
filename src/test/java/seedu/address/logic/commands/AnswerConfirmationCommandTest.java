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

    @Test
    public void execute_yesAnswer_noPendingCommandThrowsCommandException() {
        AnswerConfirmationCommand command =
                new AnswerConfirmationCommand(AnswerConfirmationCommand.AnswerType.YES);
        assertCommandFailure(command, model, AnswerConfirmationCommand.MESSAGE_NO_PENDING_COMMAND);
    }

    @Test
    public void execute_noAnswer_noPendingCommandThrowsCommandException() {
        AnswerConfirmationCommand command =
                new AnswerConfirmationCommand(AnswerConfirmationCommand.AnswerType.NO);
        assertCommandFailure(command, model, AnswerConfirmationCommand.MESSAGE_NO_PENDING_COMMAND);
    }

    @Test
    public void execute_noAnswer_returnsCancelledMessage() {
        model.setPendingCommand(new ClearCommand());
        AnswerConfirmationCommand command = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.NO);
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        assertCommandSuccess(command, model, AnswerConfirmationCommand.MESSAGE_COMMAND_CANCELLED, expectedModel);
    }

    @Test
    public void execute_noAnswer_modelUnchanged() {
        model.setPendingCommand(new ClearCommand());
        AnswerConfirmationCommand command = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.NO);
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        assertCommandSuccess(command, model, AnswerConfirmationCommand.MESSAGE_COMMAND_CANCELLED, expectedModel);
        assertEquals(expectedModel.getAddressBook(), model.getAddressBook());
    }

    @Test
    public void execute_yesAnswer_clearsModel() {
        model.setPendingCommand(new ClearCommand());
        AnswerConfirmationCommand command = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.YES);

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setAddressBook(new AddressBook());

        assertCommandSuccess(command, model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_yesAnswer_deletesPerson() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        model.setPendingCommand(new DeleteCommand(INDEX_FIRST_PERSON));
        AnswerConfirmationCommand command = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.YES);

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                seedu.address.logic.Messages.format(personToDelete));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        model.setPendingCommand(new ClearCommand());
        AnswerConfirmationCommand command = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.YES);
        assertTrue(command.equals(command));
    }

    @Test
    public void equals_sameAnswerType_returnsTrue() {
        AnswerConfirmationCommand command1 = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.NO);
        AnswerConfirmationCommand command2 = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.NO);
        assertTrue(command1.equals(command2));
    }

    @Test
    public void equals_differentAnswerType_returnsFalse() {
        AnswerConfirmationCommand yesCommand = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.YES);
        AnswerConfirmationCommand noCommand = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.NO);
        assertFalse(yesCommand.equals(noCommand));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        AnswerConfirmationCommand command = new AnswerConfirmationCommand(
                AnswerConfirmationCommand.AnswerType.NO);
        assertFalse(command.equals("N"));
    }
}
