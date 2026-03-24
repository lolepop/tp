package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public abstract class AbstractTag {
    protected final String tagName;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public AbstractTag(String tagName) {
        requireNonNull(tagName);
        this.tagName = tagName;
    }

    public abstract TagType getTagType();

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AbstractTag otherTag)) {
            return false;
        }

        return tagName.equals(otherTag.tagName) && getTagType() == otherTag.getTagType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName, getTagType());
    }

    public String getTagName() {
        return tagName;
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagName + ']';
    }
}
