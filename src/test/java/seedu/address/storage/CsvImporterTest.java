package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.TeachingStaff;
import seedu.address.model.tag.AbstractTag;
import seedu.address.storage.exceptions.InvalidHeaderRowException;

public class CsvImporterTest {

    @TempDir
    public Path tempDir;

    @Test
    public void importContacts_contactsAddedToModel_successful() throws IOException {
        Model expectedModel = new ModelManager();
        expectedModel.addPerson(ALICE);
        expectedModel.addPerson(BOB);

        Model model = new ModelManager();
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
        Files.writeString(filePath, csv);

        assertDoesNotThrow(() -> CsvImporter.importContacts(model, filePath.toString()));
        assertEquals(expectedModel, model);
    }

    @Test
    public void importContacts_onlyValidHeaderRow_successful() throws IOException {
        Model model = new ModelManager();
        String csv = CsvExporter.HEADERS;

        Path filePath = tempDir.resolve("contacts.csv");
        Files.writeString(filePath, csv);

        assertDoesNotThrow(() -> CsvImporter.importContacts(model, filePath.toString()));
    }

    @Test
    public void importContacts_emptyCsvFile_throwsEofException() throws IOException {
        Model model = new ModelManager();
        String csv = "";

        Path filePath = tempDir.resolve("contacts.csv");
        Files.writeString(filePath, csv);

        assertThrows(EOFException.class, () -> CsvImporter.importContacts(model, filePath.toString()));
    }

    @Test
    public void importContacts_invalidHeaderRow_throwsInvalidHeaderRowException() throws IOException {
        Model model = new ModelManager();

        String aliceCsvRep = "Student,";
        aliceCsvRep += String.format("\"%s\",", ALICE.getName().fullName);
        aliceCsvRep += ALICE.getPhone().value + ",";
        aliceCsvRep += ALICE.getUsername().value + ",";
        aliceCsvRep += ALICE.getEmail().value + ",";
        aliceCsvRep += ALICE.getTags().stream().map(AbstractTag::getTagName).collect(Collectors.joining(";"));
        aliceCsvRep += "\n";
        String csv = "2we3r24g54123r4dknefbibrg\n" + aliceCsvRep;

        Path filePath = tempDir.resolve("contacts.csv");
        Files.writeString(filePath, csv);

        assertThrows(InvalidHeaderRowException.class, () -> CsvImporter.importContacts(model, filePath.toString()));
    }

    @Test
    public void importContacts_emptyHeaderRow_throwsInvalidHeaderRowException() throws IOException {
        Model model = new ModelManager();
        String aliceCsvRep = "Student,";
        aliceCsvRep += String.format("\"%s\",", ALICE.getName().fullName);
        aliceCsvRep += ALICE.getPhone().value + ",";
        aliceCsvRep += ALICE.getUsername().value + ",";
        aliceCsvRep += ALICE.getEmail().value + ",";
        aliceCsvRep += ALICE.getTags().stream().map(AbstractTag::getTagName).collect(Collectors.joining(";"));
        aliceCsvRep += "\n";
        String csv = "\n" + aliceCsvRep;

        Path filePath = tempDir.resolve("contacts.csv");
        Files.writeString(filePath, csv);

        assertThrows(InvalidHeaderRowException.class, () -> CsvImporter.importContacts(model, filePath.toString()));
    }

    @Test
    public void importContacts_noHeaderRow_throwsInvalidHeaderRowException() throws IOException {
        Model model = new ModelManager();
        String aliceCsvRep = "Student,";
        aliceCsvRep += String.format("\"%s\",", ALICE.getName().fullName);
        aliceCsvRep += ALICE.getPhone().value + ",";
        aliceCsvRep += ALICE.getUsername().value + ",";
        aliceCsvRep += ALICE.getEmail().value + ",";
        aliceCsvRep += ALICE.getTags().stream().map(AbstractTag::getTagName).collect(Collectors.joining(";"));
        aliceCsvRep += "\n";

        Path filePath = tempDir.resolve("contacts.csv");
        Files.writeString(filePath, aliceCsvRep);

        assertThrows(InvalidHeaderRowException.class, () -> CsvImporter.importContacts(model, filePath.toString()));
    }

    @Test
    @EnabledOnOs(OS.WINDOWS) // the paths here are only considered invalid in Windows, valid in Linux and MacOS
    public void importContacts_windowsInvalidFilePath_throwsInvalidPathException() {
        Model model = new ModelManager();
        String invalidFilePath = "<bad>.csv";
        assertThrows(InvalidPathException.class, () -> CsvImporter.importContacts(model, invalidFilePath));
    }

    @Test
    @EnabledOnOs({OS.LINUX, OS.MAC})
    public void importContacts_linuxAndMacInvalidFilePath_throwsInvalidPathException() {
        Model model = new ModelManager();
        String invalidFilePath = "invalid" + '\0' + ".csv";
        assertThrows(InvalidPathException.class, () -> CsvImporter.importContacts(model, invalidFilePath));
    }
}
