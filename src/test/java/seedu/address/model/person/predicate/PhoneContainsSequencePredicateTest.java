package seedu.address.model.person.predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PhoneContainsSequencePredicateTest {
    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("912345");
        List<String> secondPredicateKeywordList = Arrays.asList("912345", "543212");

        PhoneContainsSequencePredicate firstPredicate = new PhoneContainsSequencePredicate(firstPredicateKeywordList);
        PhoneContainsSequencePredicate secondPredicate = new PhoneContainsSequencePredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PhoneContainsSequencePredicate firstPredicateCopy =
                new PhoneContainsSequencePredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void testEmailContainsKeywords_returnsTrue() {
        // One sequence
        PhoneContainsSequencePredicate predicate =
                new PhoneContainsSequencePredicate(Collections.singletonList("345"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("91234567").build()));

        // Multiple sequence
        predicate = new PhoneContainsSequencePredicate(Arrays.asList("567", "123"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("91234567").build()));

        // Only one matching sequence
        predicate = new PhoneContainsSequencePredicate(Arrays.asList("567", "987"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("91234567").build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Empty Sequence
        PhoneContainsSequencePredicate predicate = new PhoneContainsSequencePredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withPhone("98765413").build()));

        // Non-matching sequence
        predicate = new PhoneContainsSequencePredicate(Arrays.asList("8765"));
        assertFalse(predicate.test(new PersonBuilder().withPhone("91234567").build()));

        // Keywords match name, email and username, but does not match phone
        predicate = new PhoneContainsSequencePredicate(Arrays.asList("Alicee", "Alicee", "alicebee"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alicee").withPhone("12345678")
                .withEmail("alice@email.com").withUsername("alicebee").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> sequence = List.of("8765", "4567");
        PhoneContainsSequencePredicate predicate = new PhoneContainsSequencePredicate(sequence);

        String expected = PhoneContainsSequencePredicate.class.getCanonicalName() + "{sequence=" + sequence + "}";
        assertEquals(expected, predicate.toString());
    }
}

