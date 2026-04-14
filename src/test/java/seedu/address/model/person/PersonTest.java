package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_POSITION_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_USERNAME_BOB;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person.MutablePerson;
import seedu.address.model.person.exceptions.ImmutableEscapedScopeException;
import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name, phone, email, username; only tags differ -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // same email and username, different phone -> returns false (different people)
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // same email and phone, different username -> returns false (different people)
        editedAlice = new PersonBuilder(ALICE).withUsername(VALID_USERNAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // same username and phone, different email -> returns false (different people)
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns true
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // name differs in case, all other attributes same -> returns true
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertTrue(BOB.isSamePerson(editedBob));

        // name has extra word appended, all other attributes same -> returns true
        String nameWithExtraWord = VALID_NAME_BOB + " Jr";
        editedBob = new PersonBuilder(BOB).withName(nameWithExtraWord).build();
        assertTrue(BOB.isSamePerson(editedBob));
    }

    @Test
    public void isSamePerson_teachingStaff() {
        // two teaching staff with same identity and same position -> true
        Person bobCopy = new PersonBuilder(BOB).build();
        assertTrue(BOB.isSamePerson(bobCopy));

        // same name, phone, email, username but different position -> false
        Person bobOtherPosition = new PersonBuilder(BOB).withPosition(VALID_POSITION_AMY).build();
        assertFalse(BOB.isSamePerson(bobOtherPosition));

        // student vs teaching staff with same name, phone, email, username -> false
        Person student = new PersonBuilder(AMY).build();
        Person staffWithAmysFields = new PersonBuilder(AMY).withPosition(VALID_POSITION_AMY).build();
        assertFalse(student.isSamePerson(staffWithAmysFields));
        assertFalse(staffWithAmysFields.isSamePerson(student));
    }

    @Test
    public void immutabilityIsPreserved() {
        final var ref = new InteriorMutable();
        ALICE.cloneInto(p -> {
            ref.inner = p;
        });
        assertTrue(ALICE.equals(ref.inner.getPerson()));
        assertThrows(ImmutableEscapedScopeException.class, () -> {
            ref.inner.setName(new Name("this should fail"));
        });
    }

    @Test
    public void immutabilityPreservesSubtype() {
        final var ts = new TeachingStaff(new Name(VALID_NAME_BOB));
        var cloned = ts.cloneInto(f -> {
        });
        assertTrue(cloned instanceof TeachingStaff);
    }

    @Test
    public void cloneIntoTeachingStaff_preserves_behaviour() {
        final var ts = new TeachingStaff(new Name(VALID_NAME_BOB));
        var cloned = ts.cloneInto(f -> {
            f.setName(new Name(VALID_NAME_AMY));
        });
        var clonedTs = ts.cloneIntoTeachingStaff(f -> {
            f.setName(new Name(VALID_NAME_AMY));
        });
        assertEquals(cloned, clonedTs);
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different username -> returns false
        editedAlice = new PersonBuilder(ALICE).withUsername(VALID_USERNAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail() + ", username=" + ALICE.getUsername() + ", tags=" + ALICE.getTags()
                + "}";
        assertEquals(expected, ALICE.toString());
    }

    private class InteriorMutable {
        protected MutablePerson inner;
    }

}
