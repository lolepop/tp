package seedu.address.model.person.predicate;

import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;
import seedu.address.model.tag.AbstractTag;

/**
 * Tests that a {@code Person}'s {@code Tags} matches any of the keywords given.
 */
public class TagsContainsTagPredicate implements Predicate<Person> {
    private final Set<AbstractTag> tags;

    public TagsContainsTagPredicate(Set<AbstractTag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean test(Person person) {
        assert tags != null;
        return tags.stream().anyMatch(tag -> person.containsTag(tag));
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
