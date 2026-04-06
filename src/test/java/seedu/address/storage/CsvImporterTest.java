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
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
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
import seedu.address.model.tag.AbstractTag;
import seedu.address.model.tag.TagFactory;
import seedu.address.storage.exceptions.DeserialisePersonException;
import seedu.address.storage.exceptions.InvalidHeaderRowException;
import seedu.address.testutil.PersonBuilder;

public class CsvImporterTest {

    @TempDir
    public Path tempDir;

    @Test
    public void deserialisePerson_studentStrRepWithoutTags_returnsValidPerson() {
        String studentStrRep = "Student,John Doe,91234567,johndoe,john@example.com,";
        Person student = assertDoesNotThrow(() ->
                        CsvImporter.deserialisePerson(studentStrRep),
                "Valid student csv string rep without tags should not throw DeserialisePersonException");

        Person expectedStudent = new PersonBuilder()
                .withName("John Doe")
                .withPhone("91234567")
                .withUsername("johndoe")
                .withEmail("john@example.com")
                .withTags()
                .build();
        assertEquals(expectedStudent, student);
    }

    @Test
    public void deserialisePerson_studentStrRepWithTags_returnsValidPerson() {
        String studentStrRep = "Student,Alice Smith,81234567,alicesmith,alice@example.com,cs2103;tutee";
        Person student = assertDoesNotThrow(() ->
                        CsvImporter.deserialisePerson(studentStrRep),
                "Valid student csv string rep with tags should not throw DeserialisePersonException");

        Person expectedStudent = new PersonBuilder()
                .withName("Alice Smith")
                .withPhone("81234567")
                .withEmail("alice@example.com")
                .withUsername("alicesmith")
                .withTags("cs2103", "tutee")
                .build();
        assertEquals(expectedStudent, student);
    }

    @Test
    public void deserialisePerson_teachingStaffNoAvailNoTags_returnsValidTeachingStaff() {
        String staffStrRep = "Teaching Assistant,Prof Benson,87654321,profbenson,prof@example.com,,";
        Person staff = assertDoesNotThrow(() ->
                        CsvImporter.deserialisePerson(staffStrRep),
                "Valid staff csv string rep with no avail, no tags should not throw DeserialisePersonException");
        TeachingStaff expectedStaff = (TeachingStaff) new PersonBuilder()
                .withName("Prof Benson")
                .withPhone("87654321")
                .withEmail("prof@example.com")
                .withUsername("profbenson")
                .withPosition("Teaching Assistant")
                .withTags()
                .build();
        assertEquals(expectedStaff, staff);
    }

    @Test
    public void deserialisePerson_teachingStaffNoAvailWithTags_returnsValidTeachingStaff() {
        String staffStrRep = "Teaching Assistant,Prof Benson,87654321,profbenson,prof@example.com,cs2103;tutee,";
        Person staff = assertDoesNotThrow(() ->
                        CsvImporter.deserialisePerson(staffStrRep),
                "Valid staff csv string rep with no avail, with tags should not throw DeserialisePersonException");
        TeachingStaff expectedStaff = (TeachingStaff) new PersonBuilder()
                .withName("Prof Benson")
                .withPhone("87654321")
                .withEmail("prof@example.com")
                .withUsername("profbenson")
                .withPosition("Teaching Assistant")
                .withTags("cs2103", "tutee")
                .build();
        assertEquals(expectedStaff, staff);
    }

    @Test
    public void deserialisePerson_teachingStaffWithAvailNoTags_returnsValidTeachingStaff() {
        TimeSlot slot1 = new TimeSlot("mon-10-12");
        TimeSlot slot2 = new TimeSlot("wed-14-16");
        TeachingStaff expectedStaff = new TeachingStaff(
                new Name("Prof Alice"),
                new Phone("91111111"),
                new Email("profalice@example.com"),
                new Username("profalice"),
                new Position("Professors"),
                Set.of(),
                Set.of(slot1, slot2));
        String staffStrRep = "Professors,Prof Alice,91111111,profalice,profalice@example.com,,mon-10-12;wed-14-16";

        Person staff = assertDoesNotThrow(() ->
                        CsvImporter.deserialisePerson(staffStrRep),
                "Valid staff csv string rep with avail, no tags should not throw DeserialisePersonException");
        assertEquals(expectedStaff, staff);
    }

    @Test
    public void deserialisePerson_teachingStaffWithAvailWithTags_returnsValidTeachingStaff() {
        TimeSlot slot1 = new TimeSlot("mon-10-12");
        TimeSlot slot2 = new TimeSlot("wed-14-16");
        TeachingStaff expectedStaff = new TeachingStaff(
                new Name("Prof Alice"),
                new Phone("91111111"),
                new Email("profalice@example.com"),
                new Username("profalice"),
                new Position("Professors"),
                Set.of(TagFactory.create("lecturer")),
                Set.of(slot1, slot2));
        String staffStrRep =
                "Professors,Prof Alice,91111111,profalice,profalice@example.com,lecturer,mon-10-12;wed-14-16";

        Person staff = assertDoesNotThrow(() ->
                        CsvImporter.deserialisePerson(staffStrRep),
                "Valid staff csv string rep with avail and tags should not throw DeserialisePersonException");
        assertEquals(expectedStaff, staff);
    }

    @Test
    public void deserialisePerson_noPosField_throwsDeserialisePersonException() {
        String personStrRep = "";
        assertThrows(DeserialisePersonException.class, () -> CsvImporter.deserialisePerson(personStrRep));
    }

    @Test
    public void deserialisePerson_noNameField_throwsDeserialisePersonException() {
        String personStrRep = "Student";
        assertThrows(DeserialisePersonException.class, () -> CsvImporter.deserialisePerson(personStrRep));
    }

    @Test
    public void deserialisePerson_noPhoneField_throwsDeserialisePersonException() {
        String personStrRep = "Student,Prof Alice";
        assertThrows(DeserialisePersonException.class, () -> CsvImporter.deserialisePerson(personStrRep));
    }

    @Test
    public void deserialisePerson_noUsernameField_throwsDeserialisePersonException() {
        String personStrRep = "Student,Prof Alice,91111111";
        assertThrows(DeserialisePersonException.class, () -> CsvImporter.deserialisePerson(personStrRep));
    }

    @Test
    public void deserialisePerson_noEmailField_throwsDeserialisePersonException() {
        String personStrRep = "Student,Prof Alice,91111111,profalice";
        assertThrows(DeserialisePersonException.class, () -> CsvImporter.deserialisePerson(personStrRep));
    }

    @Test
    public void deserialisePerson_emptyPosField_throwsDeserialisePersonException() {
        String personStrRep = ",Prof Alice,91111111,profalice,profalice@example.com,lecturer,mon-10-12;wed-14-16";
        assertThrows(DeserialisePersonException.class, () -> CsvImporter.deserialisePerson(personStrRep));
    }

    @Test
    public void deserialisePerson_invalidPosField_throwsDeserialisePersonException() {
        String personStrRep =
                "invalid,Prof Alice,91111111,profalice,profalice@example.com,lecturer,mon-10-12;wed-14-16";
        assertThrows(DeserialisePersonException.class, () -> CsvImporter.deserialisePerson(personStrRep));
    }

    @Test
    public void deserialisePerson_emptyNameField_throwsDeserialisePersonException() {
        String personStrRep = "Student,,91111111,profalice,profalice@example.com,lecturer";
        assertThrows(DeserialisePersonException.class, () -> CsvImporter.deserialisePerson(personStrRep));
    }

    @Test
    public void deserialisePerson_invalidNameField_throwsDeserialisePersonException() {
        String personStrRep = "Student, whitespaceAsFirstChar,91111111,profalice,profalice@example.com,lecturer";
        assertThrows(DeserialisePersonException.class, () -> CsvImporter.deserialisePerson(personStrRep));
    }

    @Test
    public void deserialisePerson_emptyPhoneField_throwsDeserialisePersonException() {
        String personStrRep = "Student,Alice,,profalice,profalice@example.com,lecturer";
        assertThrows(DeserialisePersonException.class, () -> CsvImporter.deserialisePerson(personStrRep));
    }

    @Test
    public void deserialisePerson_invalidPhoneField_throwsDeserialisePersonException() {
        String personStrRep = "Student,Alice,invalidphone,profalice,profalice@example.com,lecturer";
        assertThrows(DeserialisePersonException.class, () -> CsvImporter.deserialisePerson(personStrRep));
    }

    @Test
    public void deserialisePerson_emptyUsernameField_throwsDeserialisePersonException() {
        String personStrRep = "Student,Alice,91111111,,profalice@example.com,lecturer";
        assertThrows(DeserialisePersonException.class, () -> CsvImporter.deserialisePerson(personStrRep));
    }

    @Test
    public void deserialisePerson_invalidUsernameField_throwsDeserialisePersonException() {
        String personStrRep = "Student,Alice,91111111,**invalidusername**,profalice@example.com,lecturer";
        assertThrows(DeserialisePersonException.class, () -> CsvImporter.deserialisePerson(personStrRep));
    }

    @Test
    public void deserialisePerson_emptyEmailField_throwsDeserialisePersonException() {
        String personStrRep = "Student,Alice,91111111,profalice,,lecturer";
        assertThrows(DeserialisePersonException.class, () -> CsvImporter.deserialisePerson(personStrRep));
    }

    @Test
    public void deserialisePerson_invalidEmailField_throwsDeserialisePersonException() {
        String personStrRep = "Student,Alice,91111111,profalice,invalidemail,lecturer";
        assertThrows(DeserialisePersonException.class, () -> CsvImporter.deserialisePerson(personStrRep));
    }

    @Test
    public void deserialisePerson_invalidTagField_throwsDeserialisePersonException() {
        String personStrRep = "Student,Alice,91111111,profalice,profalice@example.com,*not-alphanumeric";
        assertThrows(DeserialisePersonException.class, () -> CsvImporter.deserialisePerson(personStrRep));
    }

    @Test
    public void deserialisePerson_invalidTimeslotField_throwsDeserialisePersonException() {
        String personStrRep = "Professors,Alice,91111111,profalice,invalidemail,lecturer,invalid-timeslot";
        assertThrows(DeserialisePersonException.class, () -> CsvImporter.deserialisePerson(personStrRep));
    }

    @Test
    public void importContacts_contactsAddedToModel_successful() throws IOException {
        Model expectedModel = new ModelManager();
        expectedModel.addPerson(ALICE);
        expectedModel.addPerson(BOB);

        Model model = new ModelManager();
        String aliceCsvRep = "Student,";
        aliceCsvRep += ALICE.getName().fullName + ",";
        aliceCsvRep += ALICE.getPhone().value + ",";
        aliceCsvRep += ALICE.getUsername().value + ",";
        aliceCsvRep += ALICE.getEmail().value + ",";
        aliceCsvRep += ALICE.getTags().stream().map(AbstractTag::getTagName).collect(Collectors.joining(";"));
        aliceCsvRep += "\n";

        TeachingStaff bob = (TeachingStaff) BOB;
        String bobCsvRep = bob.getPosition().value + ",";
        bobCsvRep += bob.getName().fullName + ",";
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
        aliceCsvRep += ALICE.getName().fullName + ",";
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
        aliceCsvRep += ALICE.getName().fullName + ",";
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
        aliceCsvRep += ALICE.getName().fullName + ",";
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
