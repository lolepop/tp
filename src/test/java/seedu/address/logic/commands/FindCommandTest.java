package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand.FindPersonDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model) for
 * {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        FindPersonDescriptor firstFd = new FindPersonDescriptor();
        firstFd.setName(new HashSet<>(Collections.singletonList("first")));

        FindPersonDescriptor secondFd = new FindPersonDescriptor();
        secondFd.setName(new HashSet<>(Collections.singletonList("second")));


        FindCommand findFirstCommand = new FindCommand(firstFd);
        FindCommand findSecondCommand = new FindCommand(secondFd);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstFd);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        FindPersonDescriptor fd = new FindPersonDescriptor();
        fd.setName(processInput(" "));
        FindCommand command = new FindCommand(fd);
        expectedModel.updateFilteredPersonList(fd.getNamePredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        FindPersonDescriptor fd = new FindPersonDescriptor();
        fd.setName(processInput("Kurz Elle Kunz"));
        FindCommand command = new FindCommand(fd);
        expectedModel.updateFilteredPersonList(fd.getNamePredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());
    }

    @Test
    public void execute_substringKeyword_personFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        FindPersonDescriptor fd = new FindPersonDescriptor();
        fd.setName(processInput("el"));
        FindCommand command = new FindCommand(fd);
        expectedModel.updateFilteredPersonList(fd.getNamePredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(DANIEL, ELLE), model.getFilteredPersonList());
    }

    @Test
    public void execute_substringKeywordCaseInsensitive_personFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        FindPersonDescriptor fd = new FindPersonDescriptor();
        fd.setName(processInput("El"));
        FindCommand command = new FindCommand(fd);
        expectedModel.updateFilteredPersonList(fd.getNamePredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(DANIEL, ELLE), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleSubstringKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        FindPersonDescriptor fd = new FindPersonDescriptor();
        fd.setName(processInput("ur est"));
        FindCommand command = new FindCommand(fd);
        expectedModel.updateFilteredPersonList(fd.getNamePredicate());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, GEORGE), model.getFilteredPersonList());
    }

    @Test
    public void execute_nameAndTagPredicate_personFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        FindPersonDescriptor fd = new FindPersonDescriptor();
        fd.setName(processInput("Daniel"));
        fd.setTags(Set.of(new Tag("friends")));
        FindCommand command = new FindCommand(fd);
        expectedModel.updateFilteredPersonList(
                person -> fd.getNamePredicate().test(person) && fd.getTagsPredicate().test(person));
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(DANIEL), model.getFilteredPersonList());
    }

    @Test
    public void execute_nameAndTagAndEmailPredicate_personFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        FindPersonDescriptor fd = new FindPersonDescriptor();
        fd.setName(processInput("Daniel"));
        fd.setTags(Set.of(new Tag("friends")));
        fd.setEmail(Set.of("cornelia"));
        FindCommand command = new FindCommand(fd);
        expectedModel.updateFilteredPersonList(
                person -> fd.getNamePredicate().test(person) && fd.getTagsPredicate().test(person)
                        && fd.getEmailPredicate().test(person));
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(DANIEL), model.getFilteredPersonList());
    }

    @Test
    public void equals_withTagPredicate() {
        Set<String> names = processInput("Alice");
        Set<Tag> tags = Set.of(new Tag("colleague"));

        FindPersonDescriptor fdBoth = new FindPersonDescriptor();
        fdBoth.setName(names);
        fdBoth.setTags(tags);
        FindCommand commandWithBoth = new FindCommand(fdBoth);


        FindPersonDescriptor fdName = new FindPersonDescriptor();
        fdBoth.setName(names);
        FindCommand commandWithNameOnly = new FindCommand(fdName);

        // same object -> returns true
        assertTrue(commandWithBoth.equals(commandWithBoth));

        // same values -> returns true
        FindCommand commandWithBothCopy = new FindCommand(fdBoth);
        assertTrue(commandWithBoth.equals(commandWithBothCopy));

        // different types -> returns false
        assertFalse(commandWithBoth.equals(1));

        // null -> returns false
        assertFalse(commandWithBoth.equals(null));

        // different predicates -> returns false
        assertFalse(commandWithBoth.equals(commandWithNameOnly));
    }

    @Test
    public void toStringMethod() {
        FindPersonDescriptor fd = new FindPersonDescriptor();
        fd.setName(processInput("keyword"));
        FindCommand findCommand = new FindCommand(fd);
        String expected = FindCommand.class.getCanonicalName() + "{findPersonDescriptor=" + fd + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private Set<String> processInput(String userInput) {
        return new HashSet<>(Arrays.asList(userInput.split("\\s+")));
    }
}
