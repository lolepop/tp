package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.TeachingStaff;
import seedu.address.model.tag.AbstractTag;
import seedu.address.storage.CsvExporter;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code ImportCommand}.
 */
public class ImportCommandTest {

    @TempDir
    public Path tempDir;

    @Test
    public void execute_validFilePath_success() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        String aliceCsvRep = "Student,";
        aliceCsvRep += String.format("\"%s\",", ALICE.getName().fullName);
        aliceCsvRep += ALICE.getPhone().value + ",";
        aliceCsvRep += ALICE.getUsername().value + ",";
        aliceCsvRep += ALICE.getEmail().value + ",";
        aliceCsvRep += ALICE.getTags().stream().map(AbstractTag::getTagName).collect(Collectors.joining(";"));
        aliceCsvRep += "\n";

        TeachingStaff bob = (TeachingStaff) BOB;
        String bobCsvRep = bob.getPosition().value + ",";
        bobCsvRep += String.format("\"%s\",", bob.getName().fullName);
        bobCsvRep += bob.getPhone().value + ",";
        bobCsvRep += bob.getUsername().value + ",";
        bobCsvRep += bob.getEmail().value + ",";
        // extra trailing comma present because bob has no availability
        bobCsvRep += bob.getTags().stream().map(AbstractTag::getTagName).collect(Collectors.joining(";")) + ",";
        bobCsvRep += "\n";
        String csv = CsvExporter.HEADERS + aliceCsvRep + bobCsvRep;

        Path filePath = tempDir.resolve("contacts.csv");
        assertDoesNotThrow(() -> Files.writeString(filePath, csv));

        ImportCommand importCommand = new ImportCommand(filePath.toString());
        CommandResult result = importCommand.execute(model);

        String expectedMessage = String.format(ImportCommand.MESSAGE_SUCCESS, filePath);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }


    @Test
    @EnabledOnOs(OS.WINDOWS) // the paths here are only considered invalid in Windows, valid in Linux and MacOS
    public void execute_windowsInvalidFilePath_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        String invalidFilePath = "<bad>.csv";
        ImportCommand importCommand = new ImportCommand(invalidFilePath);
        assertThrows(
                CommandException.class,
                ImportCommand.MESSAGE_INVALID_PATH_EXCEPTION, () -> importCommand.execute(model)
        );
    }

    @Test
    @EnabledOnOs({OS.LINUX, OS.MAC})
    public void execute_linuxAndMacInvalidFilePath_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        String invalidFilePath = "invalid" + '\0' + ".csv";
        ImportCommand importCommand = new ImportCommand(invalidFilePath);
        assertThrows(
                CommandException.class,
                ImportCommand.MESSAGE_INVALID_PATH_EXCEPTION, () -> importCommand.execute(model)
        );
    }

    @Test
    public void execute_emptyCsvFile_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        String csv = "";
        Path filePath = tempDir.resolve("contacts.csv");
        assertDoesNotThrow(() -> Files.writeString(filePath, csv));

        ImportCommand importCommand = new ImportCommand(filePath.toString());
        assertThrows(
                CommandException.class,
                ImportCommand.MESSAGE_DESERIALISE_EXCEPTION, () -> importCommand.execute(model)
        );
    }


    @Test
    public void execute_noHeaderRowCsvFile_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        String aliceCsvRep = "Student,";
        aliceCsvRep += String.format("\"%s\",", ALICE.getName().fullName);
        aliceCsvRep += ALICE.getPhone().value + ",";
        aliceCsvRep += ALICE.getUsername().value + ",";
        aliceCsvRep += ALICE.getEmail().value + ",";
        aliceCsvRep += ALICE.getTags().stream().map(AbstractTag::getTagName).collect(Collectors.joining(";"));
        aliceCsvRep += "\n";

        String csv = aliceCsvRep;

        Path filePath = tempDir.resolve("contacts.csv");
        assertDoesNotThrow(() -> Files.writeString(filePath, csv));

        ImportCommand importCommand = new ImportCommand(filePath.toString());
        assertThrows(
                CommandException.class,
                ImportCommand.MESSAGE_DESERIALISE_EXCEPTION, () -> importCommand.execute(model)
        );
    }


    @Test
    public void execute_invalidHeaderRowCsvFile_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        String aliceCsvRep = "Student,";
        aliceCsvRep += String.format("\"%s\",", ALICE.getName().fullName);
        aliceCsvRep += ALICE.getPhone().value + ",";
        aliceCsvRep += ALICE.getUsername().value + ",";
        aliceCsvRep += ALICE.getEmail().value + ",";
        aliceCsvRep += ALICE.getTags().stream().map(AbstractTag::getTagName).collect(Collectors.joining(";"));
        aliceCsvRep += "\n";

        String invalidHeader = "swndf3eugb4ub\n";

        String csv = invalidHeader + aliceCsvRep;

        Path filePath = tempDir.resolve("contacts.csv");
        assertDoesNotThrow(() -> Files.writeString(filePath, csv));

        ImportCommand importCommand = new ImportCommand(filePath.toString());
        assertThrows(
                CommandException.class,
                ImportCommand.MESSAGE_DESERIALISE_EXCEPTION, () -> importCommand.execute(model)
        );
    }


    @Test
    public void execute_validHeaderOnly_success() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        String csv = CsvExporter.HEADERS;

        Path filePath = tempDir.resolve("contacts.csv");
        assertDoesNotThrow(() -> Files.writeString(filePath, csv));

        ImportCommand importCommand = new ImportCommand(filePath.toString());
        CommandResult result = importCommand.execute(model);

        String expectedMessage = String.format(ImportCommand.MESSAGE_SUCCESS, filePath);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }


    @Test
    public void equals() {
        String filePath1 = "contacts1.csv";
        String filePath2 = "contacts2.csv";

        ImportCommand importCommand1 = new ImportCommand(filePath1);
        ImportCommand importCommand2 = new ImportCommand(filePath2);

        // same object -> returns true
        assertTrue(importCommand1.equals(importCommand1));

        // same file path -> returns true
        ImportCommand importCommand1Copy = new ImportCommand(filePath1);
        assertTrue(importCommand1.equals(importCommand1Copy));

        // different file paths -> returns false
        assertFalse(importCommand1.equals(importCommand2));

        // different types -> returns false
        assertFalse(importCommand1.equals(1));

        // null -> returns false
        assertFalse(importCommand1.equals(null));
    }

    @Test
    public void toStringMethod() {
        String filePath = "contacts.csv";
        ImportCommand importCommand = new ImportCommand(filePath);
        String expected = ImportCommand.class.getCanonicalName() + "{File Path=" + filePath + "}";
        assertEquals(expected, importCommand.toString());
    }
}
