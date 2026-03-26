package seedu.address.model.person.predicate;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Tests that a {@code Person}'s {@code Phone} matches any of the sequences given.
 */
public class PhoneContainsSequencePredicate implements Predicate<Person> {
    private final List<String> sequence;

    public PhoneContainsSequencePredicate(List<String> sequence) {
        this.sequence = sequence;
    }

    @Override
    public boolean test(Person person) {
        assert sequence != null;
        return sequence.stream()
                .anyMatch(s -> StringUtil.containsSubstringIgnoreCase(person.getPhone().toString(), s));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PhoneContainsSequencePredicate otherPhoneContainsSequencePredicate)) {
            return false;
        }

        return sequence.equals(otherPhoneContainsSequencePredicate.sequence);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("sequence", sequence).toString();
    }
}

