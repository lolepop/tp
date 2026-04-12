package seedu.address.model.tag.restricted;

/**
 * A concrete implementation of {@link RegexTagSchema} specifically for lab
 * tags.
 * <p>
 * Valid lab tags typically follow the format of an optional uppercase letter
 * followed by up to two digits (e.g., "D24", "8").
 * <p>
 * Currently, this formatting is the same as Tutorial tags
 */
public class LabTagSchema extends RegexTagSchema {
    public static final String VARIANT = "lab";
    public static final String TAG_PATTERN = "[A-Z]?\\d{1,2}(-" + CourseTagSchema.TAG_PATTERN + ")?";
    public static final String MESSAGE_CONSTRAINTS = "Lab tag expects format of an optional uppercase letter "
            + "followed by maximum 2 numbers.\nOPTIONAL: an associated course is attachable at the end "
            + "with a dash (-). " + CourseTagSchema.MESSAGE_CONSTRAINTS + "\n"
            + "Valid lab: 'D24-CS2103T' and '8'";

    /**
     * Constructs a {@code LabTagSchema}.
     */
    public LabTagSchema() {
        super(VARIANT, TAG_PATTERN);
    }

    @Override
    public String getConstraintViolationMessage() {
        return MESSAGE_CONSTRAINTS;
    }
}
