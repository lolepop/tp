package seedu.address.model.tag.restricted;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TutorialTagSchemaTest {
    public static final String VALID_TUTORIAL_TAG = "D24";
    public static final String INVALID_TUTORIAL_TAG = "d24";

    private TutorialTagSchema schema = new TutorialTagSchema();

    @Test
    public void testValidTags() {
        assertTrue(schema.isTagValid(VALID_TUTORIAL_TAG));
        assertTrue(schema.isTagValid("8"));
        assertTrue(schema.isTagValid("10"));
        assertTrue(schema.isTagValid("9"));
        assertTrue(schema.isTagValid("A12"));
    }

    @Test
    public void testInvalidTags() {
        assertFalse(schema.isTagValid(INVALID_TUTORIAL_TAG)); // lowercase not allowed
        assertFalse(schema.isTagValid("D")); // too short without digit
        assertFalse(schema.isTagValid("D245")); // more than 2 digits
        assertFalse(schema.isTagValid("100")); // 3 digits
        assertFalse(schema.isTagValid(""));
        assertFalse(schema.isTagValid("AB24")); // two letters not allowed
    }

    @Test
    public void testValidTag_optionalCourseSuffix() {
        assertTrue(schema.isTagValid(VALID_TUTORIAL_TAG + "-" + CourseTagSchemaTest.VALID_COURSE_TAG));
    }

    @Test
    public void testInvalidTag_optionalCourseSuffix() {
        assertFalse(schema.isTagValid(CourseTagSchemaTest.VALID_COURSE_TAG));
        assertFalse(schema.isTagValid("-" + CourseTagSchemaTest.VALID_COURSE_TAG));
        assertFalse(schema.isTagValid(VALID_TUTORIAL_TAG + "-"));
        assertFalse(schema.isTagValid(VALID_TUTORIAL_TAG + "-" + CourseTagSchemaTest.INVALID_COURSE_TAG));
    }

    @Test
    public void testGetConstraintViolationMessage() {
        assertEquals(TutorialTagSchema.MESSAGE_CONSTRAINTS, schema.getConstraintViolationMessage());
    }

    @Test
    public void getVariant() {
        assertEquals(TutorialTagSchema.VARIANT, schema.getVariant());
    }
}
