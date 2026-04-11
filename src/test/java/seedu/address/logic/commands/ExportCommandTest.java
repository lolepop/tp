package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code ExportCommand}.
 */
public class ExportCommandTest {

    @TempDir
    public Path tempDir;

    @Test
    public void execute_validFilePath_success() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        String filePath = tempDir.resolve("contacts.csv").toString();

        ExportCommand exportCommand = new ExportCommand(filePath);
        CommandResult result = exportCommand.execute(model);

        String expectedMessage = String.format(ExportCommand.MESSAGE_SUCCESS, filePath);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    @EnabledOnOs({ OS.WINDOWS })
    public void execute_invalidFilePath_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        String invalidFilePath = "<bad>.csv";

        ExportCommand exportCommand = new ExportCommand(invalidFilePath);

        CommandException thrown = assertThrows(CommandException.class, () -> exportCommand.execute(model));
        assertEquals(ExportCommand.MESSAGE_INVALID_PATH_EXCEPTION, thrown.getMessage());
    }

    @Test
    @EnabledOnOs({ OS.LINUX, OS.MAC })
    public void execute_invalidFilePathLinux_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        // Test with null character which is invalid in Linux file paths
        String invalidFilePath = "test" + '\0' + ".csv";

        ExportCommand exportCommand = new ExportCommand(invalidFilePath);

        CommandException thrown = assertThrows(CommandException.class, () -> exportCommand.execute(model));
        assertEquals(ExportCommand.MESSAGE_INVALID_PATH_EXCEPTION, thrown.getMessage());
    }

    @Test
    public void execute_nestedDirectorySingleLevel_success() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        String filePath = tempDir.resolve("output/contacts.csv").toString();

        ExportCommand exportCommand = new ExportCommand(filePath);
        CommandResult result = exportCommand.execute(model);

        String expectedMessage = String.format(ExportCommand.MESSAGE_SUCCESS, filePath);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_nestedDirectoryMultipleLevels_success() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        String filePath = tempDir.resolve("output/data/contacts/export.csv").toString();

        ExportCommand exportCommand = new ExportCommand(filePath);
        CommandResult result = exportCommand.execute(model);

        String expectedMessage = String.format(ExportCommand.MESSAGE_SUCCESS, filePath);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_nestedDirectoryDeepNesting_success() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        String filePath = tempDir.resolve("a/b/c/d/e/f/g/contacts.csv").toString();

        ExportCommand exportCommand = new ExportCommand(filePath);
        CommandResult result = exportCommand.execute(model);

        String expectedMessage = String.format(ExportCommand.MESSAGE_SUCCESS, filePath);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_defaultFilePath_success() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        String filePath = tempDir.resolve("contacts.csv").toString();

        ExportCommand exportCommand = new ExportCommand(filePath);
        CommandResult result = exportCommand.execute(model);

        String expectedMessage = String.format(ExportCommand.MESSAGE_SUCCESS, filePath);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_ioExceptionThrown_throwsCommandException() throws IOException {
        // Create a regular file, then try to treat it as a parent directory
        Path blocker = tempDir.resolve("blocker");
        Files.writeString(blocker, "not a directory");

        // This path asks for "blocker" to be a directory — it isn't, so
        // createDirectories fails
        String badPath = blocker.resolve("contacts.csv").toString();

        ExportCommand command = new ExportCommand(badPath);
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        CommandException thrown = assertThrows(CommandException.class,
                () -> command.execute(model));
        assertEquals(ExportCommand.MESSAGE_IO_EXCEPTION, thrown.getMessage());
    }

    @Test
    public void equals() {
        String filePath1 = "contacts1.csv";
        String filePath2 = "contacts2.csv";

        ExportCommand exportCommand1 = new ExportCommand(filePath1);
        ExportCommand exportCommand2 = new ExportCommand(filePath2);

        // same object -> returns true
        assertTrue(exportCommand1.equals(exportCommand1));

        // same file path -> returns true
        ExportCommand exportCommand1Copy = new ExportCommand(filePath1);
        assertTrue(exportCommand1.equals(exportCommand1Copy));

        // different file paths -> returns false
        assertFalse(exportCommand1.equals(exportCommand2));

        // different types -> returns false
        assertFalse(exportCommand1.equals(1));

        // null -> returns false
        assertFalse(exportCommand1.equals(null));
    }

    @Test
    public void toStringMethod() {
        String filePath = "contacts.csv";
        ExportCommand exportCommand = new ExportCommand(filePath);
        String expected = ExportCommand.class.getCanonicalName() + "{File Path=" + filePath + "}";
        assertEquals(expected, exportCommand.toString());
    }
}
