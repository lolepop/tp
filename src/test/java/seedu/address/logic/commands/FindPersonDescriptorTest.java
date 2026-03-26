package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand.FindPersonDescriptor;
import seedu.address.model.tag.Tag;

public class FindPersonDescriptorTest {
    @Test
    public void equals() {
        // same values -> returns true
        FindPersonDescriptor fdOne = new FindPersonDescriptor();
        FindPersonDescriptor fdTwo = new FindPersonDescriptor();
        assertTrue(fdOne.equals(fdTwo));

        // same object -> returns true
        assertTrue(fdOne.equals(fdOne));

        // null -> returns false
        assertFalse(fdOne.equals(null));

        // different types -> returns false
        assertFalse(fdOne.equals(5));

        // different values -> returns false
        fdOne.setName(Set.of("test1", "test2", "test3"));
        assertFalse(fdOne.equals(fdTwo));

        // different names -> returns false
        fdTwo.setName(Set.of("test4", "test5", "test6"));
        assertFalse(fdOne.equals(fdTwo));

        // test copy -> returns true
        fdOne = new FindPersonDescriptor(fdTwo);
        assertTrue(fdOne.equals(fdOne));

        // different phone -> returns false
        fdOne = new FindPersonDescriptor();
        fdOne.setPhone(Set.of("11111111", "22222222", "3333333"));
        fdTwo = new FindPersonDescriptor();
        fdTwo.setName(Set.of("44444444", "55555555", "666666666"));
        assertFalse(fdOne.equals(fdTwo));

        // different email -> returns false
        fdOne = new FindPersonDescriptor();
        fdOne.setEmail(Set.of("test1@test2.com", "test1", "gmail.com"));
        fdTwo = new FindPersonDescriptor();
        fdTwo.setEmail(Set.of("test2@test3.com", "test2", "nus.edu.sg"));
        assertFalse(fdOne.equals(fdTwo));

        // different tags -> returns false
        fdOne = new FindPersonDescriptor();
        fdOne.setTags(Set.of(new Tag("friends"), new Tag("classmates")));
        fdTwo = new FindPersonDescriptor();
        fdTwo.setTags(Set.of(new Tag("colleagues"), new Tag("students")));
        assertFalse(fdOne.equals(fdTwo));

        // different usernames -> returns false
        fdOne = new FindPersonDescriptor();
        fdOne.setUsername(Set.of("first", "second"));
        fdTwo = new FindPersonDescriptor();
        fdTwo.setUsername(Set.of("colleagues", "students"));
        assertFalse(fdOne.equals(fdTwo));


    }

    @Test
    public void toStringMethod() {
        FindPersonDescriptor fd = new FindPersonDescriptor();
        String expected = FindPersonDescriptor.class.getCanonicalName() + "{name="
                + fd.getName().orElse(null) + ", username="
                + fd.getUsername().orElse(null) + ", phone="
                + fd.getPhone().orElse(null) + ", email="
                + fd.getEmail().orElse(null) + ", tags="
                + fd.getTags().orElse(null) + "}";
        assertEquals(expected, fd.toString());
    }
}
