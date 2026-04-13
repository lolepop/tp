package seedu.address.model.tag.restricted;

/**
 * A concrete implementation of {@link RegexTagSchema} specifically for lab
 * tags.
 * <p>
 * Valid course tags typically follow the format of an 2-4 uppercase letters
 * followed by four digits and potentially an uppercase suffix (e.g., "CS2103T",
 * "MA1521", "GESS1000T").
 */
public class CourseTagSchema extends RegexTagSchema {
    public static final String VARIANT = "course";
    public static final String TAG_PATTERN = "[A-Z]{2,4}\\d{4}[A-Z]?";
    public static final String MESSAGE_CONSTRAINTS = "Course code expects format: 2-4 uppercase letters followed by "
            + "4 numbers, and an optional uppercase suffix. Valid course (example): 'CS2103T', 'MA1521', 'GESS1000T'";

    /**
     * Constructs a {@code CourseTagSchema}.
     */
    public CourseTagSchema() {
        super(VARIANT, TAG_PATTERN);
    }

    @Override
    public String getConstraintViolationMessage() {
        return MESSAGE_CONSTRAINTS;
    }
}
