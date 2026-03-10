package seedu.address.model.tag;

/**
 * Represents types of Tag in the address book.
 */
public enum TagType {
    TAG("Tag"),;

    private final String description;

    TagType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
