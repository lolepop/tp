package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.TeachingStaff;
import seedu.address.model.tag.AbstractTag;

public class SampleDataUtilTest {

    @Test
    public void getSamplePersons_returnsExpectedEntries() {
        Person[] persons = SampleDataUtil.getSamplePersons();
        assertEquals(6, persons.length);
        assertTrue(persons[1] instanceof TeachingStaff);
        assertTrue(persons[3] instanceof TeachingStaff);
        assertTrue(persons[5] instanceof TeachingStaff);
    }

    @Test
    public void getSampleAddressBook_containsAllSamplePersons() {
        ReadOnlyAddressBook addressBook = SampleDataUtil.getSampleAddressBook();
        assertEquals(6, addressBook.getPersonList().size());
        assertTrue(addressBook.getPersonList().stream().anyMatch(p -> p.getName().fullName.equals("Alex Yeoh")));
        assertTrue(addressBook.getPersonList().stream().anyMatch(p -> p.getName().fullName.equals("Roy Balakrishnan")));
    }

    @Test
    public void getTagSet_validTags_success() {
        var tags = SampleDataUtil.getTagSet("friends", "tut:A11");
        assertEquals(2, tags.size());
        assertTrue(containsTag(tags, "friends"));
        assertTrue(containsTag(tags, "tut:A11"));
    }

    @Test
    public void getTagSet_invalidTag_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> SampleDataUtil.getTagSet("bad*tag"));
    }

    private boolean containsTag(java.util.Set<AbstractTag> tags, String expected) {
        return tags.stream().anyMatch(tag -> tag.getTagName().equals(expected));
    }
}
