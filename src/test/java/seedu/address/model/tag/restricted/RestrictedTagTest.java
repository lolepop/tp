package seedu.address.model.tag.restricted;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.tag.restricted.TagSchemaTest.VALID_PREFIX;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagFactoryTest;
import seedu.address.model.tag.TagType;
import seedu.address.model.tag.restricted.TagSchemaTest.TagSchemaStub;

public class RestrictedTagTest {
    public static final String VALID_TAG_WITH_DELIMITER = VALID_PREFIX + RestrictedTag.DELIMITER + "t123";
    public static final String OTHER_VALID_TAG = VALID_PREFIX + RestrictedTag.DELIMITER + "other";
    public static final String INVALID_TAG_NO_DELIMITER = "t123";
    public static final String INVALID_TAG_EMPTY_VALUE = VALID_PREFIX + RestrictedTag.DELIMITER;
    public static final String INVALID_TAG_INVALID_VALUE = VALID_PREFIX + RestrictedTag.DELIMITER + "invalid";
    public static final String VALID_TAG_WITH_NUMBERS = "tag123";

    @Test
    public void constructor_nullSchema_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new RestrictedTag(null, VALID_TAG_WITH_DELIMITER));
    }

    @Test
    public void constructor_invalidTagNameThrowsIllegalArgumentException() {
        TagSchemaStub schema = new TagSchemaStub(VALID_PREFIX);
        assertThrows(IllegalArgumentException.class, () -> new RestrictedTag(schema, INVALID_TAG_NO_DELIMITER));
    }

    @Test
    public void constructor_invalidTagEmptyValueThrowsIllegalArgumentException() {
        TagSchemaStub schema = new TagSchemaStub(VALID_PREFIX);
        assertThrows(IllegalArgumentException.class, () -> new RestrictedTag(schema, INVALID_TAG_EMPTY_VALUE));
    }

    @Test
    public void constructor_invalidTagInvalidValueThrowsIllegalArgumentException() {
        TagSchemaStub schema = new TagSchemaStub(VALID_PREFIX);
        assertThrows(IllegalArgumentException.class, () -> new RestrictedTag(schema, INVALID_TAG_INVALID_VALUE));
    }

    @Test
    public void constructor_validTagName_success() {
        TagSchemaStub schema = new TagSchemaStub(VALID_PREFIX);
        var restrictedTag = new RestrictedTag(schema, VALID_TAG_WITH_DELIMITER);
        assertEquals(VALID_TAG_WITH_DELIMITER, restrictedTag.getTagName());
    }

    @Test
    public void equals_sameTagName_returnsTrue() {
        TagSchemaStub schema = new TagSchemaStub(VALID_PREFIX);
        var tag1 = new RestrictedTag(schema, VALID_TAG_WITH_DELIMITER);
        var tag2 = new RestrictedTag(schema, VALID_TAG_WITH_DELIMITER);
        assertEquals(tag1, tag2);
    }

    @Test
    public void equals_differentTagName_returnsFalse() {
        TagSchemaStub schema = new TagSchemaStub(VALID_PREFIX);
        var tag1 = new RestrictedTag(schema, VALID_TAG_WITH_DELIMITER);
        var tag2 = new RestrictedTag(schema, OTHER_VALID_TAG);
        assertNotEquals(tag1, tag2);
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        TagSchemaStub schema = new TagSchemaStub(VALID_PREFIX);
        var tag = new RestrictedTag(schema, VALID_TAG_WITH_DELIMITER);
        assertEquals(tag, tag);
    }

    @Test
    public void equals_nullObject_returnsFalse() {
        TagSchemaStub schema = new TagSchemaStub(VALID_PREFIX);
        var tag = new RestrictedTag(schema, VALID_TAG_WITH_DELIMITER);
        assertFalse(tag.equals(null));
    }

    @Test
    public void equals_differentClass_returnsFalse() {
        TagSchemaStub schema = new TagSchemaStub(VALID_PREFIX);
        var tag = new RestrictedTag(schema, VALID_TAG_WITH_DELIMITER);
        var otherTag = new Tag(VALID_TAG_WITH_NUMBERS);
        assertFalse(tag.equals(otherTag));
    }

    @Test
    public void hashCode_sameTagName_sameHashCode() {
        TagSchemaStub schema = new TagSchemaStub(VALID_PREFIX);
        var tag1 = new RestrictedTag(schema, VALID_TAG_WITH_DELIMITER);
        var tag2 = new RestrictedTag(schema, VALID_TAG_WITH_DELIMITER);
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void hashCode_differentTagName_differentHashCode() {
        TagSchemaStub schema = new TagSchemaStub(VALID_PREFIX);
        var tag1 = new RestrictedTag(schema, VALID_TAG_WITH_DELIMITER);
        var tag2 = new RestrictedTag(schema, OTHER_VALID_TAG);
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void toString_validTagName_correctFormat() {
        TagSchemaStub schema = new TagSchemaStub(VALID_PREFIX);
        var tag = new RestrictedTag(schema, VALID_TAG_WITH_DELIMITER);
        assertEquals("[" + VALID_TAG_WITH_DELIMITER + "]", tag.toString());
    }

    @Test
    public void isRestrictedTag_returnsTrue_withDelimiter() {
        assertTrue(RestrictedTag.isRestrictedTag("prefix:value"));
    }

    @Test
    public void isRestrictedTag_returnsFalse_withoutDelimiter() {
        assertFalse(RestrictedTag.isRestrictedTag("prefixvalue"));
    }

    @Test
    public void tagParts_parse_success() {
        String tag = "prefix:value";
        Optional<RestrictedTag.TagParts> parsed = RestrictedTag.TagParts.parse(tag);
        assertTrue(parsed.isPresent());
        assertEquals("prefix", parsed.get().prefix);
        assertEquals("value", parsed.get().value);
    }

    @Test
    public void tagParts_parseNull_fails() {
        assertThrows(NullPointerException.class, () -> RestrictedTag.TagParts.parse(null));
    }

    @Test
    public void tagParts_parseEmptyString_fails() {
        Optional<RestrictedTag.TagParts> parsed = RestrictedTag.TagParts.parse("");
        assertFalse(parsed.isPresent());
    }

    @Test
    public void tagParts_parseMultipleDelimiters_returnsOnlyFirst() {
        String tag = "prefix:value:extra";
        Optional<RestrictedTag.TagParts> parsed = RestrictedTag.TagParts.parse(tag);
        assertTrue(parsed.isPresent());
        assertEquals("prefix", parsed.get().prefix);
        assertEquals("value:extra", parsed.get().value);
    }

    @Test
    public void tagParts_parseNoDelimiter_returnsEmpty() {
        Optional<RestrictedTag.TagParts> parsed = RestrictedTag.TagParts.parse("nodelimiter");
        assertFalse(parsed.isPresent());
    }

    @Test
    public void getTagType_courseSchema_returnsCourse() {
        var schema = new CourseTagSchema();
        var tag = new RestrictedTag(schema, TagFactoryTest.VALID_COURSE_FULL_TAG);
        assertEquals(TagType.COURSE, tag.getTagType());
    }

    @Test
    public void getTagType_tutorialSchema_returnsTutorial() {
        var schema = new TutorialTagSchema();
        var tag = new RestrictedTag(schema, TagFactoryTest.VALID_TUTORIAL_FULL_TAG);
        assertEquals(TagType.TUTORIAL, tag.getTagType());
    }

    @Test
    public void getTagType_labSchema_returnsLab() {
        var schema = new LabTagSchema();
        var tag = new RestrictedTag(schema, TagFactoryTest.VALID_LAB_FULL_TAG);
        assertEquals(TagType.LAB, tag.getTagType());
    }

    @Test
    public void getTagType_unknownSchema_returnsTag() {
        var schema = new TagSchemaStub("unknown");
        var tag = new RestrictedTag(schema, "unknown:value");
        assertEquals(TagType.TAG, tag.getTagType());
    }
}
