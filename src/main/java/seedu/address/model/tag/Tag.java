package seedu.address.model.tag;

import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Objects;

/**
 * Represents a Tag in the address book.
 * Tags are case-insensitive (i.e. tag and Tag are equivalent)
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag extends AbstractTag {
    public static final String MESSAGE_CONSTRAINTS = "Tag names should be alphanumeric and non-empty";
    public static final String VALIDATION_REGEX = "\\p{Alnum}+";

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        super(tagName);
        checkArgument(isValidTagName(tagName), MESSAGE_CONSTRAINTS);
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public TagType getTagType() {
        return TagType.TAG;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName.toLowerCase(), getTagType());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Tag otherTag)) {
            return false;
        }
        return tagName.toLowerCase().equals(otherTag.tagName.toLowerCase()) && getTagType() == otherTag.getTagType();
    }
}
