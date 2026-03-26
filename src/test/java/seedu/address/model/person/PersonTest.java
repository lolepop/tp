package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_USERNAME_BOB;
import static seedu.address.testutil.TypicalPersons.ALICE;
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

        // same name, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withUsername(VALID_USERNAME_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSamePerson(editedBob));

        // name has extra word appended, all other attributes same -> returns false
        String nameWithExtraWord = VALID_NAME_BOB + " Jr";
        editedBob = new PersonBuilder(BOB).withName(nameWithExtraWord).build();
        assertFalse(BOB.isSamePerson(editedBob));
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
