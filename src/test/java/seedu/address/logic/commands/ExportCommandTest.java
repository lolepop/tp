package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.storage.CsvExporter;

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
    @EnabledOnOs({OS.WINDOWS})
    public void execute_invalidFilePath_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        String invalidFilePath = "<bad>.csv";

        ExportCommand exportCommand = new ExportCommand(invalidFilePath);

        CommandException thrown = assertThrows(CommandException.class, () -> exportCommand.execute(model));
        assertEquals(ExportCommand.MESSAGE_IO_EXCEPTION, thrown.getMessage());
    }

    @Test
    @EnabledOnOs({OS.LINUX})
    public void execute_invalidFilePathLinux_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        // Test with null character which is invalid in Linux file paths
        String invalidFilePath = "test" + '\0' + ".csv";

        ExportCommand exportCommand = new ExportCommand(invalidFilePath);

        CommandException thrown = assertThrows(CommandException.class, () -> exportCommand.execute(model));
        assertEquals(ExportCommand.MESSAGE_IO_EXCEPTION, thrown.getMessage());
    }

    @Test
    public void execute_missingDirectory_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        // Test with a path to a non-existent directory
        String missingDirPath = "/nonexistent/directory/contacts.csv";

        ExportCommand exportCommand = new ExportCommand(missingDirPath);

        CommandException thrown = assertThrows(CommandException.class, () -> exportCommand.execute(model));
        assertEquals(ExportCommand.MESSAGE_IO_EXCEPTION, thrown.getMessage());
    }

    @Test
    public void execute_defaultFilePath_success() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        String filePath = CsvExporter.DEFAULT_FILE_PATH;

        ExportCommand exportCommand = new ExportCommand(filePath);
        CommandResult result = exportCommand.execute(model);

        String expectedMessage = String.format(ExportCommand.MESSAGE_SUCCESS, filePath);
        assertEquals(expectedMessage, result.getFeedbackToUser());
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
