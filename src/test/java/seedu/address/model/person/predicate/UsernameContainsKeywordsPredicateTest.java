package seedu.address.model.person.predicate;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;


public class UsernameContainsKeywordsPredicateTest {
    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        UsernameContainsKeywordsPredicate firstPredicate =
                new UsernameContainsKeywordsPredicate(firstPredicateKeywordList);
        UsernameContainsKeywordsPredicate secondPredicate =
                new UsernameContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        UsernameContainsKeywordsPredicate firstPredicateCopy =
                new UsernameContainsKeywordsPredicate(firstPredicateKeywordList);
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
        // One keyword
        UsernameContainsKeywordsPredicate predicate =
                new UsernameContainsKeywordsPredicate(Collections.singletonList("alice"));
        assertTrue(predicate.test(new PersonBuilder().withUsername("aliceee").build()));

        // Multiple keywords
        predicate = new UsernameContainsKeywordsPredicate(Arrays.asList("alice", "bob"));
        assertTrue(predicate.test(new PersonBuilder().withUsername("alicebob").build()));

        // Only one matching keyword
        predicate = new UsernameContainsKeywordsPredicate(Arrays.asList("bob", "gmail"));
        assertTrue(predicate.test(new PersonBuilder().withUsername("bob").build()));

        // Mixed-case keywords
        predicate = new UsernameContainsKeywordsPredicate(Arrays.asList("aLIce", "bOB"));
        assertTrue(predicate.test(new PersonBuilder().withUsername("bob").build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        UsernameContainsKeywordsPredicate predicate = new UsernameContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withUsername("bob").build()));

        // Non-matching keyword
        predicate = new UsernameContainsKeywordsPredicate(Arrays.asList("carol"));
        assertFalse(predicate.test(new PersonBuilder().withUsername("bob").build()));

        // Keywords match phone, email and username, but does not match name
        predicate = new UsernameContainsKeywordsPredicate(Arrays.asList("12345678", "Alicee", "alice@email.com"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alicee").withPhone("12345678")
                .withEmail("alice@email.com").withUsername("alicebee").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        UsernameContainsKeywordsPredicate predicate = new UsernameContainsKeywordsPredicate(keywords);

        String expected = UsernameContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
