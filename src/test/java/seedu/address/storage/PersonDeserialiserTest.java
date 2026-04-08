package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Position;
import seedu.address.model.person.TeachingStaff;
import seedu.address.model.person.TimeSlot;
import seedu.address.model.person.Username;
import seedu.address.model.tag.TagFactory;
import seedu.address.storage.exceptions.DeserialisePersonException;
import seedu.address.testutil.PersonBuilder;


public class PersonDeserialiserTest {

    @Test
    public void deserialise_studentStrRepWithoutTags_returnsValidPerson() {
        String studentStrRep = "Student,\"John Doe\",91234567,johndoe,john@example.com,";
        PersonDeserialiser deserialiser = new PersonDeserialiser(studentStrRep);
        Person student = assertDoesNotThrow(() -> deserialiser.deserialise(),
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
    public void deserialise_studentStrRepWithTags_returnsValidPerson() {
        String studentStrRep = "Student,\"Alice Smith\",81234567,alicesmith,alice@example.com,cs2103;tutee";
        PersonDeserialiser deserialiser = new PersonDeserialiser(studentStrRep);
        Person student = assertDoesNotThrow(() -> deserialiser.deserialise(),
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
    public void deserialise_teachingStaffNoAvailNoTags_returnsValidTeachingStaff() {
        String staffStrRep = "Teaching Assistant,\"Prof Benson\",87654321,profbenson,prof@example.com,,";
        PersonDeserialiser deserialiser = new PersonDeserialiser(staffStrRep);
        Person staff = assertDoesNotThrow(() -> deserialiser.deserialise(),
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
    public void deserialise_teachingStaffNoAvailWithTags_returnsValidTeachingStaff() {
        String staffStrRep = "Teaching Assistant,\"Prof Benson\",87654321,profbenson,prof@example.com,cs2103;tutee,";
        PersonDeserialiser deserialiser = new PersonDeserialiser(staffStrRep);
        Person staff = assertDoesNotThrow(() -> deserialiser.deserialise(),
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
    public void deserialise_teachingStaffWithAvailNoTags_returnsValidTeachingStaff() {
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
        String staffStrRep = "Professors,\"Prof Alice\",91111111,profalice,profalice@example.com,,mon-10-12;wed-14-16";

        PersonDeserialiser deserialiser = new PersonDeserialiser(staffStrRep);
        Person staff = assertDoesNotThrow(() -> deserialiser.deserialise(),
                "Valid staff csv string rep with avail, no tags should not throw DeserialisePersonException");
        assertEquals(expectedStaff, staff);
    }

    @Test
    public void deserialise_teachingStaffWithAvailWithTags_returnsValidTeachingStaff() {
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
                "Professors,\"Prof Alice\",91111111,profalice,profalice@example.com,lecturer,mon-10-12;wed-14-16";

        PersonDeserialiser deserialiser = new PersonDeserialiser(staffStrRep);
        Person staff = assertDoesNotThrow(() -> deserialiser.deserialise(),
                "Valid staff csv string rep with avail and tags should not throw DeserialisePersonException");
        assertEquals(expectedStaff, staff);
    }

    @Test
    public void deserialise_noPosField_throwsDeserialisePersonException() {
        String personStrRep = "";
        PersonDeserialiser deserialiser = new PersonDeserialiser(personStrRep);
        assertThrows(DeserialisePersonException.class, () -> deserialiser.deserialise());
    }

    @Test
    public void deserialise_noNameField_throwsDeserialisePersonException() {
        String personStrRep = "Student";
        PersonDeserialiser deserialiser = new PersonDeserialiser(personStrRep);
        assertThrows(DeserialisePersonException.class, () -> deserialiser.deserialise());
    }

    @Test
    public void deserialise_noPhoneField_throwsDeserialisePersonException() {
        String personStrRep = "Student,\"Prof Alice\"";
        PersonDeserialiser deserialiser = new PersonDeserialiser(personStrRep);
        assertThrows(DeserialisePersonException.class, () -> deserialiser.deserialise());
    }

    @Test
    public void deserialise_noUsernameField_throwsDeserialisePersonException() {
        String personStrRep = "Student,\"Prof Alice\",91111111";
        PersonDeserialiser deserialiser = new PersonDeserialiser(personStrRep);
        assertThrows(DeserialisePersonException.class, () -> deserialiser.deserialise());
    }

    @Test
    public void deserialise_noEmailField_throwsDeserialisePersonException() {
        String personStrRep = "Student,\"Prof Alice\",91111111,profalice";
        PersonDeserialiser deserialiser = new PersonDeserialiser(personStrRep);
        assertThrows(DeserialisePersonException.class, () -> deserialiser.deserialise());
    }

    @Test
    public void deserialise_emptyPosField_throwsDeserialisePersonException() {
        String personStrRep = ",\"Prof Alice\",91111111,profalice,profalice@example.com,lecturer,mon-10-12;wed-14-16";
        PersonDeserialiser deserialiser = new PersonDeserialiser(personStrRep);
        assertThrows(DeserialisePersonException.class, () -> deserialiser.deserialise());
    }

    @Test
    public void deserialise_invalidPosField_throwsDeserialisePersonException() {
        String personStrRep =
                "invalid,\"Prof Alice\",91111111,profalice,profalice@example.com,lecturer,mon-10-12;wed-14-16";
        PersonDeserialiser deserialiser = new PersonDeserialiser(personStrRep);
        assertThrows(DeserialisePersonException.class, () -> deserialiser.deserialise());
    }

    @Test
    public void deserialise_emptyNameField_throwsDeserialisePersonException() {
        String personStrRep = "Student,,91111111,profalice,profalice@example.com,lecturer";
        PersonDeserialiser deserialiser = new PersonDeserialiser(personStrRep);
        assertThrows(DeserialisePersonException.class, () -> deserialiser.deserialise());
    }

    @Test
    public void deserialise_invalidNameField_throwsDeserialisePersonException() {
        String personStrRep = "Student,\" whitespaceAsFirstChar\",91111111,profalice,profalice@example.com,lecturer";
        PersonDeserialiser deserialiser = new PersonDeserialiser(personStrRep);
        assertThrows(DeserialisePersonException.class, () -> deserialiser.deserialise());
    }

    @Test
    public void deserialise_emptyPhoneField_throwsDeserialisePersonException() {
        String personStrRep = "Student,\"Alice\",,profalice,profalice@example.com,lecturer";
        PersonDeserialiser deserialiser = new PersonDeserialiser(personStrRep);
        assertThrows(DeserialisePersonException.class, () -> deserialiser.deserialise());
    }

    @Test
    public void deserialise_invalidPhoneField_throwsDeserialisePersonException() {
        String personStrRep = "Student,\"Alice\",invalidphone,profalice,profalice@example.com,lecturer";
        PersonDeserialiser deserialiser = new PersonDeserialiser(personStrRep);
        assertThrows(DeserialisePersonException.class, () -> deserialiser.deserialise());
    }

    @Test
    public void deserialise_emptyUsernameField_throwsDeserialisePersonException() {
        String personStrRep = "Student,\"Alice\",91111111,,profalice@example.com,lecturer";
        PersonDeserialiser deserialiser = new PersonDeserialiser(personStrRep);
        assertThrows(DeserialisePersonException.class, () -> deserialiser.deserialise());
    }

    @Test
    public void deserialise_invalidUsernameField_throwsDeserialisePersonException() {
        String personStrRep = "Student,\"Alice\",91111111,**invalidusername**,profalice@example.com,lecturer";
        PersonDeserialiser deserialiser = new PersonDeserialiser(personStrRep);
        assertThrows(DeserialisePersonException.class, () -> deserialiser.deserialise());
    }

    @Test
    public void deserialise_emptyEmailField_throwsDeserialisePersonException() {
        String personStrRep = "Student,\"Alice\",91111111,profalice,,lecturer";
        PersonDeserialiser deserialiser = new PersonDeserialiser(personStrRep);
        assertThrows(DeserialisePersonException.class, () -> deserialiser.deserialise());
    }

    @Test
    public void deserialise_invalidEmailField_throwsDeserialisePersonException() {
        String personStrRep = "Student,\"Alice\",91111111,profalice,invalidemail,lecturer";
        PersonDeserialiser deserialiser = new PersonDeserialiser(personStrRep);
        assertThrows(DeserialisePersonException.class, () -> deserialiser.deserialise());
    }

    @Test
    public void deserialise_invalidTagField_throwsDeserialisePersonException() {
        String personStrRep = "Student,\"Alice\",91111111,profalice,profalice@example.com,*not-alphanumeric";
        PersonDeserialiser deserialiser = new PersonDeserialiser(personStrRep);
        assertThrows(DeserialisePersonException.class, () -> deserialiser.deserialise());
    }

    @Test
    public void deserialise_invalidTimeslotField_throwsDeserialisePersonException() {
        String personStrRep = "Professors,\"Alice\",91111111,profalice,profalice@example.com,lecturer,invalid-timeslot";
        PersonDeserialiser deserialiser = new PersonDeserialiser(personStrRep);
        assertThrows(DeserialisePersonException.class, () -> deserialiser.deserialise());
    }

    // TODO: Need to add tests for more special and valid names, like S/O, etc
}
