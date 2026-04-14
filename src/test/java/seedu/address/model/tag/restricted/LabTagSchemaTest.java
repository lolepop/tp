package seedu.address.model.tag.restricted;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

// these are the exact same tests as the one in tutorial tag
public class LabTagSchemaTest {
    public static final String VALID_LAB_TAG = "D24";
    public static final String INVALID_LAB_TAG = "d24";

    private LabTagSchema schema = new LabTagSchema();

    @Test
    public void testValidTags() {
        assertTrue(schema.isTagValid(VALID_LAB_TAG));
        assertTrue(schema.isTagValid("8"));
        assertTrue(schema.isTagValid("10"));
        assertTrue(schema.isTagValid("9"));
        assertTrue(schema.isTagValid("A12"));
    }

    @Test
    public void testInvalidTags() {
        assertFalse(schema.isTagValid(INVALID_LAB_TAG));
        assertFalse(schema.isTagValid("D"));
        assertFalse(schema.isTagValid("D245"));
        assertFalse(schema.isTagValid("100"));
        assertFalse(schema.isTagValid(""));
        assertFalse(schema.isTagValid("AB24"));
    }

    @Test
    public void testValidTag_optionalCourseSuffix() {
        assertTrue(schema.isTagValid(VALID_LAB_TAG + "-" + CourseTagSchemaTest.VALID_COURSE_TAG));
    }

    @Test
    public void testInvalidTag_optionalCourseSuffix() {
        assertFalse(schema.isTagValid(CourseTagSchemaTest.VALID_COURSE_TAG));
        assertFalse(schema.isTagValid("-" + CourseTagSchemaTest.VALID_COURSE_TAG));
        assertFalse(schema.isTagValid(VALID_LAB_TAG + "-"));
        assertFalse(schema.isTagValid(VALID_LAB_TAG + "-" + CourseTagSchemaTest.INVALID_COURSE_TAG));
    }

    @Test
    public void testGetConstraintViolationMessage() {
        assertEquals(LabTagSchema.MESSAGE_CONSTRAINTS, schema.getConstraintViolationMessage());
    }

    @Test
    public void getVariant() {
        assertEquals(LabTagSchema.VARIANT, schema.getVariant());
    }
}
