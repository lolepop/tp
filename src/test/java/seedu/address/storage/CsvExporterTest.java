package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Position;
import seedu.address.model.person.TeachingStaff;
import seedu.address.model.person.TimeSlot;
import seedu.address.model.person.Username;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class CsvExporterTest {

    @TempDir
    public Path tempDir;

    @Test
    public void convertToCSV_studentWithoutTags_returnsValidCSVFormat() {
        Person student = new PersonBuilder()
                .withName("John Doe")
                .withPhone("91234567")
                .withEmail("john@example.com")
                .withUsername("johndoe")
                .withTags()
                .build();

        String csv = CsvExporter.convertToCsv(student);

        assertEquals("John Doe,91234567,johndoe,john@example.com,,Student,", csv);
    }

    @Test
    public void convertToCSV_studentWithTags_includesTagsInCSV() {
        Person student = new PersonBuilder()
                .withName("Alice Smith")
                .withPhone("81234567")
                .withEmail("alice@example.com")
                .withUsername("alicesmith")
                .withTags("cs2103", "tutee")
                .build();

        String csv = CsvExporter.convertToCsv(student);

        assertTrue(csv.contains("Alice Smith"));
        assertTrue(csv.contains("cs2103;tutee") || csv.contains("tutee;cs2103"));
        assertTrue(csv.contains("Student"));
    }

    @Test
    public void convertToCSV_teachingStaffWithoutAvailability_returnsValidCSVFormat() {
        TeachingStaff staff = (TeachingStaff) new PersonBuilder()
                .withName("Prof Benson")
                .withPhone("87654321")
                .withEmail("prof@example.com")
                .withUsername("profbenson")
                .withPosition("Teaching Assistant")
                .withTags()
                .build();

        String csv = CsvExporter.convertToCsv(staff);

        assertEquals("Prof Benson,87654321,profbenson,prof@example.com,,Teaching Assistant,", csv);
    }

    @Test
    public void convertToCSV_teachingStaffWithAvailability_includesTimeSlots() {
        TimeSlot slot1 = new TimeSlot("mon-10-12");
        TimeSlot slot2 = new TimeSlot("wed-14-16");

        TeachingStaff staff = new TeachingStaff(
                new Name("Prof Alice"),
                new Phone("91111111"),
                new Email("protalice@example.com"),
                new Username("profalice"),
                new Position("Professors"),
                Collections.singleton(new Tag("lecturer")),
                java.util.Set.of(slot1, slot2));

        String csv = CsvExporter.convertToCsv(staff);

        assertTrue(csv.contains("Prof Alice"));
        assertTrue(csv.contains("Professors"));
        assertTrue(csv.contains("lecturer"));
        assertTrue(csv.contains("mon-10-12") && csv.contains("wed-14-16"));
    }

    @Test
    public void exportContacts_validFilePath_createsAndWritesFile() throws IOException {
        Model model = new ModelManager();
        model.addPerson(ALICE);
        model.addPerson(BENSON);

        Path filePath = tempDir.resolve("test_export.csv");
        CsvExporter.exportContacts(model, filePath.toString());

        assertTrue(Files.exists(filePath));
        String content = Files.readString(filePath);
        assertTrue(content.contains(CsvExporter.HEADERS));
        assertTrue(content.contains(ALICE.getName().toString()));
        assertTrue(content.contains(BENSON.getName().toString()));
    }

    @Test
    public void exportContacts_nullFilePath_usesDefaultPath() throws IOException {
        Model model = new ModelManager();
        model.addPerson(ALICE);

        try {
            CsvExporter.exportContacts(model, null);

            Path defaultPath = Paths.get(CsvExporter.DEFAULT_FILE_PATH);
            assertTrue(Files.exists(defaultPath));

            String content = Files.readString(defaultPath);
            assertTrue(content.contains(ALICE.getName().toString()));
        } finally {
            // Cleanup
            Files.deleteIfExists(Paths.get(CsvExporter.DEFAULT_FILE_PATH));
        }
    }

    @Test
    public void exportContacts_emptyPersonList_createsFileWithHeadersOnly() throws IOException {
        Model model = new ModelManager();

        Path filePath = tempDir.resolve("empty_export.csv");
        CsvExporter.exportContacts(model, filePath.toString());

        assertTrue(Files.exists(filePath));
        String content = Files.readString(filePath);
        assertEquals(CsvExporter.HEADERS, content);
    }

    @Test
    public void exportContacts_invalidParentDirectory_throwsIOException() {
        Model model = new ModelManager();
        model.addPerson(ALICE);

        String invalidPath = "C:\\NonExistent\\Path\\That\\Does\\Not\\Exist\\export.csv";
        assertThrows(IOException.class, () -> CsvExporter.exportContacts(model, invalidPath));
    }

    @Test
    public void exportContacts_multiplePersons_preservesOrderAndFormat() throws IOException {
        Model model = new ModelManager();
        model.addPerson(ALICE);
        model.addPerson(BENSON);

        Path filePath = tempDir.resolve("multi_export.csv");
        CsvExporter.exportContacts(model, filePath.toString());

        String content = Files.readString(filePath);
        String[] lines = content.split("\n");

        // First line is header
        assertEquals(CsvExporter.HEADERS.trim(), lines[0].trim());

        // Should have 3 lines total (header + 2 persons)
        assertEquals(3, lines.length);
    }

    @Test
    public void exportContacts_fileAlreadyExists_overwritesFile() throws IOException {
        Model model = new ModelManager();
        model.addPerson(ALICE);

        Path filePath = tempDir.resolve("overwrite_test.csv");

        // Create file with initial content
        Files.writeString(filePath, "Old content");
        assertFalse(Files.readString(filePath).contains(ALICE.getName().toString()));

        // Export and overwrite
        CsvExporter.exportContacts(model, filePath.toString());

        String content = Files.readString(filePath);
        assertTrue(content.contains(ALICE.getName().toString()));
        assertFalse(content.contains("Old content"));
    }

    @Test
    public void convertToCSV_outputContainsAllRequiredFields() {
        Person person = new PersonBuilder()
                .withName("Test Person")
                .withPhone("12345678")
                .withEmail("test@example.com")
                .withUsername("testuser")
                .withTags("tag1")
                .build();

        String csv = CsvExporter.convertToCsv(person);

        String[] fields = csv.split(",");
        assertEquals(6, fields.length); // Name, Phone, Username, Email, Tags, Position, Availability
        assertEquals("Test Person", fields[0]);
        assertEquals("12345678", fields[1]);
        assertEquals("testuser", fields[2]);
        assertEquals("test@example.com", fields[3]);
    }

    @Test
    public void exportContacts_headerFormatIsCorrect() throws IOException {
        Model model = new ModelManager();

        Path filePath = tempDir.resolve("header_test.csv");
        CsvExporter.exportContacts(model, filePath.toString());

        String content = Files.readString(filePath);
        assertTrue(content.startsWith("Name,Phone,Username,Email,Tags,Position,Availability"));
    }

}
