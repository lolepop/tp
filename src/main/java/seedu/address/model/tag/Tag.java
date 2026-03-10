package seedu.address.model.tag;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag extends AbstractTag {

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        super(tagName);
    }

    @Override
    public TagType getTagType() {
        return TagType.TAG;
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }
}
