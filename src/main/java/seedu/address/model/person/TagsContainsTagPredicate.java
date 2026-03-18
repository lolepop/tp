package seedu.address.model.person;

import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class TagsContainsTagPredicate implements Predicate<Person> {
    private final Set<Tag> tags;

    public TagsContainsTagPredicate(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean test(Person person) {
        return person.tags.stream().anyMatch(tag -> tags.contains(tag));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagsContainsTagPredicate)) {
            return false;
        }

        TagsContainsTagPredicate otherTagsContainsTagPredicate = (TagsContainsTagPredicate) other;
        return tags.equals(otherTagsContainsTagPredicate.tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("Tags", tags).toString();
    }
}
